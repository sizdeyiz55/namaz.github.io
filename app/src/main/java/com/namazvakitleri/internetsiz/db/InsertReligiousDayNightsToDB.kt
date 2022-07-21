package com.namazvakitleri.internetsiz.db

import com.namazvakitleri.internetsiz.db.dao.PrayTimeDao
import com.namazvakitleri.internetsiz.modal.ReligiousDaysNights
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class InsertReligiousDayNightsToDB @Inject constructor(private val prayTimeDao: PrayTimeDao) {

    suspend fun insertData(religiousDayNights: List<ReligiousDaysNights>) =
        withContext(CoroutineScope(Dispatchers.IO).coroutineContext) {

            for (x in religiousDayNights.indices) {
                prayTimeDao.insertReligiousDayNights(ReligiousDayNights(
                    0,
                    religiousDayNights[x].Day!!,
                    religiousDayNights[x].MiladiDateMonth!!,
                    religiousDayNights[x].MiladiDateDay!!,
                    religiousDayNights[x].HicriDate!!,
                    religiousDayNights[x].Year
                ))
            }
        }
}