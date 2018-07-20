package com.example.surface4pro.bakingtime;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.surface4pro.bakingtime.data.Recipe;
import com.google.gson.Gson;

public class Preferences {
    public static final String PREFERENCES_NAME = "preferences";

    public static void saveRecipe(Context context, Recipe recipe) {
        SharedPreferences.Editor prefs = context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE).edit();

        Gson gson = new Gson();
        prefs.putString("widget_recipe_key", gson.toJson(recipe));

        prefs.apply();
    }

    public static Recipe loadRecipe(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE);
        String recipeJSON = prefs.getString("widget_recipe_key", "");

        Gson gson = new Gson();

        return "".equals(recipeJSON) ? null : gson.fromJson(recipeJSON, Recipe.class);
    }
}
