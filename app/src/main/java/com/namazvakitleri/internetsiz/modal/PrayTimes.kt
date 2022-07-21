package com.namazvakitleri.internetsiz.modal

import com.google.gson.annotations.SerializedName

data class PrayTimes(
    @SerializedName("Imsak")
    var Imsak: String? = null,
    @SerializedName("Gunes")
    var Gunes: String? = null,
    @SerializedName("Ogle")
    var Ogle: String? = null,
    @SerializedName("Ikindi")
    var Ikindi: String? = null,
    @SerializedName("Aksam")
    var Aksam: String? = null,
    @SerializedName("Yatsi")
    var Yatsi: String? = null,
    @SerializedName("HicriTarihUzun")
    var HicriTarihUzun: String? = null,
    @SerializedName("MiladiTarihKisa")
    var MiladiTarihKisa: String? = null,
    @SerializedName("MiladiTarihUzun")
    var MiladiTarihUzun: String? = null
    )