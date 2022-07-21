package com.namazvakitleri.internetsiz.holder

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.namazvakitleri.internetsiz.modal.PrayTimes
import kotlinx.android.synthetic.main.custom_imsakiye.view.*

class ImsakiyeViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {

    private lateinit var prayTime: PrayTimes

    fun bind(prayTime: PrayTimes) {

        this.prayTime = prayTime

        displayPrayTime()
    }

    private fun displayPrayTime() {

        itemView.imsakiyeMiladiDateTxt.text = prayTime.MiladiTarihUzun
        itemView.imsakiyeImsakTxt.text = prayTime.Imsak
        itemView.imsakiyeAksamTxt.text = prayTime.Aksam
    }
}