package com.edapps.ed139.myapplication.widget;

import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.edapps.ed139.myapplication.database.AppDatabase;

public class WidgetRemoteViewService extends RemoteViewsService {

    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new WidgetDataProvider(this, intent);
    }

    class WidgetDataProvider implements RemoteViewsService.RemoteViewsFactory {

        AppDatabase mDb;
        Double total;
        Context mContext;

        public WidgetDataProvider(Context context, Intent intent) {
            mContext = context;
        }

        @Override
        public void onCreate(){}

        @Override
        public void onDataSetChanged(){}

        @Override
        public void onDestroy(){}

        @Override
        public int getCount(){
            return 0;
        }

        @Override
        public RemoteViews getViewAt(int position) {
            return null;
        }

        @Override
        public RemoteViews getLoadingView() {
            return null;
        }

        @Override
        public int getViewTypeCount() {
            return 1;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public boolean hasStableIds() {
            return true;
        }
    }

}
