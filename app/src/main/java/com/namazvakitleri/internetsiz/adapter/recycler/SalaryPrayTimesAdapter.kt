package com.namazvakitleri.internetsiz.adapter.recycler

import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.recyclerview.widget.RecyclerView
import com.namazvakitleri.internetsiz.R
import com.namazvakitleri.internetsiz.holder.SalaryPrayTimesViewHolder
import com.namazvakitleri.internetsiz.modal.PrayTimes

class SalaryPrayTimesAdapter(
    private var prayTimes: ArrayList<PrayTimes>
): RecyclerView.Adapter<RecyclerView.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {


        return SalaryPrayTimesViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.custom_salary_pray_time, parent, false)
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        val anim : Animation = AnimationUtils.loadAnimation(holder.itemView.context,R.anim.fade_in)

        var holder = holder as SalaryPrayTimesViewHolder

        holder.bind(prayTimes[position])
        holder.itemView.startAnimation(anim)
    }

    override fun getItemCount() = prayTimes.size
}