package com.namazvakitleri.internetsiz.modal.firestore

import com.google.gson.annotations.SerializedName

data class Value(
    @field:SerializedName("stringValue")
    var stringValue: String? = null
)
