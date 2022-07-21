package com.namazvakitleri.internetsiz.ui.fragment.region.district

import com.namazvakitleri.internetsiz.db.InsertPrayTimeToDB
import com.namazvakitleri.internetsiz.modal.District
import com.namazvakitleri.internetsiz.modal.PrayTimes
import com.namazvakitleri.internetsiz.retrofit.ApiService
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.Call
import javax.inject.Inject

class DistrictRepository @Inject constructor(
    private val apiService: ApiService,
    private val insertPrayTimeToDB: InsertPrayTimeToDB
){

    fun getDistrictFromAPI(provinceId: Int) : Flow<Call<List<District>>> {


        return flow {
            emit(
                apiService.getDistrict(provinceId)
            )
        }
    }


    fun getPrayTimesFromAPI(districtId: Int): Flow<Call<List<PrayTimes>>>{

        return flow {
            emit(
                apiService.getPrayTime(districtId)
            )
        }
    }


    fun setPrayTimes(prayTimes: ArrayList<PrayTimes>): Flow<Boolean> {

        return flow {

            insertPrayTimeToDB.insertData(prayTimes)

            emit(true)
        }
    }
}