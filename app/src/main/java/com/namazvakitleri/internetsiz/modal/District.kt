package com.namazvakitleri.internetsiz.modal

import com.google.gson.annotations.SerializedName

data class District(
    @SerializedName("IlceAdi")
    var IlceAdi: String? = "",
    @SerializedName("IlceAdiEn")
    var IlceAdiEn: String? = "",
    @SerializedName("IlceID")
    var IlceID: Int? = null
)