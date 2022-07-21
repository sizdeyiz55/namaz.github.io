package com.namazvakitleri.internetsiz.ui.fragment.religiousdaynights

import com.namazvakitleri.internetsiz.db.ReligiousDayNights
import com.namazvakitleri.internetsiz.db.dao.PrayTimeDao
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class ReligiousDayNightsRepository @Inject constructor(
    private val prayTimeDao: PrayTimeDao
) {


    fun getReligiousDaysNights(year: Int): Flow<List<ReligiousDayNights>> {


        return flow {

            emit(prayTimeDao.getReligiousDayNights(year))
        }
    }
}