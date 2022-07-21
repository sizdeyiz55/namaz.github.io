package com.namazvakitleri.internetsiz.main

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.namazvakitleri.internetsiz.Application
import com.namazvakitleri.internetsiz.modal.ReligiousDaysNights
import com.namazvakitleri.internetsiz.ui.base.BaseViewModel
import com.namazvakitleri.internetsiz.utils.enum.Results
import com.namazvakitleri.internetsiz.utils.extension.enqueue
import com.namazvakitleri.internetsiz.utils.extension.isConnectedToNetwork
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val mainRepository: MainRepository
): BaseViewModel() {

    var religiousDayNights = MutableLiveData<List<ReligiousDaysNights>>()

    var isVersionCurrent = MutableLiveData<Boolean>()

    fun getAppVersion(appVersion: String) {

        if (Application.context.isConnectedToNetwork()) {

            viewModelScope.launch {
                mainRepository.getCurrentAppVersionFromAPI()?.collect {

                    it.enqueue { response ->
                        when (response) {
                            is Results.Success -> {

                                var currentAppVersion =
                                    response.response.body()?.documents?.first()?.fields?.appVersion?.stringValue

                                isVersionCurrent.value = appVersion != currentAppVersion
                            }

                            is Results.Failure -> {

                            }
                        }
                    }
                }
            }
        }
    }

    fun getReligiousDayNightsFromAPI() {

        setLoading(true)
        viewModelScope.launch {
            mainRepository.getReligiousDayNightsFromAPI().collect { serviceResponse ->

                serviceResponse.enqueue { response ->
                    when (response) {
                        is Results.Success -> {

                            religiousDayNights.value =
                                response.response.body() as List<ReligiousDaysNights>
                        }

                        is Results.Failure -> {
                            setLoading(false)
                        }
                    }
                }
            }
        }
    }

    fun setReligiousDayNightsToDB(
        religiousDayNights: List<ReligiousDaysNights>
    ) {

        viewModelScope.launch {
            mainRepository.setReligiousDayNightsToDB(religiousDayNights).collect {

                setLoading(false)
            }
        }
    }

}