package com.wys.commonbaselib.music;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

import com.wys.commonbaselib.R;

/**
 * @author wangyasheng
 * @date 2020-06-05
 * @Describe:桌面小挂件
 */
public class MusicWidget extends AppWidgetProvider {
    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        RemoteViews remoteViews = new RemoteViews(context.getPackageName(),
                R.layout.music_widget);
        remoteViews.setTextViewText(R.id.tv_title,"this is MusicWidget");
        ComponentName componentName = new ComponentName(context,MusicWidget.class);
        appWidgetManager.updateAppWidget(componentName,remoteViews);
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);
    }
    //    private void pushUpdate(){}
}
