package com.example.surface4pro.bakingtime;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.surface4pro.bakingtime.data.Recipe;
import com.google.gson.Gson;

import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();

    private static final String JSON_REQUEST_ENDPOINT = "https://d17h27t6h515a5.cloudfront.net/topher/2017/May/59121517_baking/baking.json";

    private RequestQueue requestQueue;
    private Gson gson;

    private List<Recipe> recipes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Instantiate new RequestQueue that will handle running the network request in a background thread
        requestQueue = Volley.newRequestQueue(this);

        // Instantiate new Gson instance
        gson = new Gson();

        // Fetch the Recipes from the Server
        fetchRecipes();
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
                        recipes = Arrays.asList(gson.fromJson(response, Recipe[].class));
                    }
                },
                // Listener for if the StringRequest returns an error.
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d(TAG, "Recipe Response: " + error.toString());
                    }
                });

        // Add the request to the RequestQueue
        requestQueue.add(request);
    }
}
