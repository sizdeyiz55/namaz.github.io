package com.namazvakitleri.internetsiz.holder

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.RecyclerView
import com.namazvakitleri.internetsiz.R
import com.namazvakitleri.internetsiz.modal.Province
import com.namazvakitleri.internetsiz.ui.fragment.region.district.DistrictFragment
import com.namazvakitleri.internetsiz.utils.extension.inTransaction
import dagger.hilt.android.internal.managers.ViewComponentManager
import kotlinx.android.synthetic.main.custom_select_region.view.*

class ProvinceViewHolder(itemView: View): RecyclerView.ViewHolder(itemView), View.OnClickListener {

    private var districtFragment = DistrictFragment()
    private lateinit var province: Province
    private var manager: FragmentManager
    private var bundle = Bundle()

    init {

        manager = (activityContext() as AppCompatActivity).supportFragmentManager

        itemView.region_layout.setOnClickListener(this)
    }

    fun bind(province: Province) {

        this.province = province

        displayProvinceName()

    }

    private fun displayProvinceName() {

        itemView.nameTxt.text = province.SehirAdi

    }

    override fun onClick(view: View?) {

        when(view?.id) {

            R.id.region_layout -> {

                province.SehirID?.let { bundle.putInt("ProvinceId", it) }
                bundle.putString("ProvinceName", province.SehirAdi)
                districtFragment.arguments = bundle

                manager?.inTransaction {
                    addToBackStack("DistrictFragment")
                    replace(R.id.fragmentContainer, districtFragment)
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