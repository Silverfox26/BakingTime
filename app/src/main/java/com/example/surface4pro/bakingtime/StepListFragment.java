package com.example.surface4pro.bakingtime;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.surface4pro.bakingtime.adapters.RecipeStepsAdapter;
import com.example.surface4pro.bakingtime.data.Recipe;
import com.example.surface4pro.bakingtime.data.Step;
import com.example.surface4pro.bakingtime.databinding.FragmentStepListBinding;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link StepListFragment.OnRecipeStepClickListener} interface
 * to handle interaction events.
 * Use the {@link StepListFragment#newStepListFragmentInstance} factory method to
 * create an instance of this fragment.
 */
public class StepListFragment extends Fragment implements RecipeStepsAdapter.RecipeStepsAdapterOnClickHandler {

    private static final String ARG_RECIPE = "recipe";
    private static final String RECIPE_KEY = "recipe_instance";

    private Recipe mRecipe;

    private OnRecipeStepClickListener mListener;

    public StepListFragment() {
        // Required empty public constructor
    }

    /**
     * This factory method creates a new instance of
     * this fragment using the provided parameters.
     *
     * @param recipe A Recipe object that the Fragment should display in its RecyclerView.
     * @return A new instance of fragment StepListFragment.
     */
    public static StepListFragment newStepListFragmentInstance(Recipe recipe) {
        StepListFragment stepListFragment = new StepListFragment();
        Bundle args = new Bundle();
        args.putParcelable(ARG_RECIPE, recipe);
        stepListFragment.setArguments(args);
        return stepListFragment;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        FragmentStepListBinding mBinding = DataBindingUtil.inflate(
                inflater, R.layout.fragment_step_list, container, false);

        // If the recipe was saved after a configuration change, retrieve it now.
        if (savedInstanceState != null && savedInstanceState.containsKey(RECIPE_KEY)) {
            mRecipe = savedInstanceState.getParcelable(RECIPE_KEY);
        }

        // Check if Arguments were passed in during creation of the Fragment.
        // If yes, setup the RecyclerView with the passed in data.
        // Else show error message.
        if (getArguments() != null) {
            mRecipe = getArguments().getParcelable(ARG_RECIPE);

            RecyclerView mRecyclerView = mBinding.recipeStepsRecyclerView;
            mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
            mRecyclerView.setItemAnimator(new DefaultItemAnimator());
            RecipeStepsAdapter mAdapter = new RecipeStepsAdapter(mRecipe, this);
            mRecyclerView.setAdapter(mAdapter);
        } else {
            // TODO add error message TextView
        }

        return mBinding.getRoot();
    }

    /**
     * Override onAttach to make sure that the container activity has implemented the callback
     */
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        // This makes sure that the host activity has implemented the callback interface
        // If not, it throws an exception
        if (context instanceof OnRecipeStepClickListener) {
            mListener = (OnRecipeStepClickListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnRecipeStepClickListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * Callback method of the RecipeStepsAdapter. Gets called when a recipe step was clicked on.
     * Executes the onRecipeStepSelected callback method.
     *
     * @param step The recipe step that was clicked on.
     */
    @Override
    public void onRecipeStepClicked(Step step) {
        if (mListener != null) {
            mListener.onRecipeStepSelected(step);
        }
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     */
    public interface OnRecipeStepClickListener {
        void onRecipeStepSelected(Step step);
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(RECIPE_KEY, mRecipe);
    }
}
