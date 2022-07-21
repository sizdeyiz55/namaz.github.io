package com.namazvakitleri.internetsiz.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.namazvakitleri.internetsiz.manager.ReminderPrayTimesManager
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject


@AndroidEntryPoint
class BootAlarmReceiver: BroadcastReceiver() {

    @Inject lateinit var reminderPrayTimesManager: ReminderPrayTimesManager

    override fun onReceive(context: Context, intent: Intent) {

        if (Intent.ACTION_BOOT_COMPLETED == intent.action || intent.action == "android.intent.action.TIME_SET") {


            reminderPrayTimesManager.start(0)
        }
    }
}