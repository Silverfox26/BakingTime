package com.example.surface4pro.bakingtime;

import android.content.Intent;
import android.content.res.Configuration;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.surface4pro.bakingtime.adapters.RecipeAdapter;
import com.example.surface4pro.bakingtime.data.Recipe;
import com.example.surface4pro.bakingtime.databinding.ActivityMainBinding;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements RecipeAdapter.RecipeAdapterOnClickHandler {

    private static final String JSON_REQUEST_ENDPOINT = "https://d17h27t6h515a5.cloudfront.net/topher/2017/May/59121517_baking/baking.json";
    private static final String RECIPES_KEY = "recipes_instance";

    private RequestQueue requestQueue;
    private Gson gson;

    private RecipeAdapter mAdapter;
    private ActivityMainBinding mBinding;

    private ArrayList<Recipe> recipes = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);

        boolean isTabletSize = getResources().getBoolean(R.bool.isTablet);
        setupRecyclerView(isTabletSize);

        if (savedInstanceState == null) {
            // Instantiate new RequestQueue that will handle running the network request in a background thread
            requestQueue = Volley.newRequestQueue(this);

            // Instantiate new Gson instance
            gson = new Gson();

            // Fetch the Recipes from the Server
            fetchRecipes();
        } else {
            recipes = savedInstanceState.getParcelableArrayList(RECIPES_KEY);
            mAdapter.setRecipeData(recipes);
        }
    }

    /**
     * This method sets up and initializes the RecyclerView
     *
     * @param isTabletSize is the device a tablet or not?
     */
    private void setupRecyclerView(boolean isTabletSize) {

        RecyclerView mRecyclerView = mBinding.recipesRecyclerView;

        // Set GridLayout spanCount depending on screen size and orientation
        int orientation = this.getResources().getConfiguration().orientation;
        int spanCount;
        if (!isTabletSize && orientation == Configuration.ORIENTATION_PORTRAIT) {
            spanCount = 1;
        } else if (isTabletSize && orientation == Configuration.ORIENTATION_LANDSCAPE) {
            spanCount = 3;
        } else {
            spanCount = 2;
        }
        mRecyclerView.setLayoutManager(new GridLayoutManager(this, spanCount));

        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mAdapter = new RecipeAdapter(this, recipes, this);
        mRecyclerView.setAdapter(mAdapter);
        showRecipes();
    }

    /**
     * This method fetches the recipe JSON from the server using a StringRequest.
     */
    private void fetchRecipes() {
        StringRequest request = new StringRequest(Request.Method.GET, JSON_REQUEST_ENDPOINT,
                //Listener for the StringRequest's response
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Deserialize the JSON response into a List of Recipe objects
                        // and set the recipe List to the Adapter.
                        recipes = gson.fromJson(response, new TypeToken<ArrayList<Recipe>>() {
                        }.getType());
                        mAdapter.setRecipeData(recipes);
                        showRecipes();
                    }
                },
                // Listener for if the StringRequest returns an error.
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        showErrorMessage();
                    }
                });

        // Add the request to the RequestQueue
        requestQueue.add(request);
    }

    @Override
    public void onRecipeClicked(Recipe recipe) {
        // Start the RecipeStepsActivity with the recipe that was clicked on
        Intent startRecipeStepsIntent = new Intent(this, RecipeStepsActivity.class);
        startRecipeStepsIntent.putExtra("recipe", recipe);
        startActivity(startRecipeStepsIntent);
    }

    private void showRecipes() {
        mBinding.errorTextView.setVisibility(View.INVISIBLE);
        mBinding.recipesRecyclerView.setVisibility(View.VISIBLE);
    }

    private void showErrorMessage() {
        mBinding.errorTextView.setVisibility(View.VISIBLE);
        mBinding.recipesRecyclerView.setVisibility(View.INVISIBLE);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList(RECIPES_KEY, recipes);
    }
}
