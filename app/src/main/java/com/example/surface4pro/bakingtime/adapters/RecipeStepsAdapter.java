package com.example.surface4pro.bakingtime.adapters;

import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.surface4pro.bakingtime.R;
import com.example.surface4pro.bakingtime.data.Ingredient;
import com.example.surface4pro.bakingtime.data.Recipe;
import com.example.surface4pro.bakingtime.data.Step;
import com.example.surface4pro.bakingtime.databinding.RecipeStepIngredientItemBinding;
import com.example.surface4pro.bakingtime.databinding.RecipeStepItemBinding;

import java.math.BigDecimal;
import java.util.Locale;

public class RecipeStepsAdapter extends RecyclerView.Adapter<RecipeStepsAdapter.RecipeStepsViewHolder> {

    // Declare constant IDs for the ViewType for ingredients and for recipe step
    private static final int VIEW_TYPE_INGREDIENTS = 0;
    private static final int VIEW_TYPE_RECIPE_STEP = 1;

    // On-click handler to make it easy for an Activity to interface with the RecyclerView
    private final RecipeStepsAdapterOnClickHandler mClickHandler;

    // Cached copy of the Recipe
    private final Recipe mRecipe;
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
     * Returns an integer code related to the type of View we want the ViewHolder to be at a given
     * position. This method is useful when we want to use different layouts for different items
     * depending on their position.
     *
     * @param position index within our RecyclerView
     * @return the view type (ingredients or recipe step)
     */
    @Override
    public int getItemViewType(int position) {

        // Within getItemViewType, if position is 0, return the ID for the ingredients viewType
        if (position == 0) {
            return VIEW_TYPE_INGREDIENTS;
        } else {
            // Otherwise, return the ID for the recipe step viewType
            return VIEW_TYPE_RECIPE_STEP;
        }
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

        switch (viewType) {

            // If the view type of the layout is ingredients, use ingredients layout
            case VIEW_TYPE_INGREDIENTS:
                RecipeStepIngredientItemBinding ingredientBinding =
                        DataBindingUtil.inflate(
                                mLayoutInflater,
                                R.layout.recipe_step_ingredient_item,
                                viewGroup,
                                false);
                return new RecipeStepsViewHolder(ingredientBinding);

            // If the view type of the layout is recipe step, use recipe step layout
            case VIEW_TYPE_RECIPE_STEP:
                RecipeStepItemBinding stepItemBinding =
                        DataBindingUtil.inflate(
                                mLayoutInflater,
                                R.layout.recipe_step_item,
                                viewGroup,
                                false);
                return new RecipeStepsViewHolder(stepItemBinding);

            // Otherwise, throw an IllegalArgumentException
            default:
                throw new IllegalArgumentException("Invalid view type, value of " + viewType);
        }
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

        int viewType = getItemViewType(position);

        switch (viewType) {

            // If the view type of the layout is ingredients, set ingredients data
            case VIEW_TYPE_INGREDIENTS:
                if (holder.ingredientItemBinding != null) {

                    StringBuilder ingredientsList = new StringBuilder();
                    for (int i = 0; i < mRecipe.getIngredients().size(); i++) {
                        Ingredient ingredient = mRecipe.getIngredients().get(i);
                        BigDecimal quantity = new BigDecimal(ingredient.getQuantity());
                        ingredientsList.append(String.format(Locale.getDefault(), "- %s (%s\u00A0%s)",
                                ingredient.getIngredient(),
                                quantity.stripTrailingZeros().toPlainString(),
                                ingredient.getMeasure()));

                        if (i != mRecipe.getIngredients().size() - 1)
                            ingredientsList.append("\n");
                    }
                    holder.ingredientItemBinding.ingredientsTextView.setText(ingredientsList.toString());

                }
                break;

            // If the view type of the layout is recipe step, set recipe step data
            case VIEW_TYPE_RECIPE_STEP:
                if (holder.binding != null) {
                    holder.binding.stepNumberTextView.setText(String.format("%s.", String.valueOf(position)));
                    holder.binding.recipeStepTextView.setText(mRecipe.getSteps().get(position - 1).getShortDescription());
                }
                break;

            // Otherwise, throw an IllegalArgumentException
            default:
                throw new IllegalArgumentException("Invalid view type, value of " + viewType);
        }
    }

    /**
     * This method returns the number of items to display. It is used behind the scenes
     * to help layout our Views and for animations.
     *
     * @return The number of steps available in the Recipe
     */
    @Override
    public int getItemCount() {
        return mRecipe.getSteps().size() + 1;
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
        private final RecipeStepIngredientItemBinding ingredientItemBinding;

        /**
         * Constructor for our ViewHolder. In here we set our binding instance and the onClickListener.
         *
         * @param itemBinding The RecipeStepItemBinding instance that was instantiated in
         *                    {@link RecipeStepsAdapter#onCreateViewHolder(ViewGroup, int)}
         */
        RecipeStepsViewHolder(final RecipeStepItemBinding itemBinding) {
            super(itemBinding.getRoot());

            this.binding = itemBinding;
            this.ingredientItemBinding = null;
            itemBinding.getRoot().setOnClickListener(this);
        }

        /**
         * Constructor for our ViewHolder. In here we set our binding instance and the onClickListener.
         *
         * @param ingredientItemBinding The RecipeStepItemBinding instance that was instantiated in
         *                              {@link RecipeStepsAdapter#onCreateViewHolder(ViewGroup, int)}
         */
        RecipeStepsViewHolder(final RecipeStepIngredientItemBinding ingredientItemBinding) {
            super(ingredientItemBinding.getRoot());

            this.binding = null;
            this.ingredientItemBinding = ingredientItemBinding;
        }

        /**
         * Called when a view has been clicked.
         *
         * @param v The view that was clicked.
         */
        @Override
        public void onClick(View v) {
            int adapterPosition = getAdapterPosition();
            Step clickedStep = mRecipe.getSteps().get(adapterPosition - 1);
            mClickHandler.onRecipeStepClicked(clickedStep);
        }
    }
}