package com.namazvakitleri.internetsiz.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.namazvakitleri.internetsiz.db.PrayTime
import com.namazvakitleri.internetsiz.db.ReligiousDayNights


@Dao
interface PrayTimeDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertPrayTimes(prayTimes: PrayTime)

    @Query("SELECT * FROM PrayTimes WHERE MiladiKisa IN (:miladiDate)")
    fun getPrayTime(miladiDate: String): PrayTime

    @Query("DELETE FROM PrayTimes")
    fun deletePrayTime()

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertReligiousDayNights(religiousDayNights: ReligiousDayNights)

    @Query("SELECT * FROM ReligiousDayNights WHERE Year IN (:year)")
    fun getReligiousDayNights(year: Int): List<ReligiousDayNights>


    @Query("SELECT COUNT(Imsak) FROM PrayTimes")
    fun getPrayTimeItemCount(): Int
}