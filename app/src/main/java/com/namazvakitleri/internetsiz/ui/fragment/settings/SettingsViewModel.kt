package com.namazvakitleri.internetsiz.ui.fragment.settings

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.namazvakitleri.internetsiz.modal.ReligiousDaysNights
import com.namazvakitleri.internetsiz.ui.base.BaseViewModel
import com.namazvakitleri.internetsiz.utils.enum.Results
import com.namazvakitleri.internetsiz.utils.extension.enqueue
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private var settingsRepository: SettingsRepository
): BaseViewModel() {

    var religiousDayNights = MutableLiveData<List<ReligiousDaysNights>>()

    fun getReligiousDayNightsFromAPI() {

        setLoading(true)
        viewModelScope.launch {
            settingsRepository.getReligiousDayNightsFromAPI().collect { serviceResponse ->

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
            settingsRepository.setReligiousDayNightsToDB(religiousDayNights).collect {

                setLoading(false)
            }
        }
    }
}