package com.namazvakitleri.internetsiz.ui.base

import android.os.Bundle
import android.view.Window
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.viewbinding.ViewBinding

abstract class BaseActivity <VB: ViewBinding>: AppCompatActivity() {

    var binding: VB? = null

    abstract fun viewBinding(): VB

    abstract fun observeLiveData()


    override fun onCreate(savedInstanceState: Bundle?) {

        requestWindowFeature(Window.FEATURE_NO_TITLE)
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )

        super.onCreate(savedInstanceState)

        binding = viewBinding()

        setContentView(binding?.root)
    }

    override fun onStart() {
        super.onStart()
        observeLiveData()
    }

    override fun onDestroy() {
        super.onDestroy()


        binding = null
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finishAffinity()
    }
}