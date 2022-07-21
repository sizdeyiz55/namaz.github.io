package com.namazvakitleri.internetsiz.utils.enum

import retrofit2.Call
import retrofit2.Response

sealed class Results<T> {
    data class Success<T>(val call: Call<T>, val response: Response<T>): Results<T>()
    data class Failure<T>(val call: Call<T>, val error: Throwable): Results<T>()
}
