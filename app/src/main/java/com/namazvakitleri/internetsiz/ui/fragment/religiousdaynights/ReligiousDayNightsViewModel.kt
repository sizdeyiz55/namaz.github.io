package com.namazvakitleri.internetsiz.ui.fragment.religiousdaynights

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.namazvakitleri.internetsiz.db.ReligiousDayNights
import com.namazvakitleri.internetsiz.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ReligiousDayNightsViewModel @Inject constructor(
    private var religiousDayNightsRepository: ReligiousDayNightsRepository
): BaseViewModel() {

    var religiousDaysNightsList = MutableLiveData<List<ReligiousDayNights>>()

    fun getReligiousDayNights(year: Int) {
        viewModelScope.launch {
            religiousDayNightsRepository.getReligiousDaysNights(year).collect { religiousDaysNight ->

                religiousDaysNightsList.value = religiousDaysNight

            }
        }
    }
}