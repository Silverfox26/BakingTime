package com.example.surface4pro.bakingtime;

import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViewsService;

import com.example.surface4pro.bakingtime.data.Recipe;

public class BakingTimeWidgetService extends RemoteViewsService {

    public static void updateWidget(Context context, Recipe recipe) {
        Preferences.saveRecipe(context, recipe);

        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
        int[] appWidgetIds = appWidgetManager.getAppWidgetIds(new ComponentName(context, BakingTimeWidget.class));
        BakingTimeWidget.updateBakingTimeWidgets(context, appWidgetManager, appWidgetIds);
    }

    /**
     * To be implemented by the derived service to generate appropriate factories for
     * the data.
     *
     * @param intent
     */
    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID);

        return new WidgetListRemoteViewsFactory(this.getApplicationContext());
    }
}
