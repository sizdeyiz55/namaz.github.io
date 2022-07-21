package com.namazvakitleri.internetsiz.receiver

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.ContentResolver
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.media.AudioAttributes
import android.net.Uri
import android.os.Build
import com.namazvakitleri.internetsiz.R
import com.namazvakitleri.internetsiz.ui.activity.splash.SplashActivity

class SetNotification(var context: Context) {

    private val intent = Intent(context, SplashActivity::class.java).apply {
        flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
    }
    private val pendingIntent: PendingIntent = PendingIntent.getActivity(context, 0, intent,
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.S)PendingIntent.FLAG_IMMUTABLE else 0)
    private val channelId = "i.apps.notifications"
    private val description = "Namaz Vakti"

    fun setNotification(whichSound: Int, whichPray: String?) {

        var notificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        var notificationBuilder : Notification.Builder

        val soundUri: Uri =
            Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE + "://" + context.applicationContext.packageName + "/" + whichSound)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
           var notificationChannel = NotificationChannel(
                channelId, description, NotificationManager.IMPORTANCE_HIGH)
            notificationChannel.enableLights(true)
            notificationChannel.lightColor = Color.GREEN
            notificationChannel.enableVibration(true)
            val audioAttributes = AudioAttributes.Builder()
                .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                .setUsage(AudioAttributes.USAGE_NOTIFICATION)
                .build()
            notificationChannel.setSound(soundUri, audioAttributes)
            notificationManager.createNotificationChannel(notificationChannel)
            notificationBuilder = Notification.Builder(context, channelId)
                .setContentTitle("Namaz Vakti")
                .setContentText(whichPray)
                .setSmallIcon(R.drawable.icon_app)
                .setContentIntent(pendingIntent)
        }else{
            notificationBuilder = Notification.Builder(context)
                .setContentTitle("Namaz Vakti")
                .setContentText(whichPray)
                .setSmallIcon(R.drawable.icon_app)
                .setSound(soundUri)
                .setContentIntent(pendingIntent)
        }

         notificationManager.notify(1, notificationBuilder.build())
        }
    }
