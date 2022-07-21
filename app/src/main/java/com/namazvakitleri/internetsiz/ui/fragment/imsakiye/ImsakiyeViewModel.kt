package com.namazvakitleri.internetsiz.ui.fragment.imsakiye

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.namazvakitleri.internetsiz.Application
import com.namazvakitleri.internetsiz.modal.PrayTimes
import com.namazvakitleri.internetsiz.ui.activity.error.ErrorActivity
import com.namazvakitleri.internetsiz.ui.base.BaseViewModel
import com.namazvakitleri.internetsiz.ui.fragment.region.district.DistrictRepository
import com.namazvakitleri.internetsiz.utils.enum.Results
import com.namazvakitleri.internetsiz.utils.extension.enqueue
import com.namazvakitleri.internetsiz.utils.extension.openActivity
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ImsakiyeViewModel @Inject constructor(
    private var districtRepository: DistrictRepository
): BaseViewModel() {

    var prayTimes = MutableLiveData<ArrayList<PrayTimes>>()

    fun getPrayTimeAPI(districtId: Int) {

        setLoading(true)

        viewModelScope.launch {
            districtRepository.getPrayTimesFromAPI(districtId).collect { serviceResponse ->
                serviceResponse.enqueue { response ->
                    when (response) {
                        is Results.Success -> {
                            prayTimes.value = response.response.body() as ArrayList<PrayTimes>
                            setLoading(false)
                        }

                        is Results.Failure -> {
                            Application.context.openActivity(ErrorActivity::class.java)
                            setLoading(false)

                        }
                    }
                }
            }
        }
    }
}