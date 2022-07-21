package com.namazvakitleri.internetsiz.holder

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.RecyclerView
import com.namazvakitleri.internetsiz.R
import com.namazvakitleri.internetsiz.modal.Country
import com.namazvakitleri.internetsiz.ui.fragment.region.province.ProvinceFragment
import com.namazvakitleri.internetsiz.utils.extension.inTransaction
import com.namazvakitleri.internetsiz.utils.extension.put
import dagger.hilt.android.internal.managers.ViewComponentManager
import kotlinx.android.synthetic.main.custom_select_region.view.*

class CountryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {


    private var provinceFragment = ProvinceFragment()
    private lateinit var country: Country
    private var manager: FragmentManager
    private var bundle = Bundle()

    init {

        manager = (activityContext() as AppCompatActivity).supportFragmentManager

        itemView.region_layout.setOnClickListener(this)

    }


    fun bind(country: Country) {

        this.country = country

        displayCountryName()
    }


    private fun displayCountryName() {

        itemView.nameTxt.text = country.UlkeAdi

    }

    override fun onClick(view: View?) {

        when(view?.id) {
            R.id.region_layout -> {

                country.UlkeID?.let { bundle.putInt("CountryId", it)
                bundle.putString("CountryName",country.UlkeAdi)}
                provinceFragment.arguments = bundle

                manager.inTransaction {
                    addToBackStack("ProvinceFragment")
                    replace(R.id.fragmentContainer, provinceFragment)
                }

            }
        }
    }

    private fun activityContext(): Context? {
        val context = itemView.context
        return if (context is ViewComponentManager.FragmentContextWrapper) {
            context.baseContext
        } else context
    }
}