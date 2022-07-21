package com.namazvakitleri.internetsiz.ui.activity.region

import android.Manifest
import android.content.Context
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.graphics.Color
import android.location.*
import android.os.Bundle
import android.util.AttributeSet
import android.view.View
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.material.snackbar.Snackbar
import com.namazvakitleri.internetsiz.R
import com.namazvakitleri.internetsiz.databinding.ActivityRegionBinding
import com.namazvakitleri.internetsiz.main.MainActivity
import com.namazvakitleri.internetsiz.ui.base.BaseActivity
import com.namazvakitleri.internetsiz.ui.fragment.LocationPandingFragment
import com.namazvakitleri.internetsiz.ui.fragment.region.country.CountryFragment
import com.namazvakitleri.internetsiz.utils.constant.ConstPrefs
import com.namazvakitleri.internetsiz.utils.constant.ConstPrefs.CANCEL_REGION_BTN
import com.namazvakitleri.internetsiz.utils.extension.*
import dagger.hilt.android.AndroidEntryPoint
import java.util.*
import javax.inject.Inject


@AndroidEntryPoint
class RegionActivity : BaseActivity<ActivityRegionBinding>(), View.OnClickListener {

    override fun viewBinding(): ActivityRegionBinding = ActivityRegionBinding.inflate(layoutInflater)

    private val viewModel: RegionViewModel by viewModels()

    override fun observeLiveData() {

    }

    @Inject lateinit var prefs: SharedPreferences

    private var izinkontrol = 0
    private lateinit var locationManager: LocationManager
    private lateinit var locationListener: LocationListener

    private var visibleBtn : Boolean = false


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (savedInstanceState == null) {
            addFragment(LocationPandingFragment(), R.id.fragmentContainer)
            //addFragment(CountryFragment(), R.id.fragmentContainer)
        }

        //locationManager = this.getSystemService(Context.LOCATION_SERVICE) as LocationManager

        //izinkontrol = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)


        cancelBtnVisibility()
        binding?.cancelRegionBtn?.setOnClickListener(this)

    }

    private fun cancelBtnVisibility() {

        /*if (visibleBtn) {
            binding?.cancelRegionBtn?.visibility = View.VISIBLE
        } else {

            binding?.cancelRegionBtn?.visibility = View.INVISIBLE

        }*/

    }

    override fun onClick(view: View?) {

        when(view?.id) {
            R.id.cancelRegionBtn -> openActivity(MainActivity::class.java)
        }
    }
}
