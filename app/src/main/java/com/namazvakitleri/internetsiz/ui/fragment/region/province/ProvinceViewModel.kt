package com.namazvakitleri.internetsiz.ui.fragment.region.province

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.google.firebase.firestore.FirebaseFirestore
import com.namazvakitleri.internetsiz.Application
import com.namazvakitleri.internetsiz.modal.Country
import com.namazvakitleri.internetsiz.ui.activity.error.ErrorActivity
import com.namazvakitleri.internetsiz.modal.Province
import com.namazvakitleri.internetsiz.retrofit.ApiService
import com.namazvakitleri.internetsiz.ui.base.BaseViewModel
import com.namazvakitleri.internetsiz.utils.enum.Results
import com.namazvakitleri.internetsiz.utils.extension.enqueue
import com.namazvakitleri.internetsiz.utils.extension.openActivity
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProvinceViewModel @Inject constructor(
    private val provinceRepository: ProvinceRepository,
    private val firestore : FirebaseFirestore,
    private val apiService: ApiService
): BaseViewModel() {

    var provinceData = MutableLiveData<ArrayList<Province>>()

    fun getProvinceFromAPI(countryId: Int) {

        setLoading(true)

        viewModelScope.launch {


            firestore.collection("Cities").document("$countryId").get().addOnCompleteListener { res ->
                val arrayList = ArrayList<Province>()
                if (res.isSuccessful) {
                    val result = res.result

                    if(res.result.exists()) {


                        for (i in result.data!!) {
                            arrayList.add(
                                Province(i.key,null, i.value.toString().toInt())
                            )
                        }
                        provinceData.value = arrayList
                        setLoading(false)
                    }else{
                        val mapCity = HashMap<String, Int>()
                        apiService.getProvince(countryId).enqueue { r1 ->
                            when (r1) {
                                is Results.Success -> {
                                    val provinceDatas = r1.response.body() as ArrayList<Province>?
                                    for (j in provinceDatas!!) {
                                        mapCity.put(j.SehirAdi!!,j.SehirID!!)
                                    }
                                    firestore.collection("Cities").document("$countryId").set(mapCity).addOnCompleteListener {
                                        provinceData.value = provinceDatas!!
                                        setLoading(false)
                                    }
                                }
                            }
                        }
                    }
                }else{
                    setLoading(false)
                }


            /*provinceRepository.getProvinceFromAPI(countryId).collect { serviceResponse ->
                serviceResponse.enqueue { response ->
                    when(response) {
                        is Results.Success -> {
                            provinceData.value = response.response.body() as ArrayList<Province>
                            setLoading(false)
                        }

                        is Results.Failure -> {
                            Application.context.openActivity(ErrorActivity::class.java)
                            setLoading(false)
                        }
                    }
                }*/
            }
        }
    }
}