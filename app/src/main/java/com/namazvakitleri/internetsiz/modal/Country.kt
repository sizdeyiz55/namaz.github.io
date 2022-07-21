package com.namazvakitleri.internetsiz.modal

import com.google.gson.annotations.SerializedName

data class Country(
   @SerializedName("UlkeAdi")
    var UlkeAdi: String?="",
    @SerializedName("UlkeAdiEn")
    var UlkeAdiEn: String?="",
    @SerializedName("UlkeID")
    var UlkeID: Int? = null
)