package com.namazvakitleri.internetsiz.ui.base

import androidx.lifecycle.ViewModel
import com.namazvakitleri.internetsiz.manager.LoadingBarManager

open abstract class BaseViewModel : ViewModel() {

    fun setLoading(isVisible: Boolean) {
        LoadingBarManager.visibility(isVisible)

    }
}