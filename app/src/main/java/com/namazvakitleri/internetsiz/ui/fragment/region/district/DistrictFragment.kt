package com.namazvakitleri.internetsiz.ui.fragment.region.district

import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.namazvakitleri.internetsiz.adapter.recycler.DistrictAdapter
import com.namazvakitleri.internetsiz.manager.LoadingBarManager
import com.namazvakitleri.internetsiz.manager.NetworkDialogManager
import com.namazvakitleri.internetsiz.databinding.FragmentDistrictBinding
import com.namazvakitleri.internetsiz.db.dao.PrayTimeDao
import com.namazvakitleri.internetsiz.modal.District
import com.namazvakitleri.internetsiz.modal.PrayTimes
import com.namazvakitleri.internetsiz.ui.base.BaseFragment
import com.namazvakitleri.internetsiz.utils.constant.ConstPrefs.CREATED_ALARM
import com.namazvakitleri.internetsiz.utils.constant.ConstPrefs.DISTRICT_ID
import com.namazvakitleri.internetsiz.utils.constant.ConstPrefs.PROVINCE_NAME
import com.namazvakitleri.internetsiz.utils.constant.Constant
import com.namazvakitleri.internetsiz.utils.extension.*
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

private const val PROVINCE_ID = "ProvinceId"
private const val PROVINCE_NAME = "ProvinceName"

@AndroidEntryPoint
class DistrictFragment @Inject constructor():  BaseFragment<FragmentDistrictBinding>() {

    override val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> FragmentDistrictBinding
        = FragmentDistrictBinding::inflate

    override fun observeViewModel() {

        observe(viewModel.districtData, ::observeDistrict)
        observe(viewModel.prayTimes, ::observePrayTimes)
    }

    private val viewModel: DistrictViewModel by viewModels()

    @Inject lateinit var prefs: SharedPreferences
    @Inject lateinit var prayTimeDao: PrayTimeDao

    private lateinit var layoutManager: LinearLayoutManager

    private var districtId = -1
    private var provinceId: Int = -1
    private var provinceName: String? = null

    private lateinit var districtAdapter: DistrictAdapter
    private lateinit var districtList: ArrayList<District>


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            provinceId = it.getInt(PROVINCE_ID)
            provinceName = it.getString(PROVINCE_NAME)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        LoadingBarManager.build(requireContext(), "İlçeler ${Constant.LOAD}")

        if (!requireContext().isConnectedToNetwork()) {
                NetworkDialogManager.networkDialog(requireActivity(), "RegionActivity")
            } else {
                viewModel.getDistrictFromAPI(provinceId)
        }

        init()
        search()
    }


    private fun search() {

        binding?.districtSearchEdt?.onChange { districtName ->

            districtAdapter.filter(districtName)
        }
    }

    private fun observeDistrict(response: ArrayList<District>) {

        response?.let { it ->

            districtAdapter = DistrictAdapter(viewModel, it)
            binding?.districtRecycler?.layoutManager = layoutManager
            binding?.districtRecycler?.addItemDecoration(
                DividerItemDecoration(
                    context,
                    layoutManager.orientation
                )
            )
            binding?.districtRecycler?.adapter = districtAdapter
        }

    }

    private fun observePrayTimes(prayTimes: ArrayList<PrayTimes>) {

        prayTimes?.let {

            if (prayTimeDao.getPrayTimeItemCount() != 0) {
                prayTimeDao.deletePrayTime()
            }

            viewModel.setPrayTimesToDB(prayTimes, districtId)
            prefs.put(PROVINCE_NAME, "$provinceName")
            prefs.put(Constant.CACHE_CLEARED, true)
        }
    }

    private fun init() {

        layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)

        val districtId = prefs.get(DISTRICT_ID,0)

        if (districtId == 0) {
            prefs.put(CREATED_ALARM, false)
        } else {
            prefs.put(CREATED_ALARM, true)
        }
    }
}