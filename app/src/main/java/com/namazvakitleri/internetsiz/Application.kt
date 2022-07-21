package com.namazvakitleri.internetsiz

import android.app.Application
import android.content.Context
import android.view.LayoutInflater
import dagger.hilt.android.HiltAndroidApp


@HiltAndroidApp
open class Application: Application() {

    companion object {
        lateinit var context: Context
        lateinit var layoutInflater: LayoutInflater
    }
    override fun onCreate() {
        super.onCreate()

        context = applicationContext
        layoutInflater = LayoutInflater.from(context)
    }
}