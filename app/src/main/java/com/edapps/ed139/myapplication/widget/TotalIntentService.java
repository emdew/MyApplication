package com.edapps.ed139.myapplication.widget;

import android.app.IntentService;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;

public class TotalIntentService extends IntentService {

    // IntentService can perform, e.g. ACTION_FETCH_NEW_ITEMS
    private static final String ACTION_UPDATE_TOTAL = "com.edapps.ed139.myapplication.action.update_total";
    private static final String EXTRA_PARAM1 = "com.edapps.ed139.myapplication.extra.PARAM1";
    private static Double sPrice;

    public TotalIntentService() {
        super("TotalIntentService");
    }

    /**
     * Starts this service to perform action Foo with the given parameters. If
     * the service is already performing a task this action will be queued.
     *
     * @see IntentService
     */
    public static void startActionUpdateTotal(Context context, Double price) {
        if (price != null){
            sPrice = price;
        }
        Intent intent = new Intent(context, TotalIntentService.class);
        intent.setAction(ACTION_UPDATE_TOTAL);
        intent.putExtra(EXTRA_PARAM1, price);
        context.startService(intent);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            final String action = intent.getAction();
            if (ACTION_UPDATE_TOTAL.equals(action)) {
                final Double price = intent.getDoubleExtra(EXTRA_PARAM1, 0);
                handleActionUpdateTotal(price);
            }
        }
    }

    /**
     * Handle action Foo in the provided background thread with the provided
     * parameters.
     */
    private void handleActionUpdateTotal(Double price) {
        // get the widget manager and update with info from intent
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(this);
        int[] appWidgetsIds = appWidgetManager.getAppWidgetIds(new ComponentName(this, CameraWidget.class));
        CameraWidget.updateAllIngredientWidgets(this, appWidgetManager, appWidgetsIds, sPrice);
    }

}
