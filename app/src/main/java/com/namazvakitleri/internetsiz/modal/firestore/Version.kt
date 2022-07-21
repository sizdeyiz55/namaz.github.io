package com.namazvakitleri.internetsiz.modal.firestore

import com.google.gson.annotations.SerializedName

data class Version(

    @field:SerializedName("documents")
    val documents: List<DocumentsItem?>? = null
)
