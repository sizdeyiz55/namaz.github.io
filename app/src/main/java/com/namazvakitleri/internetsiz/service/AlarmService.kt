package com.namazvakitleri.internetsiz.service

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import com.namazvakitleri.internetsiz.Application.Companion.context
import com.namazvakitleri.internetsiz.receiver.AlarmReceiver
import javax.inject.Inject

class AlarmService @Inject constructor() {



    companion object{
        private lateinit var alarmManager: AlarmManager
        private var alarmService: AlarmService? = null

        fun getInstance(): AlarmService {
            if (alarmService == null) {
                alarmService = AlarmService()

                alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
            }
            return alarmService as AlarmService
        }
    }


    fun setExactAlarm(timeInMillis: Long, notificationText: String, timeReminder: Boolean, howManyMinuteAgo: Int) {

        cancelAlarm()

        setAlarm(timeInMillis, getPendingIntent(getIntent().apply {
           // action = prayName
            //putExtra(Constant.EXTRA_EXACT_ALARM_TIME, timeInMillis)

            putExtra("NotificationText", notificationText)
            putExtra("TimeReminder", timeReminder)
            putExtra("HowManyMinuteAgo", howManyMinuteAgo)
        }))
    }

    private fun getPendingIntent(intent: Intent) =
        PendingIntent.getBroadcast(
            context,
            52,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT
        )


    private fun setAlarm(timeInMillis: Long, pendingIntent: PendingIntent) {


        alarmManager.let {

           if (Build.VERSION.SDK_INT < 23) {
                if (Build.VERSION.SDK_INT >= 19) {
                    if (System.currentTimeMillis() < timeInMillis)
                        alarmManager.setExact(AlarmManager.RTC_WAKEUP, timeInMillis, pendingIntent)
                } else {
                    if(System.currentTimeMillis()< timeInMillis)
                        alarmManager.set(AlarmManager.RTC_WAKEUP, timeInMillis, pendingIntent)
                }
            } else {

               alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, timeInMillis, pendingIntent)
            }
        }
  }

    private fun getIntent() = Intent(context, AlarmReceiver::class.java)

    /*private fun getRandomRequestCode() = RandomUtil.getRandomInt()*/


    private fun cancelAlarm() {

        val pendingIntent =
            PendingIntent.getBroadcast(context, 52, getIntent(),
                PendingIntent.FLAG_NO_CREATE)

        if (pendingIntent != null && alarmManager != null) {

            alarmManager.cancel(pendingIntent)
        }

    }
}