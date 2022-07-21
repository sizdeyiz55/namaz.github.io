package com.namazvakitleri.internetsiz.main

import com.namazvakitleri.internetsiz.db.InsertReligiousDayNightsToDB
import com.namazvakitleri.internetsiz.modal.ReligiousDaysNights
import com.namazvakitleri.internetsiz.modal.firestore.Version
import com.namazvakitleri.internetsiz.retrofit.ApiClient
import com.namazvakitleri.internetsiz.retrofit.ApiService
import com.namazvakitleri.internetsiz.utils.constant.Constant
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.Call
import javax.inject.Inject

class MainRepository @Inject constructor(
    private val religiousDaysNight: InsertReligiousDayNightsToDB
) {

        fun getCurrentAppVersionFromAPI(): Flow<Call<Version>> {

            var api = ApiClient.api(Constant.FIRESTORE_URL)
                .create(ApiService::class.java)


            return flow {
                emit(
                    api.getAppVersion()
                )
            }
        }


    fun getReligiousDayNightsFromAPI() : Flow<Call<List<ReligiousDaysNights>>> {

        var religiousDaysService = ApiClient.api(Constant.RELIGIOUS_DAY_NIGHTS_URL)
            .create(ApiService::class.java)

        return flow {
            emit(
                religiousDaysService.getReligiousDayNights()
            )
        }
    }


    fun setReligiousDayNightsToDB(religiousDaysNights: List<ReligiousDaysNights>): Flow<Boolean> {

        return flow {

            religiousDaysNight.insertData(religiousDaysNights)

            emit(true)
        }
    }
}