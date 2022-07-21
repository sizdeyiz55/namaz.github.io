package com.namazvakitleri.internetsiz.db

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.namazvakitleri.internetsiz.Application
import com.namazvakitleri.internetsiz.db.dao.PrayTimeDao


@Database(entities = [PrayTime:: class, ReligiousDayNights:: class], version = 1, exportSchema = false)
abstract class CreateDB: RoomDatabase() {


    abstract fun prayTimesDao(): PrayTimeDao


    companion object {

        private lateinit var instance: CreateDB

        fun getDB(): CreateDB {

                instance = Room.databaseBuilder(Application.context,
                CreateDB::class.java, "PrayTime.db")
                    .allowMainThreadQueries()
                    .build()

            return instance
        }
    }
}