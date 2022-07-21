package com.namazvakitleri.internetsiz.ui.fragment.region.province

import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.namazvakitleri.internetsiz.adapter.recycler.ProvinceAdapter
import com.namazvakitleri.internetsiz.manager.LoadingBarManager
import com.namazvakitleri.internetsiz.manager.NetworkDialogManager
import com.namazvakitleri.internetsiz.modal.Province
import com.namazvakitleri.internetsiz.databinding.FragmentProvinceBinding
import com.namazvakitleri.internetsiz.ui.base.BaseFragment
import com.namazvakitleri.internetsiz.utils.constant.Constant
import com.namazvakitleri.internetsiz.utils.extension.isConnectedToNetwork
import com.namazvakitleri.internetsiz.utils.extension.observe
import com.namazvakitleri.internetsiz.utils.extension.onChange
import com.namazvakitleri.internetsiz.utils.extension.put
import dagger.hilt.android.AndroidEntryPoint
import java.util.*
import javax.inject.Inject

private const val COUNTRY_ID = "CountryId"
private const val COUNTRY_NAME = "CountryName"

@AndroidEntryPoint
class ProvinceFragment @Inject constructor() : BaseFragment<FragmentProvinceBinding>() {

    override val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> FragmentProvinceBinding
        = FragmentProvinceBinding::inflate

    override fun observeViewModel() {

        observe(viewModel.provinceData, ::observeProvince)
    }

    private val viewModel: ProvinceViewModel by viewModels()

    @Inject lateinit var layoutManager: LinearLayoutManager
    @Inject lateinit var prefs: SharedPreferences

    private var countryId: Int = -1
    private var countryName: String = ""
    private lateinit var provinceAdapter: ProvinceAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            countryId = it.getInt(COUNTRY_ID)
            countryName = it.getString(COUNTRY_NAME).toString()
        }
        prefs.put("CountryLower",countryName.lowercase())

        LoadingBarManager.build(requireContext(), "İller ${Constant.LOAD}")
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (!requireContext().isConnectedToNetwork()) {

            NetworkDialogManager.networkDialog(requireActivity(), "RegionActivity")
        } else {

            viewModel.getProvinceFromAPI(countryId)
        }

        init()
        search()
    }


    private fun init() {

        layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
    }


    private fun search() {

        binding?.provinceSearchEdt?.onChange { provinceName ->

            provinceAdapter.filter(provinceName)
        }
    }


    private fun observeProvince(response: ArrayList<Province>) {

            response?.let { provinceList ->

                provinceAdapter = ProvinceAdapter(provinceList)
                binding?.provinceRecycler?.layoutManager = layoutManager
                binding?.provinceRecycler?.addItemDecoration(
                    DividerItemDecoration(
                        context,
                        layoutManager.orientation
                    )
                )
                binding?.provinceRecycler?.adapter = provinceAdapter
            }
    }
}
