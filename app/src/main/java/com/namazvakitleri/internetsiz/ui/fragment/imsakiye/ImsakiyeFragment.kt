package com.namazvakitleri.internetsiz.ui.fragment.imsakiye

import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.namazvakitleri.internetsiz.R
import com.namazvakitleri.internetsiz.adapter.recycler.ImsakiyeAdapter
import com.namazvakitleri.internetsiz.databinding.FragmentImsakiyeBinding
import com.namazvakitleri.internetsiz.manager.LoadingBarManager
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
class ImsakiyeFragment : BaseFragment<FragmentImsakiyeBinding>(), View.OnClickListener {


    override val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> FragmentImsakiyeBinding =
        FragmentImsakiyeBinding::inflate

    override fun observeViewModel() {

        observe(viewModel.prayTimes, ::observePrayTimes)
    }

    private val viewModel: ImsakiyeViewModel by viewModels()
    @Inject lateinit var prefs: SharedPreferences
    @Inject lateinit var layoutManager: LinearLayoutManager


    private var districtId: Int = -1
    private lateinit var districtName: String
    private lateinit var provinceName: String
    private lateinit var imsakiyeAdapter: ImsakiyeAdapter


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        LoadingBarManager.build(requireContext(), "İmsakiye ${Constant.LOAD}")
        init()
        viewModel.getPrayTimeAPI(districtId)


        //binding?.imsakiyeBackBtn?.setOnClickListener(this)
    }


    private fun init() {


        districtId = prefs.get(DISTRICT_ID, 0)
        districtName = prefs.get(DISTRICT_NAME, "null")
        provinceName = prefs.get(PROVINCE_NAME, "null")

        binding?.distProvNameImsakiyeTxt?.text = "$provinceName - $districtName"
    }


    override fun onClick(view: View?) {

        /*when (view?.id) {
            R.id.imsakiyeBackBtn -> {
                fragmentManager?.popBackStack()
            }
        }*/
    }


    private fun observePrayTimes(prayTimes: ArrayList<PrayTimes>) {

        prayTimes?.let {
            imsakiyeAdapter = ImsakiyeAdapter(prayTimes)
            binding?.imsakiyeRecycler?.layoutManager = layoutManager
            binding?.imsakiyeRecycler?.adapter = imsakiyeAdapter
        }
    }

}

