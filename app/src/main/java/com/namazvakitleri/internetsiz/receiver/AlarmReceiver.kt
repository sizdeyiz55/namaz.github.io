package com.namazvakitleri.internetsiz.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import com.namazvakitleri.internetsiz.Application
import com.namazvakitleri.internetsiz.R
import com.namazvakitleri.internetsiz.manager.ReminderPrayTimesManager
import com.namazvakitleri.internetsiz.utils.constant.Constant.SELECT_SOUND_ID
import com.namazvakitleri.internetsiz.utils.extension.get
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AlarmReceiver: BroadcastReceiver() {


    private lateinit var prefs: SharedPreferences
    private lateinit var setNotification: SetNotification

    private lateinit var reminderPrayTimesManager: ReminderPrayTimesManager

    override fun onReceive(context: Context, intent: Intent) {

        //val prayName = intent.action
        //val timeInMillis = intent.getLongExtra(EXTRA_EXACT_ALARM_TIME, 0L)

         prefs = Application.context.getSharedPreferences(
            Application.context.getString(R.string.app_name),
            Context.MODE_PRIVATE)


        val notificationText = intent.getStringExtra("NotificationText")
        val reminderControl = intent.getBooleanExtra("TimeReminder", true)
        val howManyMinuteAgo = intent.getIntExtra("HowManyMinuteAgo", 0)

        if (reminderControl) {
            buildNotification(
                notificationText
            )
        }

        reminderPrayTimesManager = ReminderPrayTimesManager()
        reminderPrayTimesManager.start(howManyMinuteAgo)
    }



    private fun buildNotification(
        prayName: String?
    ) {

        var notificationSound = prefs.get(SELECT_SOUND_ID, R.raw.standart)

        setNotification = SetNotification(Application.context)
        setNotification.setNotification(notificationSound, "$prayName")
    }
}