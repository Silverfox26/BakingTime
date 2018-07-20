package com.example.surface4pro.bakingtime.adapters;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.surface4pro.bakingtime.R;
import com.example.surface4pro.bakingtime.data.Recipe;
import com.example.surface4pro.bakingtime.databinding.RecipeItemBinding;
import com.example.surface4pro.bakingtime.utilities.GlideApp;

import java.util.List;

/**
 * Adapter class for a list of recipe objects
 */
public class RecipeAdapter extends RecyclerView.Adapter<RecipeAdapter.RecipeViewHolder> {

    private final Context mContext;

    // Cached copy of Recipes
    private List<Recipe> mRecipeList;
    private LayoutInflater mLayoutInflater;

    // On-click handler to make it easy for an Activity to interface with the RecyclerView
    private final RecipeAdapterOnClickHandler mClickHandler;

    /**
     * Constructor for RecipeAdapter that accepts a List of Recipes to display and the specification
     * for the ClickHandler.
     *
     * @param recipes      A List of Recipe objects
     * @param clickHandler A clickHandler
     */
    public RecipeAdapter(Context context, List<Recipe> recipes, RecipeAdapterOnClickHandler clickHandler) {
        this.mContext = context;
        mRecipeList = recipes;
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
    public RecipeViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {

        if (mLayoutInflater == null) {
            mLayoutInflater = LayoutInflater.from(viewGroup.getContext());
        }

        RecipeItemBinding binding =
                DataBindingUtil.inflate(mLayoutInflater, R.layout.recipe_item, viewGroup, false);

        return new RecipeViewHolder(binding);
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
    public void onBindViewHolder(@NonNull RecipeViewHolder holder, final int position) {
        holder.binding.recipeNameTextView.setText(mRecipeList.get(position).getName());
        holder.binding.recipeServingSizeTextView.setText(String.format(
                mContext.getString(R.string.servings_count),
                mRecipeList.get(position).getServings().toString()));

        // Load recipe image, if one is available
        String recipeImage = mRecipeList.get(position).getImage();
        if (!recipeImage.isEmpty()) {
            GlideApp.with(mContext)
                    .load(recipeImage)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .placeholder(R.drawable.ic_cupcake)
                    .into(holder.binding.recipeImageView);
        }
    }

    /**
     * This method returns the number of items to display. It is used behind the scenes
     * to help layout our Views and for animations.
     *
     * @return The number of items available in the RecipeList
     */
    @Override
    public int getItemCount() {
        return mRecipeList.size();
    }

    /**
     * Method is used to set the Recipe data on a RecipeAdapter if one is already created.
     * This way new data can be loaded from the web and displayed without the need for
     * a new RecipeAdapter.
     *
     * @param recipeData The new recipe data to be displayed.
     */
    public void setRecipeData(List<Recipe> recipeData) {
        mRecipeList = recipeData;
        notifyDataSetChanged();
    }

    /**
     * Interface for the RecipeAdapter ClickHandler
     */
    public interface RecipeAdapterOnClickHandler {
        void onRecipeClicked(Recipe recipe);
    }

    /**
     * Cache of the children views for a list item.
     */
    class RecipeViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private final RecipeItemBinding binding;

        /**
         * Constructor for our ViewHolder. In here we set our binding instance and the onClickListener.
         *
         * @param itemBinding The RecipeItemBinding instance that was instantiated in
         *                    {@link RecipeAdapter#onCreateViewHolder(ViewGroup, int)}
         */
        RecipeViewHolder(final RecipeItemBinding itemBinding) {
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
            Recipe clickedRecipe = mRecipeList.get(adapterPosition);
            mClickHandler.onRecipeClicked(clickedRecipe);
        }
    }
}