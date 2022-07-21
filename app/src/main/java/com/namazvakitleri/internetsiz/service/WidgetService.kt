package com.namazvakitleri.internetsiz.service

import android.app.Notification
import android.app.NotificationManager
import android.app.Service
import android.content.Intent
import android.os.CountDownTimer
import android.os.IBinder
import android.util.Log
import android.widget.RemoteViews
import com.namazvakitleri.internetsiz.modal.CurrentPrayTimeInfo
import com.namazvakitleri.internetsiz.modal.PrayTimes
import java.text.SimpleDateFormat
import java.util.*

private const val timeColor = "#37697C"
private const val originalColor = "#FFFFFF"

class WidgetService : Service() {

    private lateinit var views: RemoteViews

    private val hh_mm_ss = SimpleDateFormat("HH:mm:ss")
    private val am_pm_Format = SimpleDateFormat("h:mm:ss a")
    private var currentDateFormat = SimpleDateFormat("dd.MM.yyyy")
    private lateinit var currentTime: Date

    private lateinit var districtName: String
    private lateinit var provinceName: String
    private lateinit var currentDate: String
    private lateinit var prayTimes: ArrayList<PrayTimes>
    private val countDownInterval: Long = 1000

    private lateinit var calculateTimes: ArrayList<CurrentPrayTimeInfo>
    private lateinit var editedTimes: ArrayList<PrayTimes>
    private var countDownTimer: CountDownTimer? = null


    lateinit var notificationManager: NotificationManager
    lateinit var builder: Notification
    private val description = "Test notification"

    override fun onBind(p0: Intent?): IBinder? {
        TODO("Not yet implemented")
        Log.e("WidgetService", "onBind")

    }

    override fun onCreate() {
        super.onCreate()

        Log.e("WidgetService", "onCreate")

        /*if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val CHANNEL_ID = "Widget"
            notificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            var notificationChannel = NotificationChannel(
                CHANNEL_ID, description, NotificationManager.IMPORTANCE_HIGH
            )
            notificationChannel.enableLights(true)
            notificationChannel.lightColor = Color.GREEN
            notificationChannel.enableVibration(false)
            notificationManager.createNotificationChannel(notificationChannel)

            builder = Notification.Builder(this, CHANNEL_ID)
                .setContentTitle("Namaz Vakti")
                .setContentText("Widget güncel bir şekilde eklendi.").build()
            startForeground(1, builder)
        }*/
    }


    override fun onStart(intent: Intent, startId: Int) {
        super.onStart(intent, startId)
        Log.e("WidgetService", "onStart")
    }

    /*override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {

        if (intent.action == "STOP_SERVICE") {
            stopForeground(true)
            stopSelfResult(startId)
            if (countDownTimer != null) {
                countDownTimer?.cancel()
                countDownTimer = null
            }
            return START_NOT_STICKY
        } else {
            views = RemoteViews(packageName, R.layout.pray_time_widget)
            //sqLiteHandler = SQLiteHandler.getInstance(this)
           // timeSettings()
        }

        return START_STICKY
    }*/


   /* private fun setPrayTimeTextValues(
        prayTimes: ArrayList<PrayTimes>,
        provinceDistrictName: String
    ) {

        views.setTextViewText(R.id.provinceDistrictNameWidgetTxt, "$provinceDistrictName")
        views.setTextViewText(R.id.imsakTimeWidgetTxt, "${prayTimes[0].Imsak}")
        views.setTextViewText(R.id.gunesTimeWidgetTxt, "${prayTimes[0].Gunes}")
        views.setTextViewText(R.id.ogleTimeWidgetTxt, "${prayTimes[0].Ogle}")
        views.setTextViewText(R.id.ikindiTimeWidgetTxt, "${prayTimes[0].Ikindi}")
        views.setTextViewText(R.id.aksamTimeWidgetTxt, "${prayTimes[0].Aksam}")
        views.setTextViewText(R.id.yatsiTimeWidgetTxt, "${prayTimes[0].Yatsi}")

        var componentName = ComponentName(this, PrayTimeWidget::class.java)
        AppWidgetManager.getInstance(this).updateAppWidget(componentName, views)

    }*/
}
