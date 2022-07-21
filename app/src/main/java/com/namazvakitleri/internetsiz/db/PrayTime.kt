package com.namazvakitleri.internetsiz.db

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "PrayTimes")
data class PrayTime(


    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "PrayTimesID")
    var PrayTimesID: Int = 0,


    @ColumnInfo(name = "Imsak")
    var Imsak: String,


    @ColumnInfo(name = "Gunes")
    var Gunes: String,


    @ColumnInfo(name = "Ogle")
    var Ogle: String,


    @ColumnInfo(name = "Ikindi")
    var Ikindi: String,


    @ColumnInfo(name = "Aksam")
    var Aksam: String,


    @ColumnInfo(name = "Yatsi")
    var Yatsi: String,


    @ColumnInfo(name = "HicriUzun")
    var HicriUzun: String? = null,

    @ColumnInfo(name = "MiladiKisa")
    var MiladiKisa: String? = null,


    @ColumnInfo(name = "MiladiUzun")
    var MiladiUzun: String? = null
)
