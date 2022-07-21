package com.namazvakitleri.internetsiz.ui.fragment.dashboard

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.namazvakitleri.internetsiz.R
import com.namazvakitleri.internetsiz.databinding.FragmentDashboardBinding
import com.namazvakitleri.internetsiz.ui.base.BaseFragment
import com.namazvakitleri.internetsiz.ui.fragment.settings.SettingsFragment
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class DashboardFragment @Inject constructor(): BaseFragment<FragmentDashboardBinding>() {


    override val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> FragmentDashboardBinding
            = FragmentDashboardBinding::inflate

    override fun observeViewModel() {
    }

    private val viewModel: DashboardViewModel by viewModels()
    @Inject lateinit var settingsFragment: SettingsFragment

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        requireFragmentManager().beginTransaction()
            .add(R.id.dashboardContainer, settingsFragment)
            .commit()
    }
}