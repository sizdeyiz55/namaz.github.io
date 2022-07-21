package com.namazvakitleri.internetsiz.db

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "ReligiousDayNights")
data class ReligiousDayNights (

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "ReligiousDayNightsID")
    var ReligiousDayNightsID: Int = 0,


    @ColumnInfo(name = "Day")
    var Day: String,


    @ColumnInfo(name = "MiladiDateMonth")
    var MiladiDateMonth: String,


    @ColumnInfo(name = "MiladiDateDay")
    var MiladiDateDay: String,


    @ColumnInfo(name = "HicriDate")
    var HicriDate: String,


    @ColumnInfo(name = "Year")
    var Year: Int
)