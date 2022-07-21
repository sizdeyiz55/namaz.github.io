package com.namazvakitleri.internetsiz.utils.extension

import com.namazvakitleri.internetsiz.utils.enum.Results
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/**
 * inline : Herhangi bir instance oluşturmadan doğrudan yerine getirmesi istenilenleri tamamlar.
 */
inline fun <reified T> Call<T>.enqueue(crossinline result: (Results<T>) -> Unit) {
    enqueue(object: Callback<T> {

        override fun onFailure(call: Call<T>, error: Throwable) {
            result(Results.Failure(call, error))
        }


        override fun onResponse(call: Call<T>, response: Response<T>) {
            result(Results.Success(call, response))
        }
    })
}