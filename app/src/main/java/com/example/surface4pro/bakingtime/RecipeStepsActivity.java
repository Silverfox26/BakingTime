package com.example.surface4pro.bakingtime;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.surface4pro.bakingtime.data.Recipe;
import com.example.surface4pro.bakingtime.data.Step;

public class RecipeStepsActivity extends AppCompatActivity implements StepListFragment.OnRecipeStepClickListener {

    private static final String RECIPE_KEY = "recipe_instance";

    private Recipe mRecipe;

    // A single-pane display refers to phone screens, and two-pane to larger tablet screens
    private boolean mTwoPane;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_steps);

        // Get a support ActionBar corresponding to this toolbar
        ActionBar ab = getSupportActionBar();

        // Enable the Up button
        if (ab != null) {
            ab.setDisplayHomeAsUpEnabled(true);
        }

        // Check if we are in two pane mode
        mTwoPane = getResources().getBoolean(R.bool.isTablet);

        // If the activity is started for the first time get the intent and
        // add the StepListFragment.
        if (savedInstanceState == null) {
            if (getIntent() != null && getIntent().hasExtra("recipe")) {
                Intent receivedIntent = getIntent();
                mRecipe = receivedIntent.getParcelableExtra("recipe");

                // Create a new StepListFragment instance
                StepListFragment mStepListFragment = StepListFragment.newStepListFragmentInstance(mRecipe);

                // Use a FragmentManager and transaction to add the fragment to the screen
                FragmentManager fragmentManager = getSupportFragmentManager();
                fragmentManager.beginTransaction()
                        .add(R.id.recipe_steps_container, mStepListFragment)
                        .commit();
            }
        } else {
            // After configuration change get the recipe from the savedInstanceState
            mRecipe = savedInstanceState.getParcelable(RECIPE_KEY);
        }

        if (mRecipe != null) {
            setTitle(mRecipe.getName());
        }
    }

    // Callback method of the StepListFragment.
    // Gets called, when a recipe step is clicked on in the StepListFragment.
    @Override
    public void onRecipeStepSelected(Step step) {
        StepDetailFragment stepDetailFragment = StepDetailFragment.newStepDetailFragmentInstance(step);

        FragmentManager fragmentManager = getSupportFragmentManager();

        if (mTwoPane) {
            // Add/Replace the StepDetailFragment to the right container of the MasterDetail layout
            fragmentManager.beginTransaction()
                    .replace(R.id.recipe_step_detail_container, stepDetailFragment)
                    .commit();
        } else {
            // Replace the current RecipeListFragment with the StepDetailFragment
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
            // Update the widget with the currently selected recipe's ingredient list
            BakingTimeWidgetService.updateWidget(this, mRecipe);
            Toast.makeText(this, String.format(getString(R.string.added_to_widget), mRecipe.getName()), Toast.LENGTH_SHORT).show();
            return true;
        } else
            return super.onOptionsItemSelected(item);
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(RECIPE_KEY, mRecipe);
    }

    @Override
    public boolean onSupportNavigateUp() {
        int count = getSupportFragmentManager().getBackStackEntryCount();
        if (count == 0) {
            super.onSupportNavigateUp();
        } else {
            getSupportFragmentManager().popBackStack();
        }
        return true;
    }
}
