package com.namazvakitleri.internetsiz.widget

import android.appwidget.AppWidgetProvider

/**
 * Implementation of App Widget functionality.
 */
class PrayTimeWidget : AppWidgetProvider() {

    /*companion object {
        val REFRESH_BUTTON = "REFRESH_BUTTON"
    }

    override fun onUpdate(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetIds: IntArray
    ) {

        val views = RemoteViews(context.packageName, R.layout.pray_time_widget)
        var componentName = ComponentName(context, PrayTimeWidget::class.java)


        val intent = Intent(context, SplashActivity::class.java)
        val  pendingIntent = PendingIntent.getActivity(context, 0, intent, 0)

        views.setOnClickPendingIntent(R.id.mainLayoutWidget, pendingIntent)
        views.setOnClickPendingIntent(R.id.refreshWidgetBtn, pendingIntent(context, REFRESH_BUTTON))

        appWidgetManager.updateAppWidget(componentName, views)

    }

    override fun onEnabled(context: Context) {

        //sqLiteHandler = SQLiteHandler.getInstance(context)

        /*if(sqLiteHandler.getPrayTimes().size == 0 ){
            context.showToast("Mübarek lütfen önce uygulamaya giriş yap :)!")
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                context.startForegroundService(Intent(context, WidgetService::class.java))
            } else {
                context.startService(Intent(context, WidgetService::class.java))
            }
        }*/
    }

    override fun onDisabled(context: Context) {
       /* sqLiteHandler = SQLiteHandler.getInstance(context)
        if(sqLiteHandler.getPrayTimes().size != 0) {
            context.stopService(Intent(context, WidgetService::class.java))
        }*/
    }

    override fun onReceive(context: Context, intent: Intent) {
        super.onReceive(context, intent)

        if(intent.action == REFRESH_BUTTON) {

            var intent = Intent(context, WidgetService::class.java)
            intent.action = "STOP_SERVICE"
            context.startService(intent)
            context.stopService(intent)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                context.startForegroundService(Intent(context,WidgetService::class.java))
            } else {
                context.startService(Intent(context,WidgetService::class.java))
            }
        }
    }

    private fun pendingIntent(context: Context, action: String): PendingIntent {

        var intent = Intent(context, PrayTimeWidget::class.java)
        intent.action = action
        return PendingIntent.getBroadcast(context, 0, intent, 0)
    }
}

internal fun updateAppWidget(
    context: Context,
    appWidgetManager: AppWidgetManager,
    appWidgetId: Int
) {
    // Construct the RemoteViews object
    val views = RemoteViews(context.packageName, R.layout.pray_time_widget)
   // views.setTextViewText(R.id.appwidget_text, widgetText)

    // Instruct the widget manager to update the widget
    appWidgetManager.updateAppWidget(appWidgetId, views)
}*/
}