package com.namazvakitleri.internetsiz.ui.fragment.region.province

import com.namazvakitleri.internetsiz.modal.Province
import com.namazvakitleri.internetsiz.retrofit.ApiService
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.Call
import javax.inject.Inject

class ProvinceRepository @Inject constructor(
    private val apiService: ApiService
) {

    fun getProvinceFromAPI(provinceId: Int) : Flow<Call<List<Province>>> {

        return flow {
            emit(
                apiService.getProvince(provinceId)
            )
        }

    }
}