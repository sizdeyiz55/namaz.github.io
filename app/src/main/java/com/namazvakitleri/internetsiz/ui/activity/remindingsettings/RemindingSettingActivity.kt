package com.namazvakitleri.internetsiz.ui.activity.remindingsettings

import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.view.View
import androidx.activity.viewModels
import com.namazvakitleri.internetsiz.R
import com.namazvakitleri.internetsiz.main.MainActivity
import com.namazvakitleri.internetsiz.databinding.ActivityRemindingSettingBinding
import com.namazvakitleri.internetsiz.ui.base.BaseActivity
import com.namazvakitleri.internetsiz.utils.extension.openActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RemindingSettingActivity : BaseActivity<ActivityRemindingSettingBinding>(), View.OnClickListener{

    override fun viewBinding(): ActivityRemindingSettingBinding = ActivityRemindingSettingBinding.inflate(layoutInflater)

    private val viewModel: RemindingSettingsViewModel by viewModels()

    override fun observeLiveData() {

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding?.remindingSettingBackBtn?.setOnClickListener(this)
        binding?.goSettingsCV?.setOnClickListener(this)
    }

    override fun onBackPressed() {
        super.onBackPressed()
        openActivity(MainActivity::class.java)
    }

    override fun onClick(view: View?) {

        when(view?.id) {
            R.id.remindingSettingBackBtn -> openActivity(MainActivity::class.java)
            R.id.goSettingsCV -> startActivityForResult(Intent(Settings.ACTION_APPLICATION_SETTINGS), 0)
        }
    }

}