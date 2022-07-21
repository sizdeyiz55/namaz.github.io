package com.namazvakitleri.internetsiz.ui.fragment.salarypraytime

import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.namazvakitleri.internetsiz.R
import com.namazvakitleri.internetsiz.adapter.recycler.SalaryPrayTimesAdapter
import com.namazvakitleri.internetsiz.manager.LoadingBarManager
import com.namazvakitleri.internetsiz.databinding.FragmentSalaryPrayTimeBinding
import com.namazvakitleri.internetsiz.modal.PrayTimes
import com.namazvakitleri.internetsiz.ui.base.BaseFragment
import com.namazvakitleri.internetsiz.utils.constant.ConstPrefs.DISTRICT_ID
import com.namazvakitleri.internetsiz.utils.constant.ConstPrefs.DISTRICT_NAME
import com.namazvakitleri.internetsiz.utils.constant.ConstPrefs.PROVINCE_NAME
import com.namazvakitleri.internetsiz.utils.constant.Constant
import com.namazvakitleri.internetsiz.utils.extension.get
import com.namazvakitleri.internetsiz.utils.extension.observe
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class SalaryPrayTimeFragment : BaseFragment<FragmentSalaryPrayTimeBinding>(), View.OnClickListener {

    override val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> FragmentSalaryPrayTimeBinding
        = FragmentSalaryPrayTimeBinding::inflate

    override fun observeViewModel() {

        observe(viewModel.prayTimes, ::observePrayTimes)

    }

    private val viewModel:  SalaryPrayTimeViewModel by viewModels()

    private var districtId: Int = 0
    private lateinit var districtName: String
    private lateinit var provinceName: String
    private lateinit var salaryPrayTimeAdapter: SalaryPrayTimesAdapter

    @Inject lateinit var prefs: SharedPreferences
    @Inject lateinit var layoutManager: LinearLayoutManager

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        LoadingBarManager.build(requireContext(), "Aylık namaz vakitleri ${Constant.LOAD}")
        init()
        viewModel.getPrayTimeAPI(districtId)

        //binding?.salaryPrayTimesBackBtn?.setOnClickListener(this)
    }


    private fun init() {


        districtId = prefs.get(DISTRICT_ID,0)
        districtName = prefs.get(DISTRICT_NAME, "null")
        provinceName = prefs.get(PROVINCE_NAME, "null")

        binding?.distProvNameSalaryTxt?.text = "$provinceName - $districtName"

    }

    override fun onClick(view: View?) {

       /*when (view?.id) {
            R.id.salaryPrayTimesBackBtn -> {
                fragmentManager?.popBackStack()
            }
        }*/
    }


    private fun observePrayTimes(prayTimes: ArrayList<PrayTimes>) {

        prayTimes?.let {

            salaryPrayTimeAdapter = SalaryPrayTimesAdapter(prayTimes)
            binding?.salaryPrayTimesRecycler?.layoutManager = layoutManager
            binding?.salaryPrayTimesRecycler?.adapter = salaryPrayTimeAdapter
        }
    }
}