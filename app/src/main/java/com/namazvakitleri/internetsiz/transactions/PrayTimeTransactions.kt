package com.namazvakitleri.internetsiz.transactions

import com.namazvakitleri.internetsiz.db.PrayTime
import com.namazvakitleri.internetsiz.modal.CurrentPrayTimeInfo
import java.util.*
import kotlin.math.abs

class PrayTimeTransactions {

    companion object {


        private const val oneHourMs = 3600000
        private const val oneMinuteMs = 60000
        private const val oneSecondMs = 1000

        fun editTimes(editTimes: PrayTime): PrayTime {

            return PrayTime(
                0,
            editTimes.Imsak.replace(":", "."),
            editTimes.Gunes.replace(":", "."),
            editTimes.Ogle.replace(":", "."),
            editTimes.Ikindi.replace(":", "."),
            editTimes.Aksam.replace(":", "."),
            editTimes.Yatsi.replace(":", "."))
        }

        fun calculatePrayTime(
            prayTimes: PrayTime,
            currentTime: String,
            howManyMinuteAgo: Int = 0
        ): CurrentPrayTimeInfo {

            return whichPrayTime(prayTimes, currentTime.replace(":", "."), howManyMinuteAgo)

        }

        private fun whichPrayTime(
            prayTimes: PrayTime,
            currentTime: String,
            howManyMinuteAgo: Int
        ): CurrentPrayTimeInfo {

            val imsak = prayTimes.Imsak.toDouble()
            val gunes = prayTimes.Gunes.toDouble()
            val ogle = prayTimes.Ogle.toDouble()
            val ikindi = prayTimes.Ikindi.toDouble()
            val aksam = prayTimes.Aksam.toDouble()
            val yatsi = prayTimes.Yatsi.toDouble()
            val currentTimeForCompare = currentTime.substring(0, 5)


            when(howManyMinuteAgo) {
                0 ->{
                    when {
                        imsak > currentTimeForCompare.toDouble() -> {
                            return fromTimeToMilliseconds(prayTimes.Imsak, currentTime, "İmsak")
                        }
                        gunes > currentTimeForCompare.toDouble() -> {
                            return fromTimeToMilliseconds(prayTimes.Gunes, currentTime, "Güneş")
                        }
                        ogle > currentTimeForCompare.toDouble() -> {
                            return fromTimeToMilliseconds(prayTimes.Ogle, currentTime, "Öğle")
                        }
                        ikindi > currentTimeForCompare.toDouble() -> {
                            return fromTimeToMilliseconds(prayTimes.Ikindi, currentTime, "İkindi")
                        }
                        aksam > currentTimeForCompare.toDouble() -> {
                            return fromTimeToMilliseconds(prayTimes.Aksam, currentTime, "Akşam")
                        }
                        yatsi > currentTimeForCompare.toDouble() -> {
                            return fromTimeToMilliseconds(prayTimes.Yatsi, currentTime, "Yatsı")
                        }
                        else -> {
                            return fromTimeToMilliseconds(prayTimes.Imsak, currentTime, "İmsak", true)
                        }
                    }
                } 
                else -> {
                    when {
                        imsak > currentTimeForCompare.toDouble() -> {
                            return fromTimeToMilliseconds(prayTimes.Gunes, currentTime, "Güneş")
                        }
                        gunes > currentTimeForCompare.toDouble() -> {
                            return fromTimeToMilliseconds(prayTimes.Ogle, currentTime, "Öğle")
                        }
                        ogle > currentTimeForCompare.toDouble() -> {
                            return fromTimeToMilliseconds(prayTimes.Ikindi, currentTime, "İkindi")
                        }
                        ikindi > currentTimeForCompare.toDouble() -> {
                            return fromTimeToMilliseconds(prayTimes.Aksam, currentTime, "Akşam")
                        }
                        aksam > currentTimeForCompare.toDouble() -> {
                            return fromTimeToMilliseconds(prayTimes.Yatsi, currentTime, "Yatsı")
                        }
                        else -> {
                            return fromTimeToMilliseconds(prayTimes.Imsak, currentTime, "İmsak", true)
                        }
                    }
                }
            }
        }

        private fun fromTimeToMilliseconds(
            prayTime: String,
            currentTime: String,
            whichTime: String,
            isAfterYatsi: Boolean? = null
        ): CurrentPrayTimeInfo {

            val currentPrayTimeInfo: CurrentPrayTimeInfo
            val prayTimeMs: Long
            val currentTimeMs: Long

            if (isAfterYatsi == true) {

                currentTimeMs = ((23 - (currentTime.substring(0, 2)
                    .toLong())) * oneHourMs) + ((59 - (currentTime.substring(3, 5)
                    .toLong())) * oneMinuteMs) + ((59 - currentTime.substring(6, 8)
                    .toLong()) * oneSecondMs)

                prayTimeMs =
                    (prayTime.substring(0, 2)
                        .toLong() * oneHourMs) + (prayTime.substring(3, 5)
                        .toLong() * oneMinuteMs) - (59 * oneSecondMs)

                currentPrayTimeInfo = CurrentPrayTimeInfo(prayTime,currentTimeMs + prayTimeMs, whichTime, true)
            } else {
                prayTimeMs =
                    (prayTime.substring(0, 2).toLong() * oneHourMs) +
                     (prayTime.substring(3, 5).toLong() * oneMinuteMs)

                currentTimeMs = (currentTime.substring(0, 2).toLong() * oneHourMs) +
                        (currentTime.substring(3, 5).toLong() * oneMinuteMs) +
                        (currentTime.substring(6, 8).toLong() * oneSecondMs)

                currentPrayTimeInfo =
                    CurrentPrayTimeInfo(prayTime,
                        abs(prayTimeMs - currentTimeMs),
                        whichTime
                )
            }

            return currentPrayTimeInfo
        }

        fun fromMillisecondsToTime(calculateTimes: Long): String {

            var hours = ((calculateTimes / (1000 * 60 * 60)) % 24)
            var minutes = (calculateTimes / (1000 * 60)) % 60
            var seconds = (calculateTimes / 1000) % 60

            return String.format("%02d : %02d : %02d", hours, minutes, seconds)
        }

        fun isTimeKerahat(prayTime: String, currentTime: String, fortyFiveMs: Int): Boolean {
            val prayTimeMs = ((prayTime.substring(0, 2).toLong() * oneHourMs)
                                    + (prayTime.substring(3,5).toLong() * oneMinuteMs)) + fortyFiveMs
            val currentTimeMs = (currentTime.substring(0, 2).toLong() * oneHourMs) +
                    (currentTime.substring(3, 5).toLong() * oneMinuteMs) +
                    (currentTime.substring(6, 8).toLong() * oneSecondMs)

            return prayTimeMs > currentTimeMs
        }

        fun calculateTimeToMs(remainingPrayTime: String) =
            Calendar.getInstance().apply {
                this.set(Calendar.HOUR_OF_DAY, remainingPrayTime.substring(0, 2).toInt())
                this.set(Calendar.MINUTE, remainingPrayTime.substring(3, 5).toInt())
                this.set(Calendar.SECOND, 0)
            }.timeInMillis


    }
}