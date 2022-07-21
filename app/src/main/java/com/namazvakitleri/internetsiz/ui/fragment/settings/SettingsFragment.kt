package com.namazvakitleri.internetsiz.ui.fragment.settings

import android.content.Intent
import android.os.Bundle
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.FullScreenContentCallback
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback
import com.namazvakitleri.internetsiz.Application
import com.namazvakitleri.internetsiz.R
import com.namazvakitleri.internetsiz.databinding.FragmentSettingsBinding
import com.namazvakitleri.internetsiz.db.dao.PrayTimeDao
import com.namazvakitleri.internetsiz.main.Kible
import com.namazvakitleri.internetsiz.main.Zikirmatik
import com.namazvakitleri.internetsiz.manager.LoadingBarManager
import com.namazvakitleri.internetsiz.modal.ReligiousDaysNights
import com.namazvakitleri.internetsiz.ui.base.BaseFragment
import com.namazvakitleri.internetsiz.ui.fragment.basicreligiousknowledge.BasicReligiousKnowledgeFragment
import com.namazvakitleri.internetsiz.ui.fragment.imsakiye.ImsakiyeFragment
import com.namazvakitleri.internetsiz.ui.fragment.religiousdaynights.ReligiousDayNightsFragment
import com.namazvakitleri.internetsiz.ui.fragment.reminder.ReminderFragment
import com.namazvakitleri.internetsiz.ui.fragment.salarypraytime.SalaryPrayTimeFragment
import com.namazvakitleri.internetsiz.utils.constant.Constant
import com.namazvakitleri.internetsiz.utils.extension.inTransaction
import com.namazvakitleri.internetsiz.utils.extension.isConnectedToNetwork
import com.namazvakitleri.internetsiz.utils.extension.observe
import com.namazvakitleri.internetsiz.utils.extension.showToast
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class SettingsFragment @Inject constructor(): BaseFragment<FragmentSettingsBinding>(), View.OnClickListener {

    private var mInterstitialAd: InterstitialAd? = null


    override val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> FragmentSettingsBinding
        = FragmentSettingsBinding::inflate

    override fun observeViewModel() {

        observe(viewModel.religiousDayNights, ::observeReligiousDayNights)
    }

    private val viewModel: SettingsViewModel  by viewModels()
    @Inject lateinit var prayTimeDao: PrayTimeDao


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


//        mInterstitialAd = newInterstitialAd()
//        loadInterstitial()

        adHandler()


        LoadingBarManager.build(requireContext(), "Dini Günler ve Geceler ${Constant.LOAD}")

        binding?.salaryPrayTimeCV?.setOnClickListener(this)
        binding?.religiousDayNightsCV?.setOnClickListener(this)
        binding?.imsakiyeCV?.setOnClickListener(this)
        binding?.reminderCV?.setOnClickListener(this)
        binding?.basicReligiousKnowledgeCv?.setOnClickListener(this)
        binding?.kiblepusula?.setOnClickListener(this)
        binding?.zikirmatik?.setOnClickListener(this)

    }

/*
    private fun newInterstitialAd(): InterstitialAd {
        val interstitialAd = InterstitialAd(requireContext().applicationContext)
        interstitialAd.adUnitId = getString(R.string.interstitial_ad_unit_id)
        interstitialAd.adListener = object : AdListener() {
            override fun onAdLoaded() {
              //  mInterstitialAd!!.show()
               // Toast.makeText(requireContext().applicationContext, "Yüklendi", Toast.LENGTH_SHORT).show()


            }

            override fun onAdFailedToLoad(errorCode: Int) {

               // Toast.makeText(requireContext().applicationContext, "Ad did not load", Toast.LENGTH_SHORT).show()


            }

            override fun onAdClosed() {
                // Proceed to the next level.
                // goToNextLevel()
                loadInterstitial()
               // Toast.makeText(requireContext().applicationContext, "Ad did not load", Toast.LENGTH_SHORT).show()


            }
        }
        return interstitialAd
    }


    private fun loadInterstitial() {
        // Disable the load ad button and load the ad.
        val adRequest = AdRequest.Builder().build()
        mInterstitialAd!!.loadAd(adRequest)
    }

    private fun showInterstitial() {
        // Show the ad if it is ready. Otherwise toast and reload the ad.
        if (mInterstitialAd != null && mInterstitialAd!!.isLoaded) {
            mInterstitialAd!!.show()
        } else {
            tryToLoadAdOnceAgain()
        }
    }

    private fun tryToLoadAdOnceAgain() {
        mInterstitialAd = newInterstitialAd()
        loadInterstitial()
    }

    */
//////////////////////////////////////
    private fun adHandler(){
        InterstitialAd.load(requireContext(),getString(R.string.interstitial_ad_unit_id),AdRequest.Builder().build(),
            object : InterstitialAdLoadCallback(){
                override fun onAdFailedToLoad(adError: LoadAdError) {
                    super.onAdFailedToLoad(adError)
                    mInterstitialAd = null
                }

                override fun onAdLoaded(interstitialAd: InterstitialAd) {
                    super.onAdLoaded(interstitialAd)
                    mInterstitialAd =  interstitialAd
                }
            })

        mInterstitialAd?.fullScreenContentCallback = object : FullScreenContentCallback(){
            override fun onAdShowedFullScreenContent() {
                super.onAdShowedFullScreenContent()
                mInterstitialAd = null
            }
        }
    }

    private fun showInterstitial() {
        // Show the ad if it is ready. Otherwise toast and reload the ad.
        if (mInterstitialAd != null) {
            mInterstitialAd!!.show(requireActivity())
        } else {
            // Toast.makeText(this, "Ad did not load", Toast.LENGTH_SHORT).show()
            tryToLoadAdOnceAgain()
        }
    }

    private fun tryToLoadAdOnceAgain() {
//      mInterstitialAd = newInterstitialAd()
//      loadInterstitial()
        adHandler()
        showInterstitial()

    }
    //////////////////////////////////////
    private fun observeReligiousDayNights(religiousDaysNights: List<ReligiousDaysNights>) {

        religiousDaysNights.let {
            viewModel.setReligiousDayNightsToDB(religiousDaysNights)
        }
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

                    activity?.finishAffinity()
                    return true
                }
                return false
            }
        })
    }

    override fun onClick(view: View?) {

        when(view?.id) {
            R.id.salaryPrayTimeCV -> {
                if (Application.context.isConnectedToNetwork()){
                    goFragment(SalaryPrayTimeFragment(), "SalaryPrayTimeFragment")
                }
                showInterstitial()
            }

            R.id.religiousDayNightsCV -> {
                if (prayTimeDao.getReligiousDayNights(2021).isEmpty())
                    viewModel.getReligiousDayNightsFromAPI()
                else
                    goFragment(ReligiousDayNightsFragment(), "ReligiousDayNightsFragment")
                mInterstitialAd!!.show(requireActivity())
            }

            R.id.imsakiyeCV -> {
                if (Application.context.isConnectedToNetwork())
                    goFragment(ImsakiyeFragment(), "ImsakiyeFragment")

                else
                    Application.context.showToast(Constant.INTERNET_ACCESS)
            }

            R.id.reminderCV -> {
                goFragment(ReminderFragment(), "ReminderFragment")
            }

            R.id.basicReligiousKnowledgeCv -> {
                goFragment(BasicReligiousKnowledgeFragment(), "BasicReligiousKnowledgeFragment")
            }
            R.id.kiblepusula -> {

                val intent = Intent(activity, Kible::class.java)
                startActivity(intent)
                mInterstitialAd!!.show(requireActivity())

            }
            R.id.zikirmatik -> {

                val intent = Intent(activity, Zikirmatik::class.java)
                startActivity(intent)
            }
        }
    }


    private fun goFragment(fragment: Fragment, addToBackStack: String) {
        fragmentManager?.inTransaction {
            addToBackStack(addToBackStack)
            replace(R.id.dashboardContainer, fragment)
        }
    }
}





