package com.example.surface4pro.bakingtime;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.surface4pro.bakingtime.data.Recipe;
import com.example.surface4pro.bakingtime.data.Step;

public class RecipeStepsActivity extends AppCompatActivity implements StepListFragment.OnRecipeStepClickListener {

    private static final String TAG = RecipeStepsActivity.class.getSimpleName();

    private static final String RECIPE_KEY = "recipe_instance";

    StepListFragment mStepListFragment;
    private Recipe mRecipe;
    // A single-pane display refers to phone screens, and two-pane to larger tablet screens
    private boolean mTwoPane;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_steps);

        // Check if we are in two pane mode
        mTwoPane = findViewById(R.id.steps_linear_layout) != null;

        // If the activity is started for the first time get the intent and
        // add StepListFragment.
        if (savedInstanceState == null) {
            if (getIntent() != null && getIntent().hasExtra("recipe")) {
                Intent receivedIntent = getIntent();
                mRecipe = receivedIntent.getParcelableExtra("recipe");

                // Create a new StepListFragment instance
                mStepListFragment = StepListFragment.newStepListFragmentInstance(mRecipe);

                // Use a FragmentManager and transaction to add the fragment to the screen
                FragmentManager fragmentManager = getSupportFragmentManager();
                fragmentManager.beginTransaction()
                        .add(R.id.recipe_steps_container, mStepListFragment)
                        .commit();
            }
        } else {
            mRecipe = savedInstanceState.getParcelable(RECIPE_KEY);
        }
    }

    // Callback method of the StepListFragment.
    // Gets called, when a recipe step is clicked on in the StepListFragment.
    @Override
    public void onRecipeStepSelected(Step step) {
        Log.d(TAG, "onRecipeStepSelected: " + step.getDescription());
        StepDetailFragment stepDetailFragment = StepDetailFragment.newStepListFragmentInstance(step);

        FragmentManager fragmentManager = getSupportFragmentManager();

        if (mTwoPane) {
            fragmentManager.beginTransaction()
                    .replace(R.id.recipe_step_detail_container, stepDetailFragment)
                    .commit();
        } else {
            fragmentManager.beginTransaction()
                    .setCustomAnimations(
                            R.anim.slide_in_right,
                            R.anim.slide_out_left,
                            R.anim.slide_in_left,
                            R.anim.slide_out_right)
                    .replace(R.id.recipe_steps_container, stepDetailFragment)
                    .addToBackStack(null)
                    .commit();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.add_to_widget, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_add_ingredients_to_widget) {
            BakingTimeWidgetService.updateWidget(this, mRecipe);
            Toast.makeText(this, String.format("%s added to the widget", mRecipe.getName()), Toast.LENGTH_SHORT).show();
            return true;
        } else
            return super.onOptionsItemSelected(item);
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(RECIPE_KEY, mRecipe);
    }
}
