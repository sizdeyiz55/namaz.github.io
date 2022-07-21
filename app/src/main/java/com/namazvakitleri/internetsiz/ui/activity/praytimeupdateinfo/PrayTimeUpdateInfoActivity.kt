package com.namazvakitleri.internetsiz.ui.activity.praytimeupdateinfo

import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import com.namazvakitleri.internetsiz.R
import com.namazvakitleri.internetsiz.databinding.ActivityPrayTimeUpdateInfoBinding
import com.namazvakitleri.internetsiz.ui.activity.splash.SplashActivity
import com.namazvakitleri.internetsiz.ui.base.BaseActivity
import com.namazvakitleri.internetsiz.utils.extension.openActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PrayTimeUpdateInfoActivity : BaseActivity<ActivityPrayTimeUpdateInfoBinding>(), View.OnClickListener {

    override fun viewBinding() = ActivityPrayTimeUpdateInfoBinding.inflate(layoutInflater)

    private val viewModel: PrayTimeUpdateInfoViewModel by viewModels()

    override fun observeLiveData() {

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding?.updatePrayTimeBtn?.setOnClickListener(this)
    }

    override fun onClick(view: View?) {

        when(view?.id) {
            R.id.updatePrayTimeBtn -> openActivity(SplashActivity::class.java)

        }
    }
}