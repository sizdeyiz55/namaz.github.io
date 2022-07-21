package com.namazvakitleri.internetsiz.db

import androidx.lifecycle.MutableLiveData
import com.namazvakitleri.internetsiz.db.dao.PrayTimeDao
import com.namazvakitleri.internetsiz.modal.District
import com.namazvakitleri.internetsiz.modal.PrayTimes
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class InsertPrayTimeToDB @Inject constructor(private val prayTimesDao: PrayTimeDao) {

    var isDataFinished = MutableLiveData<Boolean>()

        suspend fun insertData(prayTimes: ArrayList<PrayTimes>) =
            withContext(CoroutineScope(Dispatchers.IO).coroutineContext) {

                for (x in prayTimes.indices) {

                    prayTimesDao.insertPrayTimes(
                        PrayTime(
                            0,
                            prayTimes[x].Imsak!!,
                            prayTimes[x].Gunes!!,
                            prayTimes[x].Ogle!!,
                            prayTimes[x].Ikindi!!,
                            prayTimes[x].Aksam!!,
                            prayTimes[x].Yatsi!!,
                            prayTimes[x].HicriTarihUzun!!,
                            prayTimes[x].MiladiTarihKisa!!,
                            prayTimes[x].MiladiTarihUzun!!)
                    )
                }
                withContext(Dispatchers.Main){
                    isDataFinished.value = true
                }
            }
}