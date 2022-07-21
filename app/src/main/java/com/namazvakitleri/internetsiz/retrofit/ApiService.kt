package com.namazvakitleri.internetsiz.retrofit

import com.namazvakitleri.internetsiz.modal.*
import com.namazvakitleri.internetsiz.modal.firestore.Version
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {

    @GET("ulkeler")
    fun getCountry():  Call<List<Country>>

    @GET("ilceler/{id}")
    fun getDistrict(@Path("id") id: Int): Call<List<District>>

    @GET("sehirler/{id}")
    fun getProvince(@Path("id") id: Int): Call<List<Province>>

    @GET("(default)/documents/current-version")
    fun getAppVersion(): Call<Version>

    @GET("vakitler/{id}")
    fun getPrayTime(@Path("id") id: Int): Call<List<PrayTimes>>

    @GET("master/ReligiousDays.json")
    fun getReligiousDayNights(): Call<List<ReligiousDaysNights>>

}