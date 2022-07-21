package com.namazvakitleri.internetsiz.utils.extension

import android.content.Context
import android.content.Intent
import android.os.Bundle

fun <T> Context.openActivity(it: Class<T>, extras: Bundle.() -> Unit = {}) {
    val intent = Intent(this, it)
    intent.putExtras(Bundle().apply(extras))
    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
    startActivity(intent)
}