package com.namazvakitleri.internetsiz.modal

import com.google.gson.annotations.SerializedName

data class Province(
    @SerializedName("SehirAdi")
    var SehirAdi: String? = "",
    @SerializedName("SehirAdiEn")
    var SehirAdiEn: String? = "",
    @SerializedName("SehirID")
    var SehirID: Int? = null
)