package com.namazvakitleri.internetsiz.ui.fragment.home

import android.content.SharedPreferences
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.namazvakitleri.internetsiz.modal.*
import com.namazvakitleri.internetsiz.modal.firestore.StateValue
import com.namazvakitleri.internetsiz.transactions.CurrentTime
import com.namazvakitleri.internetsiz.ui.base.BaseViewModel
import com.namazvakitleri.internetsiz.utils.constant.ConstEsmaulHusna.ConstEsmaulHusna
import com.namazvakitleri.internetsiz.utils.constant.Constant.AYET
import com.namazvakitleri.internetsiz.utils.constant.Constant.AYET_NUMBER
import com.namazvakitleri.internetsiz.utils.constant.Constant.CHILDNAME
import com.namazvakitleri.internetsiz.utils.constant.Constant.CHILDNAME_MEANING
import com.namazvakitleri.internetsiz.utils.constant.Constant.CURRENT_DATE
import com.namazvakitleri.internetsiz.utils.constant.Constant.CURRENT_ESMAULHUSNA
import com.namazvakitleri.internetsiz.utils.constant.Constant.CURRENT_ESMAULHUSNA_MEANING
import com.namazvakitleri.internetsiz.utils.constant.Constant.DUA
import com.namazvakitleri.internetsiz.utils.constant.Constant.DUA_NUMBER
import com.namazvakitleri.internetsiz.utils.constant.Constant.ESMAULHUSNA_STATE
import com.namazvakitleri.internetsiz.utils.constant.Constant.HADIS
import com.namazvakitleri.internetsiz.utils.constant.Constant.HADIS_NUMBER
import com.namazvakitleri.internetsiz.utils.constant.Constant.HIKMETNAME
import com.namazvakitleri.internetsiz.utils.constant.Constant.HIKMETNAME_NAME
import com.namazvakitleri.internetsiz.utils.extension.get
import com.namazvakitleri.internetsiz.utils.extension.put
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val  homeRepository: HomeRepository,
    private val firestore: FirebaseFirestore
): BaseViewModel() {

    @Inject lateinit var prefs: SharedPreferences

    var esmaulhusna = MutableLiveData<EsmaulHusna>()
    var hadis = MutableLiveData<Hadis>()
    var ayet = MutableLiveData<Ayet>()
    var dua = MutableLiveData<Dua>()
    var Baby = MutableLiveData<BabyModel>()
    var Hikmetname = MutableLiveData<HikmetnameModel>()

    private var currentState = 0

    fun getPrayTimeValue(): PrayTimeValue {
        return homeRepository.prayTimeValue()
    }

    fun getHadisFromAPI() {

        var registeredDate = prefs.get(CURRENT_DATE, "")
        var currentDate = CurrentTime.getCurrentTime().date

        var registeredHadis = prefs.get(HADIS, "")
        var registeredHadisNumber = prefs.get(HADIS_NUMBER, "")

        var registeredAyet = prefs.get(AYET, "")
        var registeredAyetNumber = prefs.get(AYET_NUMBER, "")

        var registeredDua = prefs.get(DUA, "")
        var registeredDuaNumber = prefs.get(DUA_NUMBER, "")

        var esmaulHusna : String = prefs.get(CURRENT_ESMAULHUSNA, "")
        var esmaulHusnaMeaning : String = prefs.get(CURRENT_ESMAULHUSNA_MEANING, "")

        var registeredHikmetname : String = prefs.get(HIKMETNAME, "")
        var registeredHikmetnameName : String = prefs.get(HIKMETNAME_NAME, "")

        var registeredChildName : String = prefs.get(CHILDNAME, "")
        var registeredChildNameMeaning : String = prefs.get(CHILDNAME_MEANING, "")

        val country = prefs.get("CountryLower","türkiye")

        //currentState = checkState().state

        val builder = StringBuilder()
        val string = ConstEsmaulHusna

        var count = 0

        if ((registeredDate == currentDate) && registeredHadis.isNotEmpty() && registeredAyet.isNotEmpty() && registeredDua.isNotEmpty() && esmaulHusna.isNotEmpty() && registeredChildName.isNotEmpty() && registeredHikmetname.isNotEmpty()) {
            esmaulhusna.value = EsmaulHusna(esmaulHusna,esmaulHusnaMeaning)
            hadis.value = Hadis(registeredHadis, registeredHadisNumber)
            ayet.value = Ayet(registeredAyet, registeredAyetNumber)
            dua.value = Dua(registeredDua, registeredDuaNumber)
            Baby.value = BabyModel(registeredChildName,registeredChildNameMeaning)
            Hikmetname.value = HikmetnameModel(registeredHikmetname,registeredHikmetnameName)
            //updateState(country)
            //checkTime(country)
        } else {

            var esmaulhusnaState = prefs.get(ESMAULHUSNA_STATE, 0)

            if(esmaulhusnaState > 4327){
                esmaulhusnaState = 0
            }

            for(i in esmaulhusnaState..string.length){

                if(string.get(i+1) != ':'){
                    builder.append(string.get(i))
                }else{
                    count = i+2
                    builder.append(string.get(i))
                    esmaulHusna = builder.toString()
                    prefs.put(CURRENT_ESMAULHUSNA,builder.toString())
                    builder.clear()
                    break
                }
            }
            for(i in count..string.length){

                if(string.get(i+1) != '\n'){
                    builder.append(string.get(i))
                }else{
                    builder.append(string.get(i))
                    esmaulHusnaMeaning = builder.toString()
                    prefs.put(CURRENT_ESMAULHUSNA_MEANING,builder.toString())
                    prefs.put(ESMAULHUSNA_STATE, i+2)
                    break
                }
            }

            esmaulhusna.value = EsmaulHusna(esmaulHusna,esmaulHusnaMeaning)

            viewModelScope.launch {
                homeRepository.getHadis().collect { response ->
                    hadis.value = response
                }
                homeRepository.getAyet().collect { response ->
                    ayet.value = response
                }
                homeRepository.getDua().collect { response ->
                    dua.value = response
                    getBaby()
                }

            }
            /*if(checkTime(country)){
                updateState(country)
            }*/

            prefs.put(CURRENT_DATE, CurrentTime.getCurrentTime().date)
        }

    }

    private fun getHikmetname(){
        var name : String? = null
        var hikmetname : String? = null

        val r = Random()

        firestore.collection("Daily")
            .document("Hikmetname")
            .get().addOnSuccessListener { result ->
            if(result.data != null){
                var i = 0
                val state = r.nextInt(result.data!!.size)
                for (document in result.data!!) {
                    if(i == state){
                        name = document.value.toString()
                        hikmetname = document.key
                        Hikmetname.value = HikmetnameModel(hikmetname,name)
                        prefs.put(HIKMETNAME,hikmetname)
                        prefs.put(HIKMETNAME_NAME,name)
                        break
                    }else{
                        i++
                    }
                }

            }

        }
    }

    private fun getBaby(){
        var babyname : String? = null
        var meaning : String? = null

        val r = Random()

        firestore.collection("Daily")
            .document("Babynames")
            .get().addOnSuccessListener { result ->
                if(result.data != null){
                    var i = 0
                    val state = r.nextInt(result.data!!.size)
                    for (document in result.data!!) {
                        if(i == state) {
                            babyname = document.key
                            meaning = document.value.toString()
                            Baby.value = BabyModel(babyname,meaning)
                            prefs.put(CHILDNAME,babyname)
                            prefs.put(CHILDNAME_MEANING,meaning)
                            break
                        }else{
                            i++
                        }
                    }
                    getHikmetname()

                }

            }
    }

    /*private fun checkState() : StateValue{
        val checkCountry = prefs.get("CountryLower","türkiye")
        var state = 0
        firestore.collection("Daily")
            .document("CurrentState")
            .get().addOnSuccessListener { result ->
                if (result.data != null) {
                    for (document in result.data!!) {
                        if(checkCountry == document.key){
                            state = document.value.toString().toInt()
                            println("statee $state")
                        }
                    }
                }

            }
        return StateValue(checkCountry, state)
    }

    private fun checkTime(country : String) : Boolean{
        var updateTime = false
        println("heyoo"+System.currentTimeMillis())
        firestore.collection("Daily")
            .document("CurrentTime")
            .get().addOnSuccessListener { result ->
                if (result.data != null) {
                    for (document in result.data!!) {
                        if (country == document.key) {
                            val creationDate: Date = result.getDate("Date")!!
                            val a = (System.currentTimeMillis() / 1000*60*60*24)-1
                            val b = (creationDate.time / 1000*60*60*24)
                            println("asil :"+a + " asil1 :"+b)
                            if((System.currentTimeMillis() / 86400000)-1 == (creationDate.time / 86400000)){
                                updateTime = true
                                break
                            }else{
                                updateTime = false
                            }
                        }
                    }
                }
            }
        return updateTime
    }

    private fun updateState(country : String){
        firestore.collection("Daily").document("CurrentTime").set(hashMapOf(country to FieldValue.serverTimestamp()))
        firestore.collection("Daily")
            .document("CurrentState")
            .set(hashMapOf(country to currentState+1))
    }*/
}