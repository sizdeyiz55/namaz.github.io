package com.namazvakitleri.internetsiz.manager

import android.content.Context
import android.content.SharedPreferences
import com.namazvakitleri.internetsiz.Application
import com.namazvakitleri.internetsiz.Application.Companion.context
import com.namazvakitleri.internetsiz.R
import com.namazvakitleri.internetsiz.db.CreateDB
import com.namazvakitleri.internetsiz.modal.CurrentPrayTimeInfo
import com.namazvakitleri.internetsiz.modal.CurrentTimeDate
import com.namazvakitleri.internetsiz.receiver.SetNotification
import com.namazvakitleri.internetsiz.service.AlarmService
import com.namazvakitleri.internetsiz.transactions.CurrentTime
import com.namazvakitleri.internetsiz.transactions.PrayTimeTransactions
import com.namazvakitleri.internetsiz.utils.constant.Constant
import com.namazvakitleri.internetsiz.utils.constant.Constant.AKSAM
import com.namazvakitleri.internetsiz.utils.constant.Constant.AKSAM_MINUTE_AGO_VALUE
import com.namazvakitleri.internetsiz.utils.constant.Constant.AKSAM_REMINDER
import com.namazvakitleri.internetsiz.utils.constant.Constant.FIFTEEN_MS
import com.namazvakitleri.internetsiz.utils.constant.Constant.FORTY_FIVE_MS
import com.namazvakitleri.internetsiz.utils.constant.Constant.GUNES
import com.namazvakitleri.internetsiz.utils.constant.Constant.GUNES_MINUTE_AGO_VALUE
import com.namazvakitleri.internetsiz.utils.constant.Constant.GUNES_REMINDER
import com.namazvakitleri.internetsiz.utils.constant.Constant.IKINDI
import com.namazvakitleri.internetsiz.utils.constant.Constant.IKINDI_MINUTE_AGO_VALUE
import com.namazvakitleri.internetsiz.utils.constant.Constant.IKINDI_REMINDER
import com.namazvakitleri.internetsiz.utils.constant.Constant.IMSAK
import com.namazvakitleri.internetsiz.utils.constant.Constant.IMSAK_MINUTE_AGO_VALUE
import com.namazvakitleri.internetsiz.utils.constant.Constant.IMSAK_REMINDER
import com.namazvakitleri.internetsiz.utils.constant.Constant.OGLE
import com.namazvakitleri.internetsiz.utils.constant.Constant.OGLE_MINUTE_AGO_VALUE
import com.namazvakitleri.internetsiz.utils.constant.Constant.OGLE_REMINDER
import com.namazvakitleri.internetsiz.utils.constant.Constant.THIRTY_MS
import com.namazvakitleri.internetsiz.utils.constant.Constant.YATSI_MINUTE_AGO_VALUE
import com.namazvakitleri.internetsiz.utils.constant.Constant.YATSI_REMINDER
import com.namazvakitleri.internetsiz.utils.extension.get
import com.namazvakitleri.internetsiz.utils.extension.showToast
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class ReminderPrayTimesManager @Inject constructor(){

    private val hh_mm_ss = SimpleDateFormat("HH:mm:ss")
    private val dateFormat = SimpleDateFormat("dd.MM.yyyy")
    private val setNotification = SetNotification(Application.context)
    private lateinit var alarmService: AlarmService
    private lateinit var prefs: SharedPreferences

    fun start(howManyMinuteAgo: Int) {

        prefs = context.getSharedPreferences(
            context.getString(R.string.app_name),
            Context.MODE_PRIVATE)

        alarmService = AlarmService.getInstance()
        val currentTime = CurrentTime.getCurrentTime()

        var room = CreateDB.getDB()?.prayTimesDao()
        var prayTimes = room?.getPrayTime(currentTime.date)

        val editedTimes = PrayTimeTransactions.editTimes(prayTimes!!)
        val currentPrayTime = PrayTimeTransactions.calculatePrayTime(editedTimes, hh_mm_ss.format(currentTime.time), howManyMinuteAgo)

        createAlarm(currentPrayTime, currentTime)
    }

    private fun createAlarm (currentPrayTime: CurrentPrayTimeInfo, currentTimeAndDate: CurrentTimeDate) {

        if (currentPrayTime.remainingPrayName == IMSAK) {

            val imsakTime = currentPrayTime.remainingPrayTime.toDouble()

            val currentTime =
                hh_mm_ss.format(currentTimeAndDate.time).replace(":", ".").substring(0, 5).toDouble()

            if (imsakTime > currentTime) { // Saat 00.00' ı geçtiyse

                setAlarmServiceForToday(currentPrayTime, currentTimeAndDate)
            } else { // Yatsi vakti girdi. Yani alarm yarinki sabah namazı icin kurulmalı.

                setAlarmServiceForTomorrow()
            }
        } else {

            setAlarmServiceForToday(currentPrayTime, currentTimeAndDate)
        }

    }


    private fun setAlarmServiceForTomorrow() {

        val calendar = Calendar.getInstance()
        calendar.add(Calendar.DAY_OF_YEAR, 1)


        val tomorrowTime = calendar.time
        val tomorrowDate = dateFormat.format(tomorrowTime)


        var room = CreateDB.getDB()?.prayTimesDao()
        var prayTimes = room?.getPrayTime(tomorrowDate)


        if (prayTimes == null) {

            var notificationSound = prefs.get(Constant.SELECT_SOUND_ID, R.raw.standart)
            SetNotification(context).setNotification(notificationSound, Constant.FINISH_PRAY_TIME)
        } else {

            val currentTime = CurrentTime.getCurrentTime()
            val editedPrayTimes = PrayTimeTransactions.editTimes(prayTimes)

            val imsakMilliSecond = PrayTimeTransactions.calculateTimeToMs(editedPrayTimes.Imsak) + TimeUnit.DAYS.toMillis(1) // 1 gün sonraki İmsak vaktinin MS'sini al.

            val imsakPrayTimeInfo = CurrentPrayTimeInfo(editedPrayTimes.Imsak, imsakMilliSecond, "İmsak", true)
            createAlarm(imsakMilliSecond, imsakPrayTimeInfo, currentTime)
        }
    }


    private fun setAlarmServiceForToday(calculateTimes: CurrentPrayTimeInfo, currentTime: CurrentTimeDate) {

        val prayRemainingTimeMilliSecond = PrayTimeTransactions.calculateTimeToMs(calculateTimes.remainingPrayTime)

        createAlarm(prayRemainingTimeMilliSecond, calculateTimes, currentTime)
    }


    private fun createAlarm(prayRemainingTimeMilliSecond: Long, calculateTimes: CurrentPrayTimeInfo, currentTime: CurrentTimeDate) {


        val currentTimeMilliSecond = PrayTimeTransactions.calculateTimeToMs(hh_mm_ss.format(currentTime.time))

        val howManyMinuteAgoValue = howManyMinuteAgoValueRemainingTime(calculateTimes.remainingPrayName)
        val howManyMinuteAgo = howManyMinuteAgo(howManyMinuteAgoValue)

        val alarmTimeMilliSecond = prayRemainingTimeMilliSecond - howManyMinuteAgo
        val hasTheTimePassed = hasTheTimePassed(alarmTimeMilliSecond, currentTimeMilliSecond)
        val remainingTimeNotifyText = remainingTimeNotifyText(howManyMinuteAgoValue)

        val timeReminder = timeReminder(calculateTimes.remainingPrayName)

        if (hasTheTimePassed) {
            context.showToast("${calculateTimes.remainingPrayName} vaktine, ayarlamış olduğunuz vakitten daha az bir zaman kaldığı ilk hatırlatma vaktinde yapılacaktır.")
            alarmService.setExactAlarm(prayRemainingTimeMilliSecond, "${calculateTimes.remainingPrayName} vakti girdi.", timeReminder, howManyMinuteAgoValue)
        } else {
            alarmService.setExactAlarm(alarmTimeMilliSecond, "${calculateTimes.remainingPrayName} $remainingTimeNotifyText", timeReminder, howManyMinuteAgoValue)

        }
    }

    private fun hasTheTimePassed(alarmTimeMilliSecond: Long, currentTimeMilliSecond: Long) = currentTimeMilliSecond > alarmTimeMilliSecond


    private fun howManyMinuteAgoValueRemainingTime(remainingTimeName: String): Int {

        return when(remainingTimeName) {

            IMSAK -> prefs.get(IMSAK_MINUTE_AGO_VALUE, 0)
            GUNES -> prefs.get(GUNES_MINUTE_AGO_VALUE, 0)
            OGLE -> prefs.get(OGLE_MINUTE_AGO_VALUE, 0)
            IKINDI -> prefs.get(IKINDI_MINUTE_AGO_VALUE, 0)
            AKSAM -> prefs.get(AKSAM_MINUTE_AGO_VALUE, 0)
            else -> prefs.get(YATSI_MINUTE_AGO_VALUE ,0)
        }
    }


    private fun howManyMinuteAgo(remainingTimeControl: Int): Int {

        return when(remainingTimeControl) {
            0 -> 0
            1 -> FIFTEEN_MS
            2 -> THIRTY_MS
            else ->
                return FORTY_FIVE_MS
        }
    }


    private fun remainingTimeNotifyText(howManyMinuteAgoValue: Int): String {

        return when(howManyMinuteAgoValue) {

            0 -> "vakti girdi."
            1 -> "vaktine 15 dakika kaldı."
            2 -> "vaktine 30 dakika kaldı."
            else -> "vaktine 45 dakika kaldı."
        }
    }

    private fun timeReminder(remainingTime: String): Boolean {

        return when (remainingTime) {

            IMSAK -> prefs.get(
                IMSAK_REMINDER,
                true
            )

            GUNES -> prefs.get(
                GUNES_REMINDER,
                true
            )

            OGLE -> prefs.get(
                OGLE_REMINDER,
                true
            )

            IKINDI -> prefs.get(
                IKINDI_REMINDER,
                true
            )

            AKSAM -> prefs.get(
                AKSAM_REMINDER,
                true
            )

            else -> prefs.get(
                YATSI_REMINDER,
                true
            )
        }
    }
}













