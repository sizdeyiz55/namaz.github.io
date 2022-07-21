package com.namazvakitleri.internetsiz.modal.firestore

import com.google.gson.annotations.SerializedName

data class Fields(

    @field:SerializedName("app-version")
    var appVersion: Value? = null
)
