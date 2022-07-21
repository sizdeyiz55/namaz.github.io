package com.namazvakitleri.internetsiz.ui.base

import android.os.Bundle
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding

abstract class BaseFragment <VB: ViewBinding>: Fragment() {

    var binding: VB? = null

    abstract val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> VB

    abstract fun observeViewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)



    }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = bindingInflater.invoke(layoutInflater, container, false)

        return requireNotNull(binding).root
    }

    override fun onStart() {
        super.onStart()

        observeViewModel()
    }

    override fun onDestroy() {
        super.onDestroy()

        binding = null
    }

    override fun onResume() {
        super.onResume()

        if (view == null)
            return

        view?.isFocusableInTouchMode = true
        view?.requestFocus()
        view?.setOnKeyListener(object : View.OnKeyListener {
            override fun onKey(view: View, keyCode: Int, event: KeyEvent): Boolean {
                if (event.action === KeyEvent.ACTION_UP && keyCode == KeyEvent.KEYCODE_BACK) {

                    if (fragmentManager?.backStackEntryCount == 0) {
                        activity?.finishAffinity()
                    } else {
                        fragmentManager?.popBackStack()
                    }

                    return true
                }
                return false
            }
        })
    }
}