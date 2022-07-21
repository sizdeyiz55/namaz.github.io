package com.namazvakitleri.internetsiz.manager

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import com.namazvakitleri.internetsiz.Application
import com.namazvakitleri.internetsiz.R

class LoadingBarManager {
    companion object{

        private lateinit var alertDialog: AlertDialog
        private lateinit var builder: AlertDialog.Builder

        private fun load(layoutInflater: LayoutInflater?, message: String?, context: Context): AlertDialog.Builder {

            val promptView: View = layoutInflater!!.inflate(R.layout.loading_bar, null)
            var builder = AlertDialog.Builder(context)
            builder.setCancelable(false)
            builder.setView(promptView)
            var messageTxt: TextView = promptView.findViewById(R.id.message_txt)
            messageTxt.text = message

            return builder
        }

        fun build(context: Context, message: String) {

            builder = load(Application.layoutInflater, "$message", context)
            alertDialog = builder.create()
        }

        fun visibility(inVisible: Boolean) {

            if (inVisible) {

                alertDialog.show()
                alertDialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
            }
            else {
                if (alertDialog.isShowing) {
                    alertDialog.dismiss()
                }
            }
        }
    }
}