package com.namazvakitleri.internetsiz.adapter.recycler

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.namazvakitleri.internetsiz.R
import com.namazvakitleri.internetsiz.holder.ProvinceViewHolder
import com.namazvakitleri.internetsiz.modal.Province
import java.util.*
import kotlin.collections.ArrayList

class ProvinceAdapter(
    private var provinceList: ArrayList<Province>
): RecyclerView.Adapter<RecyclerView.ViewHolder>() {


    private var provinceListToSearch = ArrayList(provinceList)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        return ProvinceViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.custom_select_region, parent, false))
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        var provinceViewModel = holder as ProvinceViewHolder

        provinceViewModel.bind(provinceList[position])

    }

    override fun getItemCount() = provinceList.size


    fun filter(provinceName: String?) {

        val provinceName = provinceName?.toUpperCase(Locale.getDefault())
        provinceList.clear()

        if (provinceName?.isEmpty() == true) {
            provinceList.addAll(provinceListToSearch)
        } else {

            for (i in 0 until provinceListToSearch.size) {
                if (provinceName!! in (provinceListToSearch[i].SehirAdi)?.uppercase().toString()) {

                    provinceList.add(provinceListToSearch[i])
                }
            }
        }

        notifyDataSetChanged()
    }
}