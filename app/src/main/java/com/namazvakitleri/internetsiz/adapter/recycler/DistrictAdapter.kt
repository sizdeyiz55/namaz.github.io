package com.namazvakitleri.internetsiz.adapter.recycler

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.namazvakitleri.internetsiz.R
import com.namazvakitleri.internetsiz.holder.DistrictViewHolder
import com.namazvakitleri.internetsiz.modal.District
import com.namazvakitleri.internetsiz.ui.fragment.region.district.DistrictViewModel
import java.util.*
import kotlin.collections.ArrayList

class DistrictAdapter(
    private var districtViewModel: DistrictViewModel,
    private var districtList: ArrayList<District>
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {


    private var districtListToSearch = ArrayList(districtList)


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        return DistrictViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.custom_select_region, parent, false), districtViewModel
        )

    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        var provinceViewModel = holder as DistrictViewHolder

        provinceViewModel.bind(districtList[position])
    }

    override fun getItemCount() = districtList.size


    fun filter(provinceName: String?) {

        val provinceName = provinceName?.toUpperCase(Locale.getDefault())
        districtList.clear()

        if (provinceName?.isEmpty() == true) {
            districtList.addAll(districtListToSearch)
        } else {

            for (i in 0 until districtListToSearch.size) {
                if (provinceName!! in (districtListToSearch[i].IlceAdi)?.uppercase().toString()) {

                    districtList.add(districtListToSearch[i])
                }
            }
        }

        notifyDataSetChanged()
    }
}