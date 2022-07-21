package com.namazvakitleri.internetsiz.manager

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AlertDialog
import com.namazvakitleri.internetsiz.Application
import com.namazvakitleri.internetsiz.R
import com.namazvakitleri.internetsiz.ui.activity.region.RegionActivity
import com.namazvakitleri.internetsiz.utils.extension.isConnectedToNetwork


class NetworkDialogManager {

    companion object {
        fun networkDialog(context: Context, whereFrom: String? = null) {
            AlertDialog.Builder(context)
                .setIcon(R.drawable.icon_no_wifi)
                .setCancelable(false)
                .setTitle("Bağlantı Hatası!")
                .setMessage("Lütfen internet bağlantınızı kontrol edip tekrar deneyiniz...")
                .setPositiveButton(
                    "Kapat"
                ) { _, _ -> (context as Activity).finishAffinity() }
                .setNegativeButton(
                    "Tekrar Dene"
                ) { dialog, _ ->
                    // tekrar dene butonuna basdiginda internet kontrolu yapilir. Eger internet geldiyse dialog bar'i kapatir.
                    if (!Application.context.isConnectedToNetwork()) { // internet yoksa.
                        if(whereFrom == "RegionActivity") {
                            networkDialog(context, "RegionActivity")
                        } else {
                            networkDialog(context)
                        }
                    } else {
                        dialog.dismiss()
                        if(whereFrom == "RegionActivity") {

                            val intent = Intent(context, RegionActivity::class.java)
                            (context as Activity).finish()
                            (context).startActivity(intent)
                            (context).overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right)
                        }
                    }
                }.show()
        }
    }
}