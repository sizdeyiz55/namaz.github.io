package com.namazvakitleri.internetsiz.transactions

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import com.irozon.alertview.AlertActionStyle
import com.irozon.alertview.AlertStyle
import com.irozon.alertview.AlertView
import com.irozon.alertview.objects.AlertAction
import com.namazvakitleri.internetsiz.utils.constant.Constant

class ShowAlert {

    companion object {

        fun showAlert(context: AppCompatActivity, message: String, actionTitle: String) {

            val alert = AlertView("${Constant.APP_NAME}", "$message", AlertStyle.DIALOG)
            alert.addAction(AlertAction(actionTitle, AlertActionStyle.POSITIVE, {

                goToPlayStore(context)
            }))
            alert.addAction(AlertAction("Vazgeç", AlertActionStyle.NEGATIVE, {


            }))

            alert.show(context)
        }

        private fun goToPlayStore(context: Context) {

            val packagaName = context.packageName

            try {
                context.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(
                    "market://detals?id=$packagaName")
                ))
            } catch (anfe: ActivityNotFoundException) {
                context.startActivity(Intent(Intent.ACTION_VIEW,
                Uri.parse("https://ply.google.com/store/apps/details?id=$packagaName")))
            }
        }


        fun showReminderInfo(context: AppCompatActivity, message: String, actionTitle: String) {

            val alert = AlertView("${Constant.APP_NAME}", "$message", AlertStyle.DIALOG)
            alert.addAction(AlertAction(actionTitle, AlertActionStyle.POSITIVE, {


            }))

            alert.show(context)
        }
    }
}