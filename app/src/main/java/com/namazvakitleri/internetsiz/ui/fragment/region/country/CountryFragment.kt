package com.namazvakitleri.internetsiz.ui.fragment.region.country

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.*
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.namazvakitleri.internetsiz.R
import com.namazvakitleri.internetsiz.adapter.recycler.CountryAdapter
import com.namazvakitleri.internetsiz.databinding.FragmentCountryBinding
import com.namazvakitleri.internetsiz.manager.LoadingBarManager
import com.namazvakitleri.internetsiz.manager.NetworkDialogManager
import com.namazvakitleri.internetsiz.modal.Country
import com.namazvakitleri.internetsiz.ui.base.BaseFragment
import com.namazvakitleri.internetsiz.utils.constant.Constant
import com.namazvakitleri.internetsiz.utils.extension.isConnectedToNetwork
import com.namazvakitleri.internetsiz.utils.extension.observe
import com.namazvakitleri.internetsiz.utils.extension.onChange
import dagger.hilt.android.AndroidEntryPoint
import java.util.*

/**
 * suspend fonksiyonlar ya Coroutine icinde
 * ya da
 * suspend fonksiyonlar icinde cagirabilir.
 *
 *  Dispatchers.Main : Ui thread'ler icin kullaniliyor.
 */

@AndroidEntryPoint
class CountryFragment : BaseFragment<FragmentCountryBinding>() {

    override val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> FragmentCountryBinding
            = FragmentCountryBinding::inflate

    override fun observeViewModel() {

        observe(viewModel.countryData, ::observeCountry)
    }

    private val viewModel: CountryViewModel by viewModels()

    private var countryList = ArrayList<Country>()

    private lateinit var countryAdapter: CountryAdapter
    private lateinit var layoutManager: LinearLayoutManager

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        LoadingBarManager.build(requireContext(), "Ülkeler ${Constant.LOAD}")

        if (!requireContext().isConnectedToNetwork()) {

            NetworkDialogManager.networkDialog(requireActivity(),"RegionActivity")
        } else {

            viewModel.getCountryFromAPI()
        }

        init()
        search()
    }

    private fun init() {

        layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
    }

    private fun observeCountry(response: List<Country>) {

             response?.let { it ->

                countryList = ArrayList()
                countryList.add(Country("TÜRKİYE", "TURKEY", 2))
                countryList.addAll(it)

                countryAdapter = CountryAdapter(countryList)
                binding?.countryRecycler?.layoutManager = layoutManager
                binding?.countryRecycler?.addItemDecoration(
                     DividerItemDecoration(
                         context,
                         layoutManager.orientation
                     )
                 )
                binding?.countryRecycler?.adapter = countryAdapter
            }
    }


    private fun search() {

            binding?.countrySearchEdt?.onChange { countryName ->

                countryAdapter.filter(countryName)
            }
    }
}
