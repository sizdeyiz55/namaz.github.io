package com.namazvakitleri.internetsiz.ui.fragment.region.country

import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import com.namazvakitleri.internetsiz.Application
import com.namazvakitleri.internetsiz.modal.Country
import com.namazvakitleri.internetsiz.modal.PrayTimes
import com.namazvakitleri.internetsiz.retrofit.ApiService
import com.namazvakitleri.internetsiz.ui.activity.error.ErrorActivity
import com.namazvakitleri.internetsiz.utils.enum.Results
import com.namazvakitleri.internetsiz.utils.extension.openActivity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.Call
import java.util.ArrayList
import javax.inject.Inject

class CountryRepository @Inject constructor(
    private val apiService: ApiService,
    private val firestore : FirebaseFirestore
) {

    fun getCountryFromAPI(): Flow<Call<List<Country>>> {

        return flow {
            emit(
                apiService.getCountry()
            )
        }
    }

    fun getCountryFromFirestore():
            Flow<Task<QuerySnapshot>> {
        return flow {
            emit (
                firestore.collection("Countries").get()
            )
        }
    }


}