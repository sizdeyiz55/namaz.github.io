package com.namazvakitleri.internetsiz.modal

import com.namazvakitleri.internetsiz.db.PrayTime

data class PrayTimeValue(
    var calculateTimes: CurrentPrayTimeInfo,
    var prayTimes: PrayTime,
    var fromMillisecondsToTime: String
)
