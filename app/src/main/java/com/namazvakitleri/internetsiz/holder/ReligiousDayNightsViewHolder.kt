package com.namazvakitleri.internetsiz.holder

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.namazvakitleri.internetsiz.db.ReligiousDayNights
import kotlinx.android.synthetic.main.custom_religious_days_nights.view.*

class ReligiousDayNightsViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {


    private lateinit var religiousDaysNights: ReligiousDayNights

    fun bind(religiousDaysNights: ReligiousDayNights) {

        this.religiousDaysNights = religiousDaysNights

        displayReligiousDayNights()
    }

    private fun displayReligiousDayNights() {

        itemView.dayTxt.text = "${religiousDaysNights.Day} - (${religiousDaysNights.MiladiDateDay?.substring(0,2)} ${religiousDaysNights.MiladiDateMonth} /${religiousDaysNights.MiladiDateDay?.substring(2)})"
        itemView.miladiMonthDayDateTxt.text = "${religiousDaysNights.HicriDate}"
    }
}