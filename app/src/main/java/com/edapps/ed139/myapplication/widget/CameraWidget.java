package com.edapps.ed139.myapplication.widget;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.util.Log;
import android.widget.RemoteViews;

import com.edapps.ed139.myapplication.R;

import java.text.DecimalFormat;

/**
 * Implementation of App Widget functionality.
 */
public class CameraWidget extends AppWidgetProvider {

    public static final String LOG_TAG = "UpdateWidget";
    public static Double mPrice;

    // 6th/last
    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager, int appWidgetId, Double price) {

        DecimalFormat format = new DecimalFormat("0.00");

        // Construct the RemoteViews object
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.camera_widget);

        mPrice = price;

        if (price == null){
            // meh
        } else {
            // set the price on the view
            views.setTextViewText(R.id.widget_total, String.valueOf(format.format(price)));
        }

        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    // 5th
    public static void updateAllIngredientWidgets(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds, Double price){
        for (int appWidgetId: appWidgetIds){
            updateAppWidget(context, appWidgetManager, appWidgetId, price);
        }
    }

    // 1st
    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        if (mPrice != null) {
            Log.d(LOG_TAG, "Should update with " + mPrice);
        }
        TotalIntentService.startActionUpdateTotal(context, mPrice);
    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }
}

