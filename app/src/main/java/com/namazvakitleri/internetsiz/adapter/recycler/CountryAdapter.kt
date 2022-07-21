package com.namazvakitleri.internetsiz.adapter.recycler

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.namazvakitleri.internetsiz.R
import com.namazvakitleri.internetsiz.holder.CountryViewHolder
import com.namazvakitleri.internetsiz.modal.Country
import java.util.*
import kotlin.collections.ArrayList

class CountryAdapter(
    private var countryList: ArrayList<Country>
): RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    var countryListListToSearch = ArrayList(countryList)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        return CountryViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.custom_select_region, parent, false))
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        var countryViewHolder = holder as CountryViewHolder

        countryViewHolder.bind(countryList[position])
    }

    override fun getItemCount() = countryList.size


    fun filter(provinceName: String?) {

        val provinceName = provinceName?.toUpperCase(Locale.getDefault())
        countryList.clear()

        if (provinceName?.isEmpty() == true) {
            countryList.addAll(countryListListToSearch)
        } else {

            for (i in 0 until countryListListToSearch.size) {
                if (provinceName!!?.contains((countryListListToSearch[i].UlkeAdi)?.uppercase().toString())) {

                    countryList.add(countryListListToSearch[i])
                }
            }
        }

        notifyDataSetChanged()
    }
}