package com.namazvakitleri.internetsiz.holder

import android.content.Context
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.RecyclerView
import com.namazvakitleri.internetsiz.Application.Companion.context
import com.namazvakitleri.internetsiz.R
import com.namazvakitleri.internetsiz.modal.District
import com.namazvakitleri.internetsiz.ui.fragment.region.district.DistrictViewModel
import com.namazvakitleri.internetsiz.utils.constant.ConstPrefs.DISTRICT_ID
import com.namazvakitleri.internetsiz.utils.constant.ConstPrefs.DISTRICT_NAME
import com.namazvakitleri.internetsiz.utils.extension.put
import dagger.hilt.android.internal.managers.ViewComponentManager
import kotlinx.android.synthetic.main.custom_select_region.view.*
import javax.inject.Inject

class DistrictViewHolder @Inject constructor(itemView: View, private var districtViewModel: DistrictViewModel): RecyclerView.ViewHolder(itemView), View.OnClickListener {

    private var prefs = context.getSharedPreferences(
        context.getString(R.string.app_name),
        Context.MODE_PRIVATE)

    private lateinit var district: District
    private var manager: FragmentManager

    init {

        manager = (activityContext() as AppCompatActivity).supportFragmentManager

        itemView.region_layout.setOnClickListener(this)
    }


    fun bind(district: District) {

        this.district = district

        displayProvinceName()

    }

    private fun displayProvinceName() {

        itemView.nameTxt.text = district.IlceAdi

    }

    override fun onClick(view: View?) {

        when(view?.id) {
            R.id.region_layout -> {

                district.IlceID?.let { districtViewModel.getPrayTimeAPI(it) }
                addShared()
            }
        }
    }


    private fun activityContext(): Context? {
        val context = itemView.context
        return if (context is ViewComponentManager.FragmentContextWrapper) {
            context.baseContext
        } else context
    }


    private fun addShared() {

        prefs.put(DISTRICT_NAME, district.IlceAdi)
        prefs.put(DISTRICT_ID, district.IlceID)

    }
}