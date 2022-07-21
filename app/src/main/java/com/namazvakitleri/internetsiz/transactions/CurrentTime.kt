package com.namazvakitleri.internetsiz.transactions

import com.namazvakitleri.internetsiz.modal.CurrentTimeDate
import java.text.SimpleDateFormat
import java.util.*

data class CurrentTime(
    var time: Date,
    var date: String
) {

    companion object {

        private var currentDateFormat = SimpleDateFormat("dd.MM.yyyy")

        fun getCurrentTime(): CurrentTimeDate {


            val currentTime = Calendar.getInstance().time // guncel saati al.
            val currentDate = currentDateFormat.format(currentTime) // tarih formatina cevir.



            return CurrentTimeDate(currentTime, currentDate)
        }
    }
}