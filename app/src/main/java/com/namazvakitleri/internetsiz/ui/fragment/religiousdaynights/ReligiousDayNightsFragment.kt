package com.namazvakitleri.internetsiz.ui.fragment.religiousdaynights

import android.content.SharedPreferences
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.namazvakitleri.internetsiz.R
import com.namazvakitleri.internetsiz.adapter.recycler.ReligiousDayNightsAdapter
import com.namazvakitleri.internetsiz.databinding.FragmentReligiousDayNightsBinding
import com.namazvakitleri.internetsiz.db.ReligiousDayNights
import com.namazvakitleri.internetsiz.ui.base.BaseFragment
import com.namazvakitleri.internetsiz.utils.constant.ConstPrefs.DISTRICT_NAME
import com.namazvakitleri.internetsiz.utils.constant.ConstPrefs.PROVINCE_NAME
import com.namazvakitleri.internetsiz.utils.extension.get
import com.namazvakitleri.internetsiz.utils.extension.observe
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject


private const val originalColor = "#D7DDD7"
private const val clickColor = "#819C81"

@AndroidEntryPoint
class ReligiousDayNightsFragment :
    BaseFragment<FragmentReligiousDayNightsBinding>(),
    View.OnClickListener {

    override val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> FragmentReligiousDayNightsBinding
        = FragmentReligiousDayNightsBinding::inflate

    override fun observeViewModel() {

        observe(viewModel.religiousDaysNightsList, ::setAdapter)
    }


    private val viewModel: ReligiousDayNightsViewModel by viewModels()

    @Inject lateinit var prefs: SharedPreferences
    @Inject lateinit var layoutManager: LinearLayoutManager

    private lateinit var religiousDaysNightsAdapter: ReligiousDayNightsAdapter
    private lateinit var provinceName: String
    private lateinit var districtName: String


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        init()
        getReligiousDayNight(2021)


        binding?.firstYearBtn?.setOnClickListener(this)
        binding?.secondYearBtn?.setOnClickListener(this)
    }

    private fun init() {

        binding?.firstYearBtn?.setBackgroundColor(Color.parseColor(clickColor))

        districtName = prefs.get(DISTRICT_NAME, "null")
        provinceName = prefs.get(PROVINCE_NAME, "null")
        binding?.distProvNameReligiousTxt?.text = "$provinceName - $districtName"
    }


    override fun onClick(v: View?) {

        when (v?.id) {

                R.id.firstYearBtn -> {
                    getReligiousDayNight(2021)
                }

                R.id.secondYearBtn -> {
                    getReligiousDayNight(2022)
                }
        }
    }

    private fun setAdapter(religiousDaysNightsList: List<ReligiousDayNights>) {
        religiousDaysNightsAdapter =
            ReligiousDayNightsAdapter(religiousDaysNightsList)
        binding?.religiousDaysNightsRecycler?.layoutManager = layoutManager
        binding?.religiousDaysNightsRecycler?.adapter = religiousDaysNightsAdapter
    }



    private fun getReligiousDayNight(year: Int) {

        if (year == 2021)
        {

            viewModel.getReligiousDayNights(2021)
            binding?.secondYearBtn?.setBackgroundColor(Color.WHITE)
            binding?.secondYearBtn?.setTextColor(Color.parseColor("#26444E"))
            binding?.firstYearBtn?.setBackgroundColor(Color.parseColor("#26444E"))
            binding?.firstYearBtn?.setTextColor(Color.WHITE)

        }
        else
        {
            viewModel.getReligiousDayNights(2022)
            binding?.firstYearBtn?.setBackgroundColor(Color.WHITE)
            binding?.firstYearBtn?.setTextColor(Color.parseColor("#26444E"))
            binding?.secondYearBtn?.setBackgroundColor(Color.parseColor("#26444E"))
            binding?.secondYearBtn?.setTextColor(Color.WHITE)
        }
    }


}
