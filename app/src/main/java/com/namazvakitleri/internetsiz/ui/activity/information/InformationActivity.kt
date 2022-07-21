package com.namazvakitleri.internetsiz.ui.activity.information

import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import com.namazvakitleri.internetsiz.Application
import com.namazvakitleri.internetsiz.R
import com.namazvakitleri.internetsiz.databinding.ActivityInformationBinding
import com.namazvakitleri.internetsiz.ui.activity.region.RegionActivity
import com.namazvakitleri.internetsiz.ui.base.BaseActivity
import com.namazvakitleri.internetsiz.utils.constant.ConstPrefs
import com.namazvakitleri.internetsiz.utils.constant.Constant.CACHE_CLEARED
import com.namazvakitleri.internetsiz.utils.constant.Constant.INFORMATION
import com.namazvakitleri.internetsiz.utils.extension.get
import com.namazvakitleri.internetsiz.utils.extension.openActivity
import com.namazvakitleri.internetsiz.utils.extension.put
import dagger.hilt.android.AndroidEntryPoint
import java.lang.Exception
import javax.inject.Inject

@AndroidEntryPoint
class InformationActivity : BaseActivity<ActivityInformationBinding>(), View.OnClickListener{

    override fun viewBinding() = ActivityInformationBinding.inflate(layoutInflater)

    private val viewModel: InformationViewModel by viewModels()

    override fun observeLiveData() {}

    @Inject lateinit var prefs: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        checked()
        binding?.continueBtn?.setOnClickListener(this)
    }

    private fun checked() {
        binding?.checkBox?.setOnCheckedChangeListener { _, isChecked ->

            binding?.continueBtn?.isEnabled = isChecked

            if (isChecked) {
                binding?.continueBtn?.background = Application.context.resources.getDrawable(R.drawable.custom_button_info)
            } else{
                binding?.continueBtn?.background = Application.context.resources.getDrawable(R.drawable.custom_button_info_enabled)
            }
        }
    }

    override fun onClick(view: View?) {

        when(view?.id) {
            R.id.continueBtn -> {
                deleteCacheData()
                prefs.put(INFORMATION, true)
                openActivity(RegionActivity::class.java)
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
            }
        }
    }


    private fun deleteCacheData() {

         if(prefs.get(CACHE_CLEARED, false)) {
             return
         }

         applicationContext.cacheDir.deleteRecursively()

        try {

            var runTime = Runtime.getRuntime()
            runTime.exec(applicationContext.packageName)

        } catch (exp: Exception) {

        }
    }
}