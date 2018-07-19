package com.example.surface4pro.bakingtime;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.example.surface4pro.bakingtime.data.Recipe;
import com.example.surface4pro.bakingtime.data.Step;

public class RecipeStepsActivity extends AppCompatActivity implements StepListFragment.OnRecipeStepClickListener {

    private static final String TAG = RecipeStepsActivity.class.getSimpleName();
    StepListFragment mStepListFragment;
    private Recipe mRecipe;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_steps);

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
        }
    }

    // Callback method of the StepListFragment.
    // Gets called, when a recipe step is clicked on in the StepListFragment.
    @Override
    public void onRecipeStepSelected(Step step) {
        Log.d(TAG, "onRecipeStepSelected: " + step.getDescription());
        StepDetailFragment stepDetailFragment = StepDetailFragment.newStepListFragmentInstance(step);

        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.recipe_step_detail_container, stepDetailFragment)
                .commit();
    }
}
