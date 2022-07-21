package com.namazvakitleri.internetsiz.main

import android.content.SharedPreferences
import android.os.Bundle
import android.view.KeyEvent
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.google.android.gms.ads.*
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.namazvakitleri.internetsiz.Application
import com.namazvakitleri.internetsiz.R
import com.namazvakitleri.internetsiz.databinding.ActivityMainBinding
import com.namazvakitleri.internetsiz.db.dao.PrayTimeDao
import com.namazvakitleri.internetsiz.manager.LoadingBarManager
import com.namazvakitleri.internetsiz.modal.ReligiousDaysNights
import com.namazvakitleri.internetsiz.transactions.ShowAlert
import com.namazvakitleri.internetsiz.ui.base.BaseActivity
import com.namazvakitleri.internetsiz.ui.fragment.basicreligiousknowledge.BasicReligiousKnowledgeFragment
import com.namazvakitleri.internetsiz.ui.fragment.home.HomeFragment
import com.namazvakitleri.internetsiz.ui.fragment.imsakiye.ImsakiyeFragment
import com.namazvakitleri.internetsiz.ui.fragment.religiousdaynights.ReligiousDayNightsFragment
import com.namazvakitleri.internetsiz.ui.fragment.reminder.ReminderFragment
import com.namazvakitleri.internetsiz.ui.fragment.salarypraytime.SalaryPrayTimeFragment
import com.namazvakitleri.internetsiz.ui.fragment.settings.SettingsFragment
import com.namazvakitleri.internetsiz.utils.constant.Alert
import com.namazvakitleri.internetsiz.utils.constant.ConstPrefs.FIRST_OPEN_APP
import com.namazvakitleri.internetsiz.utils.constant.ConstPrefs.VOTE
import com.namazvakitleri.internetsiz.utils.constant.Constant
import com.namazvakitleri.internetsiz.utils.constant.Constant.REMINDER_INFO
import com.namazvakitleri.internetsiz.utils.constant.Constant.VOTE_INFO
import com.namazvakitleri.internetsiz.utils.extension.*
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject


@AndroidEntryPoint
class MainActivity : BaseActivity<ActivityMainBinding>() {
    private var mAdView: AdView? = null

    override fun viewBinding(): ActivityMainBinding = ActivityMainBinding.inflate(layoutInflater)

    private val viewModel: MainViewModel by viewModels()

    private var selectedFragment : Fragment? = null

    private var mInterstitialAd: InterstitialAd? = null

    override fun observeLiveData() {
        observe(viewModel.isVersionCurrent, ::showAlertForAppVersion)
        observe(viewModel.religiousDayNights, ::observeReligiousDayNights)
    }

    @Inject lateinit var prefs: SharedPreferences
    @Inject lateinit var prayTimeDao: PrayTimeDao


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val adView = AdView(this)
        adView.setAdSize(AdSize.BANNER)
        adView.setAdUnitId("ca-app-pub-3582614483058227/5002153233")

        // Create the InterstitialAd and set the adUnitId (defined in values/strings.xml).
      //  mInterstitialAd = newInterstitialAd()
      //  loadInterstitial()

        mAdView = findViewById(R.id.adView)
        val adRequest =
            AdRequest.Builder().build()
        mAdView!!.loadAd(adRequest)

        val firstOpenApp = prefs.get(FIRST_OPEN_APP,true)

        if(!firstOpenApp){
            supportFragmentManager.beginTransaction().add(R.id.frame_contInMain,HomeFragment()).commit()
        }else{
            supportFragmentManager.beginTransaction().add(R.id.frame_contInMain,ReminderFragment()).commit()
            findViewById<BottomNavigationView>(R.id.nav_view).selectedItemId = R.id.navigation_reminder
            prefs.put(FIRST_OPEN_APP, false)
            ShowAlert.showReminderInfo(this, REMINDER_INFO, Alert.Okay.value)
        }

        findViewById<BottomNavigationView>(R.id.nav_view).setOnNavigationItemSelectedListener(selectedListener)

        viewModel.getAppVersion(prefs.get(Constant.APP_VERSION,"1.0.7"))
        init(firstOpenApp)

        adHandler()

        LoadingBarManager.build(this, "Dini Günler ve Geceler ${Constant.LOAD}")

        // startService(Intent(this,NotificationService::class.java))
    }
    private val selectedListener =  BottomNavigationView.OnNavigationItemSelectedListener { menuItem ->
        when (menuItem.itemId) {

            R.id.navigation_home -> {
                selectedFragment = HomeFragment()
            }

            R.id.navigation_salaryPrayTime -> {
                if (this.isConnectedToNetwork()) {
                    selectedFragment = SalaryPrayTimeFragment()
                    showInterstitial()
                }else{
                    selectedFragment = HomeFragment()
                    Toast.makeText(this,Constant.INTERNET_ACCESS,Toast.LENGTH_SHORT).show()
                }

            }

            R.id.navigation_religiousDayNights -> {
                if(this.isConnectedToNetwork()) {
                    if (prayTimeDao.getReligiousDayNights(2021).isEmpty()) {
                        viewModel.getReligiousDayNightsFromAPI()
                        selectedFragment = ReligiousDayNightsFragment()
                        showInterstitial()
                    }
                }
                selectedFragment = ReligiousDayNightsFragment()
            }

            R.id.navigation_reminder -> {
                selectedFragment = ReminderFragment()
                if (this.isConnectedToNetwork()) {
                    showInterstitial()
                }
            }

            R.id.navigation_imsakiye -> {

                if (this.isConnectedToNetwork())
                    selectedFragment = ImsakiyeFragment()
                else{
                    selectedFragment = HomeFragment()
                    Toast.makeText(this,Constant.INTERNET_ACCESS,Toast.LENGTH_SHORT).show()
                }
            }

        }
        supportFragmentManager.beginTransaction().replace(R.id.frame_contInMain,selectedFragment!!).commit()
        true
    }

    private fun init(firstOpenApp : Boolean) {

        var showVote = prefs.get(VOTE,1) % 75

        if (showVote == 0) {
            ShowAlert.showAlert(this, VOTE_INFO, Alert.Vote.value)
        }
    }

    /*private fun setPager() {
        var list = ArrayList<Fragment>()
        list.add(HomeFragment())
        list.add(DashboardFragment())

        pager = findViewById(R.id.pager)
        pagerAdapter = SlidePagerAdapter(supportFragmentManager, list)
        pager.adapter = pagerAdapter

        var pageIndicatorView = binding?.pageIndicatorView
        pageIndicatorView?.count = 2
        pageIndicatorView?.selection = 2
        pageIndicatorView?.setSelected(0)

        pager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {

            override fun onPageScrollStateChanged(state: Int) {
            }

            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
                pageIndicatorView?.selection = position
            }
            override fun onPageSelected(position: Int) {

            }

        })
    }*/

    private fun showAlertForAppVersion(isShow: Boolean) {

        //if(isShow)
          // ShowAlert.showAlert(this, Constant.NEW_VERSION, Alert.Update.value)
    }

    private fun observeReligiousDayNights(religiousDaysNights: List<ReligiousDaysNights>) {

        religiousDaysNights.let {
            viewModel.setReligiousDayNightsToDB(religiousDaysNights)
        }
    }

    private fun adHandler(){
        InterstitialAd.load(this,getString(R.string.interstitial_ad_unit_id),AdRequest.Builder().build(),
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
            mInterstitialAd!!.show(this)
        } else {
            // Toast.makeText(this, "Ad did not load", Toast.LENGTH_SHORT).show()
        //tryToLoadAdOnceAgain()
        }
    }

    private fun tryToLoadAdOnceAgain() {
//      mInterstitialAd = newInterstitialAd()
//      loadInterstitial()
        adHandler()
        showInterstitial()
    }
}
