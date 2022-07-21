package com.namazvakitleri.internetsiz.ui.fragment.home

import android.content.SharedPreferences
import com.namazvakitleri.internetsiz.Application
import com.namazvakitleri.internetsiz.R
import com.namazvakitleri.internetsiz.db.dao.PrayTimeDao
import com.namazvakitleri.internetsiz.modal.Ayet
import com.namazvakitleri.internetsiz.modal.Dua
import com.namazvakitleri.internetsiz.modal.Hadis
import com.namazvakitleri.internetsiz.modal.PrayTimeValue
import com.namazvakitleri.internetsiz.transactions.CurrentTime
import com.namazvakitleri.internetsiz.transactions.PrayTimeTransactions
import com.namazvakitleri.internetsiz.utils.constant.Constant.AYET
import com.namazvakitleri.internetsiz.utils.constant.Constant.AYET_HADIS_API_URL
import com.namazvakitleri.internetsiz.utils.constant.Constant.AYET_NUMBER
import com.namazvakitleri.internetsiz.utils.constant.Constant.DUA
import com.namazvakitleri.internetsiz.utils.constant.Constant.DUA_NUMBER
import com.namazvakitleri.internetsiz.utils.constant.Constant.HADIS
import com.namazvakitleri.internetsiz.utils.constant.Constant.HADIS_NUMBER
import com.namazvakitleri.internetsiz.utils.extension.put
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import org.jsoup.Jsoup
import java.lang.Exception
import java.text.SimpleDateFormat
import javax.inject.Inject

class HomeRepository @Inject constructor(
    private val prayTimeDao: PrayTimeDao
) {

    @Inject lateinit var prefs: SharedPreferences

    private val hh_mm_ss = SimpleDateFormat("HH:mm:ss")

    fun prayTimeValue(): PrayTimeValue {

        val currentTime = CurrentTime.getCurrentTime()

        var prayTimes = prayTimeDao.getPrayTime(currentTime.date)

        val editedTime = PrayTimeTransactions.editTimes(prayTimes)
        val calculateTimes = PrayTimeTransactions.calculatePrayTime(editedTime, hh_mm_ss.format(currentTime.time))
        val fromMillisecondsToTime =
            PrayTimeTransactions.fromMillisecondsToTime(calculateTimes.remainingPrayTimeMs)

        return PrayTimeValue(calculateTimes, prayTimes, fromMillisecondsToTime)
    }

    fun getHadis(): Flow<Hadis> {

        return flow {
            emit(getHadisFromAPI())
        }
    }

    fun getAyet(): Flow<Ayet> {

        return flow {
            emit(getAyetFromAPI())
        }
    }

    fun getDua(): Flow<Dua> {

        return flow {
            emit(getDuaFromAPI())
        }
    }

    private suspend fun getHadisFromAPI(): Hadis{
        var done = false
        var hadis =""
        var hadisNumber =""

        withContext(Dispatchers.IO) {

            try {

                val doc = Jsoup.connect(AYET_HADIS_API_URL).get()

                hadis = doc.select("div[class=hadis]").select("p").text()
                hadisNumber =
                    doc.select("div[class=aht-bottom]").select("p[class=alt-sure-title]")[1].text()
                done = true

            } catch (exception: Exception) {
            }
        }

        if (hadis.isEmpty() && done) {
            hadis = Application.context.resources.getText(R.string.default_hadis).toString()
            hadisNumber = Application.context.resources.getText(R.string.default_hadis_number).toString()
        }

        prefs.put(HADIS, hadis)
        prefs.put(HADIS_NUMBER, hadisNumber)

        return Hadis(hadis,hadisNumber)
    }

    private suspend fun getAyetFromAPI(): Ayet{
        var done = false
        var ayet =""
        var ayetNumber =""

        withContext(Dispatchers.IO) {

            try {

                val doc = Jsoup.connect(AYET_HADIS_API_URL).get()

                ayet = doc.select("div[class=ayet]").select("p").text()
                ayetNumber =
                    doc.select("div[class=aht-bottom]").select("p[class=alt-sure-title]")[0].text()
                done = true

            } catch (exception: Exception) {
            }
        }

        if (ayet.isEmpty() && done) {
            ayet = Application.context.resources.getText(R.string.default_ayet).toString()
            ayetNumber = Application.context.resources.getText(R.string.default_ayet_number).toString()
        }

        prefs.put(AYET, ayet)
        prefs.put(AYET_NUMBER, ayetNumber)

        return Ayet(ayet,ayetNumber)
    }

    private suspend fun getDuaFromAPI(): Dua{

        var done = false

        var dua =""
        var duaNumber =""

        withContext(Dispatchers.IO) {

            try {

                val doc = Jsoup.connect(AYET_HADIS_API_URL).get()

                dua = doc.select("div[class=dua]").select("p").text()
                duaNumber =
                    doc.select("div[class=aht-bottom]").select("p[class=alt-sure-title]")[2].text()

                done = true

            } catch (exception: Exception) {

            }
        }

        if (dua.isEmpty() && done) {
            dua = Application.context.resources.getText(R.string.default_dua).toString()
            duaNumber = Application.context.resources.getText(R.string.default_dua_number).toString()
        }

        prefs.put(DUA, dua)
        prefs.put(DUA_NUMBER, duaNumber)

        return Dua(dua,duaNumber)
    }

}