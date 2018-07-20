package com.example.surface4pro.bakingtime;

import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViewsService;

import com.example.surface4pro.bakingtime.data.Recipe;

public class BakingTimeWidgetService extends RemoteViewsService {

    public static void updateWidget(Context context, Recipe recipe) {
        // Save the recipe to sharedPreferences
        Preferences.saveRecipe(context, recipe);

        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
        int[] appWidgetIds = appWidgetManager.getAppWidgetIds(new ComponentName(context, BakingTimeWidget.class));
        BakingTimeWidget.updateBakingTimeWidgets(context, appWidgetManager, appWidgetIds);
    }

    /**
     * To be implemented by the derived service to generate appropriate factories for
     * the data.
     */
    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new WidgetListRemoteViewsFactory(this.getApplicationContext());
    }
}