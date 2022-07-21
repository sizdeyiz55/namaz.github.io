package com.namazvakitleri.internetsiz.service

import android.app.Service
import android.content.Intent
import android.os.CountDownTimer
import android.os.IBinder
import android.util.Log
import android.widget.Toast

class NotificationService: Service() {

    override fun onBind(p0: Intent?): IBinder? {
        TODO("Not yet implemented")
        Log.e("NotificationService", "onBind")
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        return super.onStartCommand(intent, flags, startId)

        Log.e("NotificationService", "onStartCommand")
    }

    override fun onStart(intent: Intent?, startId: Int) {
        super.onStart(intent, startId)
        Log.e("NotificationService", "onStart")
     //   timer(10000,1000).start()
    }

    override fun onCreate() {
        super.onCreate()
        Log.e("NotificationService", "onCreate")
    }

    private fun timer(millisInFuture: Long, countDownInterval: Long): CountDownTimer {

        return object : CountDownTimer(millisInFuture, countDownInterval) {
            override fun onTick(millisUntilFinished: Long) {
                    Log.e("NotificationTimer","$millisUntilFinished")
                Toast.makeText(applicationContext,"$millisUntilFinished", Toast.LENGTH_SHORT).show()
            }

            override fun onFinish() {
                timer(10000,1000).start()
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.e("NotificationService","onDestroy")
    }
}