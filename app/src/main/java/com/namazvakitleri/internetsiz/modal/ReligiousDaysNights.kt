package com.namazvakitleri.internetsiz.modal
import com.google.gson.annotations.SerializedName

data class ReligiousDaysNights(
    @SerializedName("gun")
    var Day: String? = null,
    @SerializedName("miladi_tarih_ay")
    var MiladiDateMonth: String? = null,
    @SerializedName("miladi_tarih_gun")
    var MiladiDateDay: String? = null,
    @SerializedName("hicri_tarih")
    var HicriDate: String? = null,
    @SerializedName("yil")
    var Year: Int = 0
) {
}