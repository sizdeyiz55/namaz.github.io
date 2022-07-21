package com.namazvakitleri.internetsiz.ui.fragment.region.country

import androidx.lifecycle.*
import com.google.firebase.firestore.FieldPath
import com.google.firebase.firestore.FirebaseFirestore
import com.namazvakitleri.internetsiz.modal.Country
import com.namazvakitleri.internetsiz.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class CountryViewModel @Inject constructor(
    private val countryRepository: CountryRepository,
    private val firestore : FirebaseFirestore
): BaseViewModel() {

    var countryData = MutableLiveData<List<Country>>()
      fun getCountryFromAPI() {

          setLoading(true)

          viewModelScope.launch {
              firestore.collection("Countries").get().addOnCompleteListener { res ->
                  val arrayList = ArrayList<Country>()
                  if (res.isSuccessful) {
                      val result = res.result

                      for(i in 0 until res.result.size())   {
                          arrayList.add(Country(result.documents.get(i).get("first").toString(),
                              result.documents.get(i).get("second").toString(),
                              result.documents.get(i).id.toInt()))
                      }
                      countryData.value = arrayList
                      setLoading(false)
                  }


                  /*when(response) {
                      is Results.Success -> {
                          countryData.value = response.response.body() as List<Country>
                          setLoading(false)
                      }

                      is Results.Failure -> {
                          Application.context.openActivity(ErrorActivity::class.java)
                          setLoading(false)
                      }
                  }*/

              }

          }
    }
}