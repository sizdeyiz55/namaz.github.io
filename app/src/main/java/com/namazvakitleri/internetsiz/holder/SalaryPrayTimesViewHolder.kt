package com.namazvakitleri.internetsiz.holder

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.namazvakitleri.internetsiz.modal.PrayTimes
import kotlinx.android.synthetic.main.custom_salary_pray_time.view.*

class SalaryPrayTimesViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {


    private lateinit var prayTimes: PrayTimes

    fun bind(prayTimes: PrayTimes) {

        this.prayTimes = prayTimes

        displayPrayTimes()
    }

    private fun displayPrayTimes() {


        itemView.salaryImsakTxt.text = prayTimes.Imsak
        itemView.salaryGunesTxt.text = prayTimes.Gunes
        itemView.salaryOgleTxt.text = prayTimes.Ogle
        itemView.salaryIkindiTxt.text = prayTimes.Ikindi
        itemView.salaryAksamTxt.text = prayTimes.Aksam
        itemView.salaryYatsiTxt.text = prayTimes.Yatsi
        itemView.miladiDateTxt.text = prayTimes.MiladiTarihUzun
    }

}