package com.namazvakitleri.internetsiz.ui.fragment.region.district

import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.google.firebase.firestore.FirebaseFirestore
import com.namazvakitleri.internetsiz.Application
import com.namazvakitleri.internetsiz.ui.activity.error.ErrorActivity
import com.namazvakitleri.internetsiz.main.MainActivity
import com.namazvakitleri.internetsiz.modal.District
import com.namazvakitleri.internetsiz.modal.PrayTimes
import com.namazvakitleri.internetsiz.modal.Province
import com.namazvakitleri.internetsiz.retrofit.ApiService
import com.namazvakitleri.internetsiz.transactions.CurrentTime
import com.namazvakitleri.internetsiz.ui.base.BaseViewModel
import com.namazvakitleri.internetsiz.utils.enum.Results
import com.namazvakitleri.internetsiz.utils.extension.enqueue
import com.namazvakitleri.internetsiz.utils.extension.openActivity
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

@HiltViewModel
class DistrictViewModel @Inject constructor(
    private var districtRepository: DistrictRepository,
    private val firestore : FirebaseFirestore,
    private val apiService: ApiService
): BaseViewModel() {

    var districtData = MutableLiveData<ArrayList<District>>()
    var prayTimes = MutableLiveData<ArrayList<PrayTimes>>()

    fun getDistrictFromAPI(provinceId: Int) {


        setLoading(true)

        viewModelScope.launch {

            firestore.collection("Districts").document("$provinceId").get().addOnCompleteListener { res ->
                val arrayList = ArrayList<District>()
                if (res.isSuccessful) {
                    val result = res.result

                    if (res.result.exists()) {



                        for (i in result.data!!) {
                            arrayList.add(
                                District(i.key, null, i.value.toString().toInt())
                            )
                        }

                        districtData.value = arrayList
                        setLoading(false)
                    } else {


                        val mapCity = HashMap<String, Int>()
                        apiService.getDistrict(provinceId).enqueue { r1 ->
                            when (r1) {
                                is Results.Success -> {
                                    val districtDatas = r1.response.body() as ArrayList<District>?
                                    for (j in districtDatas!!) {
                                        mapCity.put(j.IlceAdi!!, j.IlceID!!)
                                    }
                                    firestore.collection("Districts").document("$provinceId")
                                        .set(mapCity).addOnCompleteListener {
                                            districtData.value = districtDatas!!
                                            setLoading(false)
                                    }
                                }
                            }
                        }
                    }
                }else{
                    setLoading(false)
                }


                /*districtRepository.getDistrictFromAPI(provinceId).collect { serviceResponse ->
                serviceResponse.enqueue { response ->
                    when (response) {
                        is Results.Success -> {
                            districtData.value = response.response.body() as ArrayList<District>
                            setLoading(false)
                        }

                        is Results.Failure -> {
                            Application.context.openActivity(ErrorActivity::class.java)
                            setLoading(false)
                        }
                    }
                }
            }*/
            }
        }
    }

    fun getPrayTimeAPI(districtId: Int) {

        setLoading(true)

        firestore.collection("PrayTimes")
            .document("PrayTimesDocument").collection(districtId.toString()).get()
            .addOnCompleteListener { task ->

                if (task.isSuccessful) {
                    val result1 = task.result

                    if (!result1.isEmpty) {



                        for(x in 0..30) {
                            if (result1.documents.get(x).id == "30") {
                                val dateFromFirestore =
                                    result1.documents.get(x).get("miladiTarihKisa").toString()
                                val formatter = SimpleDateFormat("dd.MM.yyyy", Locale.ENGLISH)

                                val date = formatter.parse(dateFromFirestore)
                                val dateCurrent = formatter.parse(CurrentTime.getCurrentTime().date)



                                if (dateCurrent!!.after(date)) {
                                    apiService.getPrayTime(districtId).enqueue { r4 ->
                                        when (r4) {
                                            is Results.Success -> {
                                                val prayTime = r4.response.body() as java.util.ArrayList<PrayTimes>?
                                                var count = 0
                                                for (i in prayTime!!) {
                                                    firestore.collection("PrayTimes").document("PrayTimesDocument")
                                                        .collection(districtId.toString())
                                                        .document(count.toString())
                                                        .set(i)
                                                    count++
                                                }
                                                prayTimes.value = prayTime!!
                                                setLoading(false)
                                            }
                                        }
                                    }
                                }else {
                                    val arrayList = ArrayList<PrayTimes>()

                                    for (m in result1.documents) {
                                        val mymap = m.data

                                        arrayList.add(
                                            PrayTimes(
                                                mymap?.get("imsak")
                                                    .toString(),
                                                mymap?.get("gunes")
                                                    .toString(),
                                                mymap?.get("ogle")
                                                    .toString(),
                                                mymap?.get("ikindi")
                                                    .toString(),
                                                mymap?.get("aksam")
                                                    .toString(),
                                                mymap?.get("yatsi")
                                                    .toString(),
                                                mymap?.get("hicriTarihUzun")
                                                    .toString(),
                                                mymap?.get("miladiTarihKisa")
                                                    .toString(),
                                                mymap?.get("miladiTarihUzun")
                                                    .toString()
                                            )
                                        )
                                    }
                                    prayTimes.value = arrayList!!
                                    setLoading(false)
                                }
                                break
                            }
                        }
                    }else {


                        apiService.getPrayTime(districtId).enqueue { r4 ->
                            when (r4) {
                                is Results.Success -> {
                                    val prayTime =
                                        r4.response.body() as java.util.ArrayList<PrayTimes>?
                                    var count = 0
                                    for (i in prayTime!!) {
                                        firestore.collection("PrayTimes")
                                            .document("PrayTimesDocument").collection(districtId.toString())
                                            .document(count.toString()).set(i)
                                        count++
                                    }
                                    prayTimes.value = prayTime!!
                                    setLoading(false)
                                }
                            }
                        }
                    }
                }
            }

        /*viewModelScope.launch {
            districtRepository.getPrayTimesFromAPI(districtId).collect { serviceResponse ->
                serviceResponse.enqueue { response ->
                    when (response) {
                        is Results.Success -> {
                            prayTimes.value = response.response.body() as ArrayList<PrayTimes>
                        }

                        is Results.Failure -> {
                            Application.context.openActivity(ErrorActivity::class.java)
                            setLoading(false)
                        }
                    }
                }
            }
        }*/

    }

    fun setPrayTimesToDB(prayTimes: ArrayList<PrayTimes>, districtId: Int) {

        viewModelScope.launch {
            districtRepository.setPrayTimes(prayTimes).collect { setCompleted ->

                if (setCompleted)
                    Application.context.openActivity(MainActivity::class.java) {
                        putString("DistrictId", districtId.toString())
                    }

                setLoading(false)
            }
        }

    }
}