package com.namazvakitleri.internetsiz.modal

data class CurrentPrayTimeInfo (
    var remainingPrayTime: String,
    var remainingPrayTimeMs: Long,
    var remainingPrayName: String,
    var isAfterYatsi: Boolean? = null
) {
}