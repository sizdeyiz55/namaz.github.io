package com.namazvakitleri.internetsiz.ui.activity.splash

import android.app.Activity
import android.content.SharedPreferences
import android.os.Bundle
import android.os.Handler
import android.util.Log
import androidx.activity.viewModels
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.FullScreenContentCallback
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback
import com.namazvakitleri.internetsiz.R
import com.namazvakitleri.internetsiz.databinding.ActivitySplashBinding
import com.namazvakitleri.internetsiz.db.dao.PrayTimeDao
import com.namazvakitleri.internetsiz.main.MainActivity
import com.namazvakitleri.internetsiz.manager.LoadingBarManager
import com.namazvakitleri.internetsiz.modal.CurrentTimeDate
import com.namazvakitleri.internetsiz.modal.PrayTimes
import com.namazvakitleri.internetsiz.transactions.CurrentTime
import com.namazvakitleri.internetsiz.ui.activity.information.InformationActivity
import com.namazvakitleri.internetsiz.ui.base.BaseActivity
import com.namazvakitleri.internetsiz.utils.constant.ConstPrefs.DISTRICT_ID
import com.namazvakitleri.internetsiz.utils.constant.ConstPrefs.DISTRICT_NAME
import com.namazvakitleri.internetsiz.utils.constant.ConstPrefs.FIRST_OPEN_APP
import com.namazvakitleri.internetsiz.utils.constant.ConstPrefs.VOTE
import com.namazvakitleri.internetsiz.utils.constant.Constant
import com.namazvakitleri.internetsiz.utils.constant.Constant.APP_VERSION
import com.namazvakitleri.internetsiz.utils.constant.Constant.INFORMATION
import com.namazvakitleri.internetsiz.utils.extension.get
import com.namazvakitleri.internetsiz.utils.extension.observe
import com.namazvakitleri.internetsiz.utils.extension.openActivity
import com.namazvakitleri.internetsiz.utils.extension.put
import dagger.hilt.android.AndroidEntryPoint
import java.util.*
import javax.inject.Inject

@AndroidEntryPoint
class SplashActivity : BaseActivity<ActivitySplashBinding>() {

//    private lateinit var activity_binding: ActivitySplashBinding

    override fun viewBinding() =  ActivitySplashBinding.inflate(layoutInflater)

    private val viewModel: SplashViewModel by viewModels()

    override fun observeLiveData() {

        observe(viewModel.prayTimes, ::observePrayTimes)
    }


    @Inject lateinit var prefs: SharedPreferences
    @Inject lateinit var prayTimeDao: PrayTimeDao

    private var districtId: Int = 0
    private lateinit var districtName: String
    private var isSeenInformation: Boolean = false
    //Ad Object
    private var mInterstitialAd: InterstitialAd? = null
    private lateinit var currentTime: CurrentTimeDate


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash);


        // Create the InterstitialAd and set the adUnitId (defined in values/strings.xml).
//        mInterstitialAd = newInterstitialAd()
        adHandler(this)
//        loadInterstitial()

        setAppVersion()
        LoadingBarManager.build(this, "Aylık vakitleriniz ${Constant.LOAD}")

        if (!prefs.get(Constant.CACHE_CLEARED, false)) {

            goActivity(InformationActivity::class.java)
        } else {

            init()
            whichPass()
        }
    }
    //Test 1


    private fun adHandler(activity: Activity){
        InterstitialAd.load(this,getString(R.string.interstitial_ad_unit_id),AdRequest.Builder().build(),
            object : InterstitialAdLoadCallback(){
                override fun onAdFailedToLoad(adError: LoadAdError) {
                    super.onAdFailedToLoad(adError)
                    mInterstitialAd = null
                }

                override fun onAdLoaded(interstitialAd: InterstitialAd) {
                    super.onAdLoaded(interstitialAd)
                    mInterstitialAd =  interstitialAd
                    mInterstitialAd!!.show(activity)

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
            tryToLoadAdOnceAgain()
        }
    }

    private fun tryToLoadAdOnceAgain() {
//      mInterstitialAd = newInterstitialAd()
//      loadInterstitial()
        adHandler(this)
        showInterstitial()

    }

/*
  private fun newInterstitialAd(): InterstitialAd {
      val interstitialAd = InterstitialAd(this)
      interstitialAd.adUnitId = getString(R.string.interstitial_ad_unit_id)
      interstitialAd.adListener = object : AdListener() {
          override fun onAdLoaded() {
            //  Toast.makeText(applicationContext, "Ad Loaded", Toast.LENGTH_SHORT).show()
           //   interstitialAd.show() //show ad

          }

          override fun onAdFailedToLoad(errorCode: Int) {
            //  Toast.makeText(applicationContext, "Ad Failed To Load", Toast.LENGTH_SHORT).show()
          }

          override fun onAdClosed() {
              // Proceed to the next level.
              // goToNextLevel()
           //   Toast.makeText(applicationContext, "Ad Closed", Toast.LENGTH_SHORT).show()
          }
      }
      return interstitialAd
  }

    */
/*
  private fun loadInterstitial() {
      // Disable the load ad button and load the ad.
      val adRequest = AdRequest.Builder().build()
      mInterstitialAd!!.loadAd(adRequest)


  }

 */


  private fun init() {

      currentTime = CurrentTime.getCurrentTime()

      isSeenInformation = prefs.get(INFORMATION, false)
      districtId = prefs.get(DISTRICT_ID, 0)
      districtName = prefs.get(DISTRICT_NAME, "null")


      var count = prefs.get(VOTE,0)
      prefs.put(VOTE, count+1)
  }


  private fun whichPass() {
      if (prayTimeDao.getPrayTime(currentTime.date) == null) {

          if (districtName == "null") {

                  prefs.put(FIRST_OPEN_APP, false)
                  goActivity(InformationActivity::class.java)
          } else { // vakitler bitmis.

              prayTimeDao.deletePrayTime()
              viewModel.getPrayTimeFromAPI(districtId)
          }
      } else {

          goActivity(MainActivity::class.java)
      }
  }

  private fun goActivity(goActivity: Class<*>) {
      Handler().postDelayed(
          {
              openActivity(goActivity)
              overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
          }, 500
      )
  }

  private fun observePrayTimes(prayTimes: ArrayList<PrayTimes>) {

      prayTimes?.let {
          viewModel.setPrayTimesToDB(prayTimes, districtId)
      }
  }

  private fun setAppVersion() {
      var manager = applicationContext.packageManager
      var info = manager.getPackageInfo(applicationContext.packageName, 0)
      prefs.put(APP_VERSION, info.versionName)
  }
}


/*   uiScope.launch {
                     val response = ApiClient.api(Constant.API_URL)
                         .create(ApiService::class.java)
                         .getPrayTimes(districtId.toInt())

                     if (response.isSuccessful) {

                         val list = (response.body() as ArrayList<PrayTimes>)

                         var isRegistrationCompleted = GlobalScope.async {
                             insertPrayTimeAsync.insertData(list)
                         }

                         GlobalScope.launch {
                             isRegistrationCompleted.await() // database'e ekleme islemi tamamlanana kadar bekle.

                             if(alertDialog.isShowing) {
                                 alertDialog.dismiss()
                             }*/