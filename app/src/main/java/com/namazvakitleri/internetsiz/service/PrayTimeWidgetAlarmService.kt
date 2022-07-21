package com.namazvakitleri.internetsiz.service

import android.content.Context

private const val INTERVAL_MILLIS = 60000
private const val ALARM_ID = 0

class PrayTimeWidgetAlarmService(context: Context) {

    private var context = context

    /*--fun startAlarm() {

        var calendar = Calendar.getInstance()
        calendar.add(Calendar.MILLISECOND, INTERVAL_MILLIS)

        val alarmIntent = Intent(context, PrayTimesWidget::class.java)
        alarmIntent.action = PrayTimesWidget.ACTION_AUTO_UPDATE

        var pendingIntent = PendingIntent.getBroadcast(context, ALARM_ID, alarmIntent, PendingIntent.FLAG_CANCEL_CURRENT)
        var alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        alarmManager.setRepeating(AlarmManager.ELAPSED_REALTIME, SystemClock.elapsedRealtime(), INTERVAL_MILLIS.toLong(), pendingIntent)

    }

    fun stopAlarm() {

        val alarmIntent = Intent(PrayTimesWidget.ACTION_AUTO_UPDATE)
        var pendingIntent = PendingIntent.getBroadcast(context, ALARM_ID, alarmIntent, PendingIntent.FLAG_CANCEL_CURRENT)
        var alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        alarmManager.cancel(pendingIntent)
    }*/
}