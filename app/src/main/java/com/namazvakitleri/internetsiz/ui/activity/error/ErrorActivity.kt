package com.namazvakitleri.internetsiz.ui.activity.error

import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import com.namazvakitleri.internetsiz.R
import com.namazvakitleri.internetsiz.databinding.ActivityErrorBinding
import com.namazvakitleri.internetsiz.ui.activity.splash.SplashActivity
import com.namazvakitleri.internetsiz.ui.base.BaseActivity
import com.namazvakitleri.internetsiz.utils.extension.openActivity

class ErrorActivity : BaseActivity<ActivityErrorBinding>(), View.OnClickListener{

    override fun viewBinding(): ActivityErrorBinding = ActivityErrorBinding.inflate(layoutInflater)

    private val viewModel: ErrorViewModel by viewModels()


    override fun observeLiveData() {

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding?.restartBtn?.setOnClickListener(this)
    }

    override fun onClick(view: View?) {

        when(view?.id) {
            R.id.restartBtn -> openActivity(SplashActivity::class.java)
        }
    }
}