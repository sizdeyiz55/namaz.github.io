package com.namazvakitleri.internetsiz.adapter.recycler

import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.recyclerview.widget.RecyclerView
import com.namazvakitleri.internetsiz.R
import com.namazvakitleri.internetsiz.db.ReligiousDayNights
import com.namazvakitleri.internetsiz.holder.ReligiousDayNightsViewHolder

class ReligiousDayNightsAdapter(
    private var religiousDaysNights: List<ReligiousDayNights>
): RecyclerView.Adapter<RecyclerView.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {


        return ReligiousDayNightsViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.custom_religious_days_nights, parent, false)
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        val anim : Animation = AnimationUtils.loadAnimation(holder.itemView.context,R.anim.fade_in)

        var holder = holder as ReligiousDayNightsViewHolder

        holder.bind(religiousDaysNights[position])
        holder.itemView.startAnimation(anim)
    }

    override fun getItemCount() = religiousDaysNights.size
}