package com.namazvakitleri.internetsiz.service

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.media.RingtoneManager
import android.text.TextUtils
import androidx.core.app.NotificationCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.namazvakitleri.internetsiz.R
import com.namazvakitleri.internetsiz.main.MainActivity
import java.io.InputStream
import java.net.HttpURLConnection
import java.net.URL

private const val TITLE = "Namaz Vakti"

class FirebaseMessagingService: FirebaseMessagingService() {

    private lateinit var bgLargeIcon: Bitmap
    private lateinit var image: Bitmap
    private lateinit var connection: HttpURLConnection
    private lateinit var inputStream: InputStream
    private lateinit var notificationManager: NotificationManager

    override fun onMessageReceived(remoteMessage: RemoteMessage) {

        if (remoteMessage.notification != null) {
            var title = remoteMessage.notification?.title
            var body = remoteMessage.notification?.body
            var imageHost = remoteMessage.notification?.imageUrl?.host
            var imagePath = remoteMessage.notification?.imageUrl?.path
            var imageUrl = imageHost + imagePath

            displayNotification(title, body, imageUrl)
        }
    }

    private fun displayNotification(title: String?, body: String?, imageUrl: String) {

        bgLargeIcon = (resources.getDrawable(R.mipmap.icon_application) as BitmapDrawable).bitmap

        var intent = Intent(applicationContext, MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        var pendingIntent = PendingIntent.getActivity(
            applicationContext,
            0,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT
        )

        var defaultUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        var notificationBuilder = NotificationCompat.Builder(this)
            .setSmallIcon(R.mipmap.icon_application)
            .setLargeIcon(bgLargeIcon)
            .setContentText(body)
            .setAutoCancel(true)
            .setSound(defaultUri)
            .setContentIntent(pendingIntent)

        if (!TextUtils.isEmpty(title)) {
            notificationBuilder.setContentTitle(title)
        } else {
            notificationBuilder.setContentTitle(TITLE)
        }

        if (!TextUtils.isEmpty(imageUrl)) {
            image = getBitmapFromUrl(imageUrl)

            var style = NotificationCompat.BigPictureStyle().bigPicture(image)
            notificationBuilder.setLargeIcon(image)
            style.setSummaryText(body)
            notificationBuilder.setStyle(style)
        }

           if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        }

        notificationManager.notify(1, notificationBuilder.build());
    }

    private fun getBitmapFromUrl(imageUrl: String): Bitmap {

        var url = URL(imageUrl)
        connection = url.openConnection() as HttpURLConnection
        connection.doInput = true
        connection.connect()
        inputStream = connection.inputStream

        return BitmapFactory.decodeStream(inputStream)
    }
}