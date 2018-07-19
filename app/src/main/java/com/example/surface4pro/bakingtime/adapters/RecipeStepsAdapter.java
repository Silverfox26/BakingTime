package com.example.surface4pro.bakingtime.adapters;

import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.surface4pro.bakingtime.R;
import com.example.surface4pro.bakingtime.data.Recipe;
import com.example.surface4pro.bakingtime.data.Step;
import com.example.surface4pro.bakingtime.databinding.RecipeStepItemBinding;

public class RecipeStepsAdapter extends RecyclerView.Adapter<RecipeStepsAdapter.RecipeStepsViewHolder> {

    private static final String TAG = RecipeStepsAdapter.class.getSimpleName();

    // On-click handler to make it easy for an Activity to interface with the RecyclerView
    private final RecipeStepsAdapterOnClickHandler mClickHandler;

    // Cached copy of the Recipe
    private Recipe mRecipe;
    private LayoutInflater mLayoutInflater;

    /**
     * Constructor for RecipeStepsAdapter that accepts a Recipe object to display and the specification
     * for the ClickHandler.
     *
     * @param recipe       A List of Recipe objects
     * @param clickHandler A clickHandler
     */
    public RecipeStepsAdapter(Recipe recipe, RecipeStepsAdapterOnClickHandler clickHandler) {
        mRecipe = recipe;
        mClickHandler = clickHandler;
    }

    /**
     * This gets called when each new ViewHolder is created. This happens when the RecyclerView
     * is laid out. Enough ViewHolders will be created to fill the screen and allow for scrolling.
     *
     * @param viewGroup The ViewGroup that these ViewHolders are contained within.
     * @param viewType  If the RecyclerView has more than one type of item you
     *                  can use this viewType integer to provide a different layout.
     * @return A new RecipeViewHolder that holds the View for each item
     */
    @NonNull
    @Override
    public RecipeStepsViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {

        if (mLayoutInflater == null) {
            mLayoutInflater = LayoutInflater.from(viewGroup.getContext());
        }

        RecipeStepItemBinding binding =
                DataBindingUtil.inflate(mLayoutInflater, R.layout.recipe_step_item, viewGroup, false);

        return new RecipeStepsViewHolder(binding);
    }

    /**
     * OnBindViewHolder is called by the RecyclerView to display the data at the specified
     * position.
     *
     * @param holder   The ViewHolder which should be updated to represent the contents of the
     *                 item at the given position in the data set.
     * @param position The position of the item within the adapter's data set.
     */
    @Override
    public void onBindViewHolder(@NonNull RecipeStepsViewHolder holder, final int position) {
        holder.binding.recipeStepTextView.setText(mRecipe.getSteps().get(position).getShortDescription());
    }

    /**
     * This method returns the number of items to display. It is used behind the scenes
     * to help layout our Views and for animations.
     *
     * @return The number of steps available in the Recipe
     */
    @Override
    public int getItemCount() {
        return mRecipe.getSteps().size();
    }

    /**
     * Interface for the RecipeAdapter ClickHandler
     */
    public interface RecipeStepsAdapterOnClickHandler {
        void onRecipeStepClicked(Step step);
    }

    /**
     * Cache of the children views for a list item.
     */
    class RecipeStepsViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private final RecipeStepItemBinding binding;

        /**
         * Constructor for our ViewHolder. In here we set our binding instance and the onClickListener.
         *
         * @param itemBinding The RecipeStepItemBinding instance that was instantiated in
         *                    {@link RecipeStepsAdapter#onCreateViewHolder(ViewGroup, int)}
         */
        RecipeStepsViewHolder(final RecipeStepItemBinding itemBinding) {
            super(itemBinding.getRoot());
            this.binding = itemBinding;
            itemBinding.getRoot().setOnClickListener(this);
        }

        /**
         * Called when a view has been clicked.
         *
         * @param v The view that was clicked.
         */
        @Override
        public void onClick(View v) {
            int adapterPosition = getAdapterPosition();
            Step clickedStep = mRecipe.getSteps().get(adapterPosition);
            mClickHandler.onRecipeStepClicked(clickedStep);
        }
    }
}
