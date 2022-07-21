package com.namazvakitleri.internetsiz.di

import android.app.AlarmManager
import android.content.Context
import android.content.SharedPreferences
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.namazvakitleri.internetsiz.R
import com.namazvakitleri.internetsiz.db.CreateDB
import com.namazvakitleri.internetsiz.db.dao.PrayTimeDao
import com.namazvakitleri.internetsiz.retrofit.ApiClient
import com.namazvakitleri.internetsiz.retrofit.ApiService
import com.namazvakitleri.internetsiz.utils.constant.Constant
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object Modules {

    @Singleton
    @Provides
    fun sharedPrefs(@ApplicationContext context: Context): SharedPreferences =context.getSharedPreferences(
        context.getString(R.string.app_name),
        Context.MODE_PRIVATE
    )

    @Provides
    fun apiService(): ApiService = ApiClient.api(Constant.API_URL)
        .create(ApiService::class.java)


    @Singleton
    @Provides
    fun createDB(): PrayTimeDao = CreateDB.getDB().prayTimesDao()

    @Provides
    fun alarmManager(@ApplicationContext context: Context): AlarmManager =
        context.getSystemService(Context.ALARM_SERVICE) as AlarmManager


    @Provides
    fun linearLayoutManager(@ApplicationContext context: Context) = LinearLayoutManager(context, RecyclerView.VERTICAL, false)

    @Singleton
    @Provides
    fun providesFirebaseFirestore() = Firebase.firestore

}