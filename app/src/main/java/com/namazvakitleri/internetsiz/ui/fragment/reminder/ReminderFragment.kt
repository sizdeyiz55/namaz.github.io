package com.namazvakitleri.internetsiz.ui.fragment.reminder

import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.namazvakitleri.internetsiz.R
import com.namazvakitleri.internetsiz.databinding.FragmentReminderBinding
import com.namazvakitleri.internetsiz.dialog.NotificationSoundDialog
import com.namazvakitleri.internetsiz.manager.LoadingBarManager
import com.namazvakitleri.internetsiz.manager.ReminderPrayTimesManager
import com.namazvakitleri.internetsiz.ui.base.BaseFragment
import com.namazvakitleri.internetsiz.ui.fragment.home.HomeFragment
import com.namazvakitleri.internetsiz.utils.constant.ConstPrefs.REMINDER_SETTINGS_SAVED
import com.namazvakitleri.internetsiz.utils.constant.Constant
import com.namazvakitleri.internetsiz.utils.constant.Constant.AKSAM_MINUTE_AGO_VALUE
import com.namazvakitleri.internetsiz.utils.constant.Constant.AKSAM_REMINDER
import com.namazvakitleri.internetsiz.utils.constant.Constant.GUNES_MINUTE_AGO_VALUE
import com.namazvakitleri.internetsiz.utils.constant.Constant.GUNES_REMINDER
import com.namazvakitleri.internetsiz.utils.constant.Constant.IKINDI_MINUTE_AGO_VALUE
import com.namazvakitleri.internetsiz.utils.constant.Constant.IKINDI_REMINDER
import com.namazvakitleri.internetsiz.utils.constant.Constant.IMSAK_MINUTE_AGO_VALUE
import com.namazvakitleri.internetsiz.utils.constant.Constant.IMSAK_REMINDER
import com.namazvakitleri.internetsiz.utils.constant.Constant.OGLE_MINUTE_AGO_VALUE
import com.namazvakitleri.internetsiz.utils.constant.Constant.OGLE_REMINDER
import com.namazvakitleri.internetsiz.utils.constant.Constant.YATSI_MINUTE_AGO_VALUE
import com.namazvakitleri.internetsiz.utils.constant.Constant.YATSI_REMINDER
import com.namazvakitleri.internetsiz.utils.extension.action
import com.namazvakitleri.internetsiz.utils.extension.get
import com.namazvakitleri.internetsiz.utils.extension.put
import com.namazvakitleri.internetsiz.utils.extension.snack
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject


@AndroidEntryPoint
class ReminderFragment : BaseFragment<FragmentReminderBinding>(), /*CompoundButton.OnCheckedChangeListener,*/ View.OnClickListener {


    override val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> FragmentReminderBinding
        = FragmentReminderBinding::inflate

    override fun observeViewModel() {

    }

    private val viewModel: ReminderViewModel by viewModels()

    private var notificationSoundDialog: NotificationSoundDialog? = null

    private var imsakMinuteAgoValue: Int = -1
    private var gunesMinuteAgoValue: Int = -1
    private var ogleMinuteAgoValue: Int = -1
    private var ikindiMinuteAgoValue: Int = -1
    private var aksamMinuteAgoValue: Int = -1
    private var yatsiMinuteAgoValue: Int = -1


    @Inject lateinit var prefs: SharedPreferences
    @Inject lateinit var reminderPrayTimesManager: ReminderPrayTimesManager

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        LoadingBarManager.build(requireContext(), "${Constant.TRANSACTION}")
        setSwitchCheckValue()
        setSelectedRemainingTimeValue()


        binding?.saveBtn?.setOnClickListener(this)
        binding?.notificationSoundCv?.setOnClickListener(this)
    }


    private fun setSwitchCheckValue() {
        binding?.imsakSw?.isChecked = prefs.get(
            Constant.IMSAK_REMINDER,
            true
        )

        binding?.gunesSw?.isChecked = prefs.get(
            Constant.GUNES_REMINDER,
            true
        )

        binding?.ogleSw?.isChecked = prefs.get(
            Constant.OGLE_REMINDER,
            true
        )

        binding?.ikindiSw?.isChecked = prefs.get(
            Constant.IKINDI_REMINDER,
            true
        )

        binding?.aksamSw?.isChecked = prefs.get(
            Constant.AKSAM_REMINDER,
            true
        )

        binding?.yatsiSw?.isChecked = prefs.get(
            Constant.YATSI_REMINDER,
            true
        )
    }


    /*override fun onCheckedChanged(switch: CompoundButton?, isChecked: Boolean) {

        when(switch?.id) {
            R.id.imsak_sw -> {
                prefs.put(IMSAK_REMINDER,isChecked)
            }
            R.id.gunes_sw -> {
                prefs.put(GUNES_REMINDER, isChecked)
            }
            R.id.ogle_sw -> {
                prefs.put(OGLE_REMINDER, isChecked)
            }
            R.id.ikindi_sw -> {
                prefs.put(IKINDI_REMINDER, isChecked)
            }
            R.id.aksam_sw -> {
                prefs.put(AKSAM_REMINDER, isChecked)
            }
            R.id.yatsi_sw -> {
                prefs.put(YATSI_REMINDER, isChecked)
            }
        }
    }*/


    override fun onClick(v: View?) {
        when(v?.id) {
            R.id.notification_sound_cv -> {
                notificationSoundDialog = NotificationSoundDialog()
                notificationSoundDialog?.arguments = null
                notificationSoundDialog?.show(parentFragmentManager,"NotificationSoundDialog")
            }

            R.id.save_btn -> {
                setReminderSwState()
                saveSelectedRemainingTimeValue()
                reminderPrayTimesManager.start(0)
                showSnackMessage(v)
                prefs.put(REMINDER_SETTINGS_SAVED, true)
                fragmentManager?.beginTransaction()?.replace(R.id.frame_contInMain, HomeFragment())?.commit()
                val view = activity?.findViewById<BottomNavigationView>(R.id.nav_view)
                view?.selectedItemId = R.id.navigation_home
            }
        }
    }

    private fun showSnackMessage(view: View) {
        view.snack("Ayarlar başarıyla kaydedildi."){
            action("Kapat") {
                dismiss()
            }
        }
    }

    private fun setReminderSwState() {
        prefs.put(IMSAK_REMINDER, binding?.imsakSw?.isChecked)
        prefs.put(GUNES_REMINDER, binding?.gunesSw?.isChecked)
        prefs.put(OGLE_REMINDER, binding?.ogleSw?.isChecked)
        prefs.put(IKINDI_REMINDER, binding?.ikindiSw?.isChecked)
        prefs.put(AKSAM_REMINDER, binding?.aksamSw?.isChecked)
        prefs.put(YATSI_REMINDER, binding?.yatsiSw?.isChecked)

    }



    private fun saveSelectedRemainingTimeValue() {

        prefs.put(IMSAK_MINUTE_AGO_VALUE, binding?.imsakSpn?.selectedItemId?.toInt())
        prefs.put(GUNES_MINUTE_AGO_VALUE, binding?.gunesSpn?.selectedItemId?.toInt())
        prefs.put(OGLE_MINUTE_AGO_VALUE, binding?.ogleSpn?.selectedItemId?.toInt())
        prefs.put(IKINDI_MINUTE_AGO_VALUE, binding?.ikindiSpn?.selectedItemId?.toInt())
        prefs.put(AKSAM_MINUTE_AGO_VALUE, binding?.aksamSpn?.selectedItemId?.toInt())
        prefs.put(YATSI_MINUTE_AGO_VALUE, binding?.yatsiSpn?.selectedItemId?.toInt())
    }

    private fun setSelectedRemainingTimeValue() {

        imsakMinuteAgoValue = prefs.get(
            IMSAK_MINUTE_AGO_VALUE,0).toInt()
        gunesMinuteAgoValue = prefs.get(
            GUNES_MINUTE_AGO_VALUE,0).toInt()
        ogleMinuteAgoValue = prefs.get(
            OGLE_MINUTE_AGO_VALUE,0).toInt()
        ikindiMinuteAgoValue = prefs.get(
            IKINDI_MINUTE_AGO_VALUE,0).toInt()
        aksamMinuteAgoValue = prefs.get(
            AKSAM_MINUTE_AGO_VALUE,0).toInt()
        yatsiMinuteAgoValue = prefs.get(
            YATSI_MINUTE_AGO_VALUE,0).toInt()


        binding?.imsakSpn?.setSelection(imsakMinuteAgoValue)
        binding?.gunesSpn?.setSelection(gunesMinuteAgoValue)
        binding?.ogleSpn?.setSelection(ogleMinuteAgoValue)
        binding?.ikindiSpn?.setSelection(ikindiMinuteAgoValue)
        binding?.aksamSpn?.setSelection(aksamMinuteAgoValue)
        binding?.yatsiSpn?.setSelection(yatsiMinuteAgoValue)

    }
}