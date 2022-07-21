package com.namazvakitleri.internetsiz.ui.fragment.home


import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.os.CountDownTimer
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.appbar.AppBarLayout.OnOffsetChangedListener
import com.google.firebase.firestore.FirebaseFirestore
import com.namazvakitleri.internetsiz.R
import com.namazvakitleri.internetsiz.adapter.recycler.AlertRvAdapter
import com.namazvakitleri.internetsiz.databinding.FragmentBasicReligiousKnowledgeBinding
import com.namazvakitleri.internetsiz.databinding.FragmentHomeBinding
import com.namazvakitleri.internetsiz.db.dao.PrayTimeDao
import com.namazvakitleri.internetsiz.manager.FadeInLinearLayoutManager
import com.namazvakitleri.internetsiz.manager.ReminderPrayTimesManager
import com.namazvakitleri.internetsiz.modal.*
import com.namazvakitleri.internetsiz.transactions.CurrentTime
import com.namazvakitleri.internetsiz.transactions.PrayTimeTransactions
import com.namazvakitleri.internetsiz.ui.activity.praytimeupdateinfo.PrayTimeUpdateInfoActivity
import com.namazvakitleri.internetsiz.ui.activity.region.RegionActivity
import com.namazvakitleri.internetsiz.ui.activity.remindingsettings.RemindingSettingActivity
import com.namazvakitleri.internetsiz.ui.base.BaseFragment
import com.namazvakitleri.internetsiz.ui.fragment.basicreligiousknowledge.BasicReligiousKnowledgeFragment
import com.namazvakitleri.internetsiz.utils.constant.ConstPrefs.CANCEL_REGION_BTN
import com.namazvakitleri.internetsiz.utils.constant.ConstPrefs.CREATED_ALARM
import com.namazvakitleri.internetsiz.utils.constant.ConstPrefs.DISTRICT_NAME
import com.namazvakitleri.internetsiz.utils.constant.ConstPrefs.PROVINCE_NAME
import com.namazvakitleri.internetsiz.utils.constant.ConstPrefs.REMINDER_SETTINGS_SAVED
import com.namazvakitleri.internetsiz.utils.constant.Constant
import com.namazvakitleri.internetsiz.utils.constant.Constant.AKSAM
import com.namazvakitleri.internetsiz.utils.constant.Constant.OGLE
import com.namazvakitleri.internetsiz.utils.extension.*
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.*
import java.text.SimpleDateFormat
import javax.inject.Inject


private const val countDownInterval: Long = 1000

@AndroidEntryPoint
class HomeFragment : BaseFragment<FragmentHomeBinding>(), View.OnClickListener {

    // Coroutines
    private var job = Job()
    private val coroutineScope = CoroutineScope(Dispatchers.Main + job)

    override val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> FragmentHomeBinding =
        FragmentHomeBinding::inflate


    private var ayet : String = ""
    private var ayetRes: String = ""
    private var esma : String = ""
    private var esmaRes: String = ""
    private var mhadis : String = ""
    private var mhadisRes: String = ""
    private var dua : String = ""
    private var duaRes: String = ""
    private var name : String? = ""
    private var hikmetname: String? = ""
    private var babyname : String? = ""
    private var babynameMeaning: String? = ""

    override fun observeViewModel() {
        observe(viewModel.esmaulhusna, ::ResponseEsmaulHusna)
        observe(viewModel.hadis, ::ResponseThis)
        observe(viewModel.ayet, ::ResponseAyet)
        observe(viewModel.dua, :: ResponseDua)
        observe(viewModel.Baby, ::ResponseBaby)
        observe(viewModel.Hikmetname, ::ResponseHikmetname)
    }


    private var TimeList : ArrayList<String>? = null
    private var remaningPray : String? = null
    private var afterYatsi : Boolean? = false

    private val viewModel: HomeViewModel by viewModels()

    @Inject lateinit var prefs: SharedPreferences
    @Inject lateinit var prayTimeDao: PrayTimeDao
    @Inject lateinit var reminderPrayTimesManager: ReminderPrayTimesManager
    @Inject lateinit var firestore: FirebaseFirestore

    private lateinit var districtName: String
    private lateinit var provinceName: String
    private lateinit var prayTimeValue: PrayTimeValue
    private val hh_mm_ss = SimpleDateFormat("HH:mm:ss")


    private var scroolY: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        TimeList = ArrayList<String>()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (dataControl()) {
            Intent(requireContext(), PrayTimeUpdateInfoActivity::class.java).let {
                it.flags = Intent.FLAG_ACTIVITY_NO_HISTORY
                startActivity(it)
            }
        } else {
            init()
            timeSettings()
            willAlarmUpdated()

            coroutineScope.launch(Dispatchers.Main) {
                viewModel.getHadisFromAPI()
            }
        }

        binding?.regionTxt?.isSelected = true

        binding?.cardviewInHomeForLocation?.setOnClickListener(this)
        binding?.imageBasicReligiousKnowledge?.setOnClickListener(this)
        binding?.remindingSettingImgView?.setOnClickListener(this)
        binding?.rv?.layoutManager = FadeInLinearLayoutManager(context,LinearLayoutManager.VERTICAL,false)

        binding?.appBarLayout?.addOnOffsetChangedListener(OnOffsetChangedListener { appBarLayout, verticalOffset ->
                if (Math.abs(verticalOffset) - appBarLayout.totalScrollRange == 0) {
                    binding?.toolbarLayout?.isTitleEnabled = true
                    binding?.constraintInHome?.visibility = View.INVISIBLE
                } else {
                    binding?.constraintInHome?.visibility = View.VISIBLE
                    binding?.toolbarLayout?.isTitleEnabled = false
                }
        })

    }

    private fun timeSettings() {

        prayTimeValue = viewModel.getPrayTimeValue()
        setPrayTimeValues(prayTimeValue)

        remaningPray = prayTimeValue.calculateTimes.remainingPrayName
        afterYatsi = prayTimeValue.calculateTimes.isAfterYatsi

        timer(
            prayTimeValue.calculateTimes.remainingPrayTimeMs,
            prayTimeValue.calculateTimes.remainingPrayName
        ).start()

    }


    private fun setPrayTimeValues(prayTimeValue: PrayTimeValue) {

        TimeList?.add(prayTimeValue.prayTimes.Imsak)
        TimeList?.add(prayTimeValue.prayTimes.Gunes)
        TimeList?.add(prayTimeValue.prayTimes.Ogle)
        TimeList?.add(prayTimeValue.prayTimes.Ikindi)
        TimeList?.add(prayTimeValue.prayTimes.Aksam)
        TimeList?.add(prayTimeValue.prayTimes.Yatsi)

        binding?.hicriTxt?.text = prayTimeValue.prayTimes.HicriUzun
        binding?.miladiTxt?.text = prayTimeValue.prayTimes.MiladiUzun
    }

    private fun timer(
        millisInFuture: Long,
        whichPray: String
    ): CountDownTimer {

        return object : CountDownTimer(millisInFuture, countDownInterval) {
            override fun onTick(millisUntilFinished: Long) {
                val fromMillisecondsToTime =
                    PrayTimeTransactions.fromMillisecondsToTime(millisUntilFinished)
                binding?.remainingTimeTxt?.text = fromMillisecondsToTime
                isTimeKerahat(millisUntilFinished, whichPray)
                binding?.toolbarLayout?.title = "vaktin çıkmasına - " + fromMillisecondsToTime
            }

            override fun onFinish() {
                timeSettings()
                kerahatTimeSettings(false)
            }
        }
    }

    /**
     * Gunesten sonraki kerahatte; gunes vaktini ms'e cevirip ustune 45'in ms'sini ekle.
     * Guncel vaktin ms'sinden kucukse demekki gunesten sonra 45 dk gecmemis. bu da hala kerahatte oldugu anlamina geliyor.
     */
    private fun isTimeKerahat(millisUntilFinished: Long, whichPray: String) {

        if (whichPray == OGLE || whichPray == AKSAM) {
            if (millisUntilFinished < Constant.KERAHAT_MINUTE) {

                kerahatTimeSettings(true)
            } else {
                val kerahatTime = CurrentTime.getCurrentTime()
                val currentTimeConverted = hh_mm_ss.format(kerahatTime.time).replace(":", ".")
                val gunesTime = prayTimeDao.getPrayTime(CurrentTime.getCurrentTime().date)
                val gunesTimeConverted = gunesTime.Gunes.replace(":", ".")

                val isTimeKerahat = PrayTimeTransactions.isTimeKerahat(
                    gunesTimeConverted,
                    currentTimeConverted,
                    Constant.KERAHAT_MINUTE
                )

                kerahatTimeSettings(isTimeKerahat)
            }
        }
    }

    private fun kerahatTimeSettings(isTimeKerahat: Boolean) {
        if (isTimeKerahat) {
            binding?.kerahatCv?.visibility = View.VISIBLE
            binding?.kerahatTxt?.visibility = View.VISIBLE
            binding?.kerahatTxt?.isSelected = true
            binding?.exitTimeTxt?.setTextColor(Color.RED)
            binding?.remainingTimeTxt?.setTextColor(Color.RED)
        } else {
            binding?.kerahatCv?.visibility = View.GONE
            binding?.kerahatTxt?.visibility = View.GONE
            binding?.exitTimeTxt?.setTextColor(Color.WHITE)
            binding?.remainingTimeTxt?.setTextColor(Color.WHITE)
        }
    }


    private fun init() {

        districtName = prefs.get(DISTRICT_NAME, "null")
        provinceName = prefs.get(PROVINCE_NAME, "null")
        binding?.regionTxt?.text = "$provinceName - $districtName"
        //binding?.hadisLoadingBar?.visibility = View.VISIBLE
    }


    override fun onResume() {
        super.onResume()

        if (dataControl()) {
            startActivity(Intent(context, PrayTimeUpdateInfoActivity::class.java))
        }
    }

    private fun dataControl(): Boolean {

        val currentTime = CurrentTime.getCurrentTime()
        val prayTimes = prayTimeDao.getPrayTime(currentTime.date)

        return prayTimes == null
    }

    private fun willAlarmUpdated() {

        val createAlarm = prefs.get(CREATED_ALARM, false)
        val reminderSettingsSaved = prefs.get(REMINDER_SETTINGS_SAVED, false)

        if (createAlarm && reminderSettingsSaved) {
            reminderPrayTimesManager.start(0)
            prefs.put(CREATED_ALARM, false)
        }
    }

    override fun onClick(view: View?) {
        when (view?.id) {
            R.id.cardviewInHomeForLocation -> {
                context?.openActivity(RegionActivity::class.java)
            }
            R.id.imageBasicReligiousKnowledge -> {
               fragmentManager?.beginTransaction()?.replace(R.id.frame_contInMain, BasicReligiousKnowledgeFragment())
                ?.addToBackStack("BasicReligiousKnowledge")?.commit()
            }
            R.id.remindingSettingImgView -> {
                context?.openActivity(RemindingSettingActivity::class.java)
            }
        }
    }


    private fun ResponseThis(hadis: Hadis) {
      //  binding?.hadisLoadingBar?.visibility = View.GONE
        mhadis = hadis.hadis
        mhadisRes = hadis.hadisNumber
    }

    private fun ResponseAyet(myAyet: Ayet) {
        ayet = myAyet.ayet
        ayetRes = myAyet.ayetNumber

    }
    private fun ResponseDua(myDua: Dua) {
        dua = myDua.dua
        duaRes = myDua.duaNumber
    }
    private fun ResponseEsmaulHusna(name: EsmaulHusna) {
        esma = name.esmaulhusna
        esmaRes = name.esmaulhusnaNumber
    }

    private fun ResponseBaby(baby: BabyModel) {
        babyname = baby.BabyName
        babynameMeaning = baby.Meaning
    }

    private fun ResponseHikmetname(Hikmetname: HikmetnameModel) {
        hikmetname = Hikmetname.Hikmetname
        name = Hikmetname.Name
        binding?.rv?.adapter = AlertRvAdapter(requireContext(),TimeList!!,remaningPray!!,afterYatsi,dua,
            duaRes,ayet,ayetRes,mhadis,mhadisRes,esma,esmaRes,hikmetname,name,babyname,babynameMeaning,null,null)
    }

}