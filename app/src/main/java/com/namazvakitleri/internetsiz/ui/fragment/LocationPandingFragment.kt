package com.namazvakitleri.internetsiz.ui.fragment

import android.Manifest
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.graphics.Color
import android.location.*
import android.os.Bundle
import android.os.CountDownTimer
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.firestore.FieldPath
import com.google.firebase.firestore.FirebaseFirestore
import com.namazvakitleri.internetsiz.Application
import com.namazvakitleri.internetsiz.R
import com.namazvakitleri.internetsiz.databinding.FragmentLocationPandingBinding
import com.namazvakitleri.internetsiz.db.InsertPrayTimeToDB
import com.namazvakitleri.internetsiz.db.dao.PrayTimeDao
import com.namazvakitleri.internetsiz.main.MainActivity
import com.namazvakitleri.internetsiz.manager.NetworkDialogManager
import com.namazvakitleri.internetsiz.modal.Country
import com.namazvakitleri.internetsiz.modal.PrayTimes
import com.namazvakitleri.internetsiz.modal.Province
import com.namazvakitleri.internetsiz.retrofit.ApiService
import com.namazvakitleri.internetsiz.transactions.CurrentTime
import com.namazvakitleri.internetsiz.ui.activity.error.ErrorActivity
import com.namazvakitleri.internetsiz.ui.base.BaseFragment
import com.namazvakitleri.internetsiz.ui.fragment.region.country.CountryFragment
import com.namazvakitleri.internetsiz.utils.constant.ConstPrefs
import com.namazvakitleri.internetsiz.utils.constant.Constant
import com.namazvakitleri.internetsiz.utils.enum.Results
import com.namazvakitleri.internetsiz.utils.extension.*
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject


@AndroidEntryPoint
class LocationPandingFragment @Inject constructor() : BaseFragment<FragmentLocationPandingBinding>() {

    private val EnglishAlphabets = "cgiiosu".toCharArray()
    private val TurkishAlphabets = "çğıiöşü".toCharArray()

    override val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> FragmentLocationPandingBinding
            = FragmentLocationPandingBinding::inflate


    override fun observeViewModel() {

    }

    private val viewModel: LocationPandingViewModel by viewModels()

    @Inject lateinit var layoutManager: LinearLayoutManager
    @Inject lateinit var apiService: ApiService
    @Inject lateinit var insertPrayTimeToDB: InsertPrayTimeToDB
    @Inject lateinit var prayTimeDao: PrayTimeDao
    @Inject lateinit var prefs: SharedPreferences
    @Inject lateinit var firestore: FirebaseFirestore

    private lateinit var textTick: TextView
    private lateinit var timer : CountDownTimer

    private lateinit var manager: FragmentManager
    private lateinit var locationManager: LocationManager
    private lateinit var locationListener: LocationListener

    private lateinit var permissionLauncher: ActivityResultLauncher<String>

    private var countryname : String? = null
    private var cityName : String? = null
    private var gps_enabled = false
    private var network_enabled = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        manager = (requireActivity() as AppCompatActivity).supportFragmentManager

        locationManager = requireActivity().getSystemService(Context.LOCATION_SERVICE) as LocationManager

        gps_enabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
        network_enabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)

        registerLauncher()

        timer = object: CountDownTimer(40000, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                textTick.text = (millisUntilFinished / 1000).toString()
            }

            override fun onFinish() {
                Toast.makeText(
                    requireContext(),
                    "Konumunuz ile gelen bilgilerle sorun yaşadık. Lütfen manuel olarak seçim yapınız.",
                    Toast.LENGTH_LONG
                ).show()

                manager.inTransaction {
                    replace(R.id.fragmentContainer, CountryFragment())
                }
                onDestroy()
            }
        }

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        textTick = view.findViewById(R.id.txtTick)

        if (!gps_enabled && !network_enabled) {
            AlertDialog.Builder(context)
                .setMessage("Konum servisleri kapalı, lütfen konum servislerini açıp tekrar deneyin")
                .setPositiveButton("Konum ayarlarına git"
                ) { paramDialogInterface, paramInt ->
                    requireContext().startActivity(
                        Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                    )
                    requireActivity().finish()
                }
                .setNegativeButton("İptal") { dialogInterface, i ->
                    dialogInterface.dismiss()
                    manager.inTransaction {
                        replace(R.id.fragmentContainer, CountryFragment())
                    }
                    onDestroy()
                }.setCancelable(false)
                .show()
        }else  if (!requireContext().isConnectedToNetwork()) {
            NetworkDialogManager.networkDialog(requireActivity(), "RegionActivity")
        }else{
            timer.start()
            locationListener = object : LocationListener {
                override fun onLocationChanged(location: Location) {
                    val address: List<Address>
                    val geocoder = Geocoder(requireContext(), Locale.getDefault())

                    address = geocoder.getFromLocation(location.latitude, location.longitude, 1)

                    countryname = address.get(0).countryName
                    cityName = address.get(0).adminArea

                    locationManager.removeUpdates(this)

                    if (countryname != "Türkiye") {
                        Toast.makeText(
                            requireContext(),
                            "Bulunduğunuz ülke Diyanet İşleri Başkanlığının namaz vakitlerini sağladığı ülkeler listesinde değil! Lütfen manuel olarak seçim yapınız.",
                            Toast.LENGTH_LONG
                        ).show()

                        manager.inTransaction {
                            replace(R.id.fragmentContainer, CountryFragment())
                        }
                        onDestroy()
                    }else{
                        setLocation()
                        prefs.put("CountryLower",countryname?.lowercase())

                    }

                }
                override fun onProviderEnabled(provider: String) {}

                override fun onProviderDisabled(provider: String) {}

                override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {}

            }

            if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                if (ActivityCompat.shouldShowRequestPermissionRationale(requireActivity(), Manifest.permission.ACCESS_FINE_LOCATION)) {
                    Snackbar.make(binding!!.root, "Konum bilgileri için izin lazım.", Snackbar.LENGTH_INDEFINITE).setAction("İzin Ver") {

                        permissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)

                    }.setActionTextColor(Color.WHITE).show()
                } else {

                    permissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)

                }
            } else {
                locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0f, locationListener)
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0f, locationListener)
            }
        }


    }

    private fun registerLauncher() {
        permissionLauncher = registerForActivityResult(ActivityResultContracts.RequestPermission()) { result ->
            if (result) {
                //permission granted
                if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

                    locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0f, locationListener)
                    locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0f, locationListener)

                }
            } else {
                //permission denied
                    Toast.makeText(
                        requireContext(),
                        "Konum bilgilerini doğrulamak için konum izni vermeniz gerekmektedir.",
                        Toast.LENGTH_LONG
                    ).show()

                    manager.inTransaction {
                        replace(R.id.fragmentContainer, CountryFragment())
                    }
                onDestroy()
            }
        }
    }

    private fun setLocation(){

        var sehirId = -1
        var districtId = -1

        val convertedCity = convertCity(cityName!!.lowercase().trim())

            if (!requireContext().isConnectedToNetwork()) {
                NetworkDialogManager.networkDialog(requireActivity(), "RegionActivity")
            } else {

                    firestore.collection("Cities").document("2").get()
                        .addOnSuccessListener { result ->
                            if (result.data != null) {
                                for (j in result.data!!) {
                                    if (convertCity(j.key?.lowercase()?.trim()!!)
                                            .equals(convertedCity)
                                    ) {
                                        sehirId = j.value.toString().toInt()

                                        break
                                    }
                                }
                                if (sehirId != -1) {
                                    firestore.collection("Districts").document(sehirId.toString())
                                        .get().addOnSuccessListener { res ->
                                        if (res.data != null) {
                                            for (k in res.data!!) {
                                                if (convertCity(k.key?.lowercase()?.trim()!!)
                                                        .equals(convertedCity)
                                                ) {
                                                    districtId = k.value.toString().toInt()

                                                    prefs.put(ConstPrefs.DISTRICT_NAME, k.key)
                                                    prefs.put(
                                                        ConstPrefs.DISTRICT_ID,
                                                        k.value.toString().toInt()
                                                    )
                                                    break
                                                }
                                            }

                                            if (districtId != -1) {
                                                    firestore.collection("PrayTimes")
                                                        .document("2").collection(districtId.toString()).get()
                                                        .addOnCompleteListener { task ->

                                                            if (task.isSuccessful) {
                                                                val result1 = task.result

                                                                if (!result1.isEmpty) {



                                                                    for(x in 0..30) {
                                                                        if(result1.documents.get(x).id == "30"){
                                                                            val dateFromFirestore = result1.documents.get(x).get("miladiTarihKisa").toString()
                                                                            val formatter = SimpleDateFormat("dd.MM.yyyy", Locale.ENGLISH)

                                                                            val date = formatter.parse(dateFromFirestore)
                                                                            val dateCurrent = formatter.parse(CurrentTime.getCurrentTime().date)


                                                                            if (dateCurrent!!.after(date)) {
                                                                                apiService.getPrayTime(districtId).enqueue { r4 ->
                                                                                    when (r4) {
                                                                                        is Results.Success -> {
                                                                                            val prayTimes =
                                                                                                r4.response.body() as ArrayList<PrayTimes>?
                                                                                            var count =
                                                                                                0
                                                                                            for (i in prayTimes!!) {
                                                                                                firestore.collection("PrayTimes").document("2").collection(districtId.toString())
                                                                                                    .document(count.toString())
                                                                                                    .set(i)
                                                                                                count++
                                                                                            }

                                                                                            val districtid =
                                                                                                prefs.get(
                                                                                                    ConstPrefs.DISTRICT_ID,
                                                                                                    0
                                                                                                )

                                                                                            if (districtid == 0) {
                                                                                                prefs.put(
                                                                                                    ConstPrefs.CREATED_ALARM,
                                                                                                    false
                                                                                                )
                                                                                            } else {
                                                                                                prefs.put(
                                                                                                    ConstPrefs.CREATED_ALARM,
                                                                                                    true
                                                                                                )
                                                                                            }

                                                                                            if (prayTimeDao.getPrayTimeItemCount() != 0) {
                                                                                                prayTimeDao.deletePrayTime()
                                                                                            }

                                                                                            prefs.put(
                                                                                                ConstPrefs.PROVINCE_NAME,
                                                                                                "${cityName?.uppercase()}"
                                                                                            )
                                                                                            prefs.put(
                                                                                                Constant.CACHE_CLEARED,
                                                                                                true
                                                                                            )

                                                                                            insertPrayTimeToDB.isDataFinished.observe(
                                                                                                viewLifecycleOwner
                                                                                            ) { boolean ->
                                                                                                if (boolean == true) {
                                                                                                    Application.context.openActivity(
                                                                                                        MainActivity::class.java
                                                                                                    ) {
                                                                                                        putString(
                                                                                                            "DistrictId",
                                                                                                            districtId.toString()
                                                                                                        )
                                                                                                    }
                                                                                                    onDestroy()
                                                                                                }
                                                                                            }

                                                                                            CoroutineScope(
                                                                                                Dispatchers.Main
                                                                                            ).launch {
                                                                                                insertPrayTimeToDB.insertData(
                                                                                                    prayTimes
                                                                                                )
                                                                                            }

                                                                                        }
                                                                                    }
                                                                                }
                                                                            } else {
                                                                                val arrayList =
                                                                                    ArrayList<PrayTimes>()

                                                                                for (m in result1.documents) {
                                                                                    val mymap = m.data

                                                                                    arrayList.add(
                                                                                        PrayTimes(
                                                                                            mymap?.get("imsak")
                                                                                                .toString(),
                                                                                            mymap?.get("gunes")
                                                                                                .toString(),
                                                                                            mymap?.get("ogle")
                                                                                                .toString(),
                                                                                            mymap?.get("ikindi")
                                                                                                .toString(),
                                                                                            mymap?.get("aksam")
                                                                                                .toString(),
                                                                                            mymap?.get("yatsi")
                                                                                                .toString(),
                                                                                            mymap?.get("hicriTarihUzun")
                                                                                                .toString(),
                                                                                            mymap?.get("miladiTarihKisa")
                                                                                                .toString(),
                                                                                            mymap?.get("miladiTarihUzun")
                                                                                                .toString()
                                                                                        )
                                                                                    )
                                                                                }

                                                                                val districtid =
                                                                                    prefs.get(
                                                                                        ConstPrefs.DISTRICT_ID,
                                                                                        0
                                                                                    )

                                                                                if (districtid == 0) {
                                                                                    prefs.put(
                                                                                        ConstPrefs.CREATED_ALARM,
                                                                                        false
                                                                                    )
                                                                                } else {
                                                                                    prefs.put(
                                                                                        ConstPrefs.CREATED_ALARM,
                                                                                        true
                                                                                    )
                                                                                }

                                                                                if (prayTimeDao.getPrayTimeItemCount() != 0) {
                                                                                    prayTimeDao.deletePrayTime()
                                                                                }

                                                                                prefs.put(
                                                                                    ConstPrefs.PROVINCE_NAME,
                                                                                    "${cityName?.uppercase()}"
                                                                                )
                                                                                prefs.put(
                                                                                    Constant.CACHE_CLEARED,
                                                                                    true
                                                                                )

                                                                                insertPrayTimeToDB.isDataFinished.observe(
                                                                                    viewLifecycleOwner
                                                                                ) { boolean ->
                                                                                    if (boolean == true) {
                                                                                        Application.context.openActivity(
                                                                                            MainActivity::class.java
                                                                                        ) {
                                                                                            putString(
                                                                                                "DistrictId",
                                                                                                districtId.toString()
                                                                                            )
                                                                                        }
                                                                                        onDestroy()
                                                                                    }
                                                                                }

                                                                                CoroutineScope(
                                                                                    Dispatchers.Main
                                                                                ).launch {
                                                                                    insertPrayTimeToDB.insertData(
                                                                                        arrayList
                                                                                    )
                                                                                }
                                                                            }
                                                                            break
                                                                        }
                                                                    }
                                                                }else{


                                                                    Toast.makeText(
                                                                        requireContext(),
                                                                        "Bu konum ilk defa kullanıldığı için düşündüğümüzden biraz uzun sürebilir, lütfen uygulamayı kapatmayınız",
                                                                        Toast.LENGTH_LONG
                                                                    ).show()

                                                                    apiService.getPrayTime(districtId).enqueue { r4 ->

                                                                        when (r4) {
                                                                            is Results.Success -> {
                                                                                val prayTimes = r4.response.body() as ArrayList<PrayTimes>?
                                                                                var count = 0
                                                                                for(i in prayTimes!!) {
                                                                                    firestore.collection("PrayTimes").document("2").collection(districtId.toString()).document(count.toString()).set(i)
                                                                                    count++
                                                                                }
                                                                                            val districtid =
                                                                                                prefs.get(
                                                                                                    ConstPrefs.DISTRICT_ID,
                                                                                                    0
                                                                                                )

                                                                                            if (districtid == 0) {
                                                                                                prefs.put(
                                                                                                    ConstPrefs.CREATED_ALARM,
                                                                                                    false
                                                                                                )
                                                                                            } else {
                                                                                                prefs.put(
                                                                                                    ConstPrefs.CREATED_ALARM,
                                                                                                    true
                                                                                                )
                                                                                            }

                                                                                            if (prayTimeDao.getPrayTimeItemCount() != 0) {
                                                                                                prayTimeDao.deletePrayTime()
                                                                                            }

                                                                                            prefs.put(
                                                                                                ConstPrefs.PROVINCE_NAME,
                                                                                                "${cityName?.uppercase()}"
                                                                                            )
                                                                                            prefs.put(
                                                                                                Constant.CACHE_CLEARED,
                                                                                                true
                                                                                            )

                                                                                            insertPrayTimeToDB.isDataFinished.observe(
                                                                                                viewLifecycleOwner
                                                                                            ) { boolean ->
                                                                                                if (boolean == true) {
                                                                                                    Application.context.openActivity(
                                                                                                        MainActivity::class.java
                                                                                                    ) {
                                                                                                        putString(
                                                                                                            "DistrictId",
                                                                                                            districtId.toString()
                                                                                                        )
                                                                                                    }
                                                                                                    onDestroy()
                                                                                                }
                                                                                            }

                                                                                            CoroutineScope(Dispatchers.Main).launch {
                                                                                                insertPrayTimeToDB.insertData(
                                                                                                    prayTimes
                                                                                                )
                                                                                            }

                                                                            }
                                                                        }
                                                                    }

                                                                }
                                                            } else {
                                                                Application.context.openActivity(
                                                                    ErrorActivity::class.java
                                                                )
                                                            }

                                                        }.addOnFailureListener {
                                                            Application.context.openActivity(
                                                                ErrorActivity::class.java
                                                            )
                                                        }

                                            } else {
                                                Toast.makeText(
                                                    requireContext(),
                                                    "Konumunuzdan bulunduğunuz ilçe teşhis edilemedi. Lütfen manuel olarak seçim yapınız.",
                                                    Toast.LENGTH_LONG
                                                ).show()

                                                manager.inTransaction {
                                                    replace(
                                                        R.id.fragmentContainer,
                                                        CountryFragment()
                                                    )
                                                }
                                                onDestroy()
                                            }

                                        }
                                    }.addOnFailureListener {
                                        Application.context.openActivity(ErrorActivity::class.java)
                                    }
                                } else {

                                    Toast.makeText(
                                        requireContext(),
                                        "Konumunuzdan bulunduğunuz şehir teşhis edilemedi. Lütfen manuel olarak seçim yapınız.",
                                        Toast.LENGTH_LONG
                                    ).show()

                                    manager.inTransaction {
                                        replace(R.id.fragmentContainer, CountryFragment())
                                    }
                                    onDestroy()
                                }
                            }
                        }.addOnFailureListener {
                        Application.context.openActivity(ErrorActivity::class.java)
                    }
                        /*apiService.getProvince(2).enqueue { r1 ->
                            println("2 basladi")
                            when (r1) {
                                is Results.Success -> {
                                    println("sehir :" + cityName)
                                    println("sehir :" + cityName?.lowercase())
                                    val provinceData = r1.response.body() as ArrayList<Province>?
                                    for (j in provinceData!!) {
                                        println("sehiradi:" + j.SehirAdi?.lowercase())
                                        println("sehiradibenim:" + cityName?.lowercase())

                                    }
                                }
                                is Results.Failure -> {
                                    Application.context.openActivity(ErrorActivity::class.java)
                                }
                            }
                            println("2 bitti")
                            if (sehirId != -1) {
                                apiService.getDistrict(sehirId).enqueue { r3 ->
                                    println("3 basladi")
                                    when (r3) {
                                        is Results.Success -> {
                                            val districtData =
                                                r3.response.body() as ArrayList<District>?

                                            for (k in districtData!!) {

                                                if (k.IlceAdi?.lowercase()?.trim()
                                                        .toString() == cityName?.lowercase()?.trim()
                                                ) {
                                                    districtId = k.IlceID!!
                                                    println("3 girdi")
                                                    prefs.put(ConstPrefs.DISTRICT_NAME, k.IlceAdi)
                                                    prefs.put(ConstPrefs.DISTRICT_ID, k.IlceID)
                                                    break
                                                }
                                            }
                                        }

                                        is Results.Failure -> {
                                            Application.context.openActivity(ErrorActivity::class.java)
                                        }
                                    }
                                    println("3 bitti")
                                    if (districtId != -1) {
                                        apiService.getPrayTime(districtId).enqueue { r4 ->
                                            println("4 basladi")
                                            when (r4) {
                                                is Results.Success -> {

                                                    val prayTimes = r4.response.body() as ArrayList<PrayTimes>?

                                                    val districtid = prefs.get(ConstPrefs.DISTRICT_ID, 0)

                                                    if (districtid == 0) {
                                                        prefs.put(ConstPrefs.CREATED_ALARM, false)
                                                    } else {
                                                        prefs.put(ConstPrefs.CREATED_ALARM, true)
                                                    }

                                                    if (prayTimeDao.getPrayTimeItemCount() != 0) {
                                                        prayTimeDao.deletePrayTime()
                                                    }

                                                    prefs.put(ConstPrefs.PROVINCE_NAME, "${cityName?.uppercase()}")
                                                    prefs.put(Constant.CACHE_CLEARED, true)

                                                    insertPrayTimeToDB.isDataFinished.observe(
                                                        viewLifecycleOwner
                                                    ) { boolean ->
                                                        if (boolean == true) {
                                                            Application.context.openActivity(
                                                                MainActivity::class.java
                                                            ) {
                                                                putString(
                                                                    "DistrictId",
                                                                    districtId.toString()
                                                                )
                                                            }
                                                            onDestroy()
                                                        }
                                                    }

                                                    CoroutineScope(Dispatchers.Main).launch {
                                                        insertPrayTimeToDB.insertData(prayTimes!!)
                                                    }

                                                }

                                                is Results.Failure -> {
                                                    Application.context.openActivity(ErrorActivity::class.java)
                                                }
                                            }
                                            println("4 bitti")
                                        }
                                    } else {
                                        Toast.makeText(
                                            requireContext(),
                                            "Konumunuzdan bulunduğunuz ilçe teşhis edilemedi. Lütfen manuel olarak seçim yapınız.",
                                            Toast.LENGTH_LONG
                                        ).show()

                                        manager.inTransaction {
                                            replace(R.id.fragmentContainer, CountryFragment())
                                        }
                                        onDestroy()
                                    }
                                }
                            } else {
                                Toast.makeText(
                                    requireContext(),
                                    "Konumunuzdan bulunduğunuz şehir teşhis edilemedi. Lütfen manuel olarak seçim yapınız.",
                                    Toast.LENGTH_LONG
                                ).show()

                                manager.inTransaction {
                                    replace(R.id.fragmentContainer, CountryFragment())
                                }
                                onDestroy()
                            }
                        }
                    /*} else {
                        Toast.makeText(
                            requireContext(),
                            "Konumunuzdan bulunduğunuz ülke teşhis edilemedi. Lütfen manuel olarak seçim yapınız.",
                            Toast.LENGTH_LONG
                        ).show()

                        manager.inTransaction {
                            replace(R.id.fragmentContainer, CountryFragment())
                        }
                    }*/
                //}
            */
            }

    }

    /*private fun writeToFirebase(){
        val mid = 2
        val mapCity = HashMap<Int, String>()
        apiService.getProvince(mid).enqueue { r1 ->
            when (r1) {
                is Results.Success -> {
                    val provinceData = r1.response.body() as ArrayList<Province>?
                    for (j in provinceData!!) {
                        mapCity.put(j.SehirID!!,j.SehirAdi!!)
                    }
                    //firestore.collection("Cities").document("$mid").set(map as Map<String, Any>)
                }
            }

        }
        val i = 506
            apiService.getDistrict(i).enqueue { r3 ->
                println("3 basladi")
                when (r3) {
                    is Results.Success -> {
                        val districtData = r3.response.body() as ArrayList<District>?
                        val map = HashMap<String, Int>()
                        for (j in districtData!!) {
                            println("ilceadi:"+j.IlceAdi)
                            println("maptenIlceadi:"+mapCity.get(i))
                            println("ilceID:"+j.IlceID)
                                if(j.IlceAdi!! == mapCity.get(i)){

                                    map.put(j.IlceAdi!!, j.IlceID!!)
                                    break
                                }
                        }
                        firestore.collection("Districts").document("$i")
                            .set(map as Map<String, Any>)
                    }
                }
            }
    }*/

    private fun convertCity(city: String) : String{
        return when(city) {
            "adiyaman" -> "adıyaman"
            "ağri" -> "ağrı"
            "aydin" -> "aydın"
            "balikesi̇r" -> "balıkesir"
            "bartin" -> "bartın"
            "çankiri" -> "çankırı"
            "diyarbakir" -> "diyarbakır"
            "elaziğ" -> "elazığ"
            "iğdir" -> "ığdır"
            "iğdır" -> "ığdır"
            "isparta" -> "ısparta"
            "kirikkale" -> "kırıkkale"
            "kirklareli" -> "kırklareli"
            "kirşehir" -> "kırşehir"
            "şanliurfa" -> "şanlıurfa"
            "şirnak" -> "şırnak"
            else -> {
                city
            }
        }
    }


            override fun onDestroy() {
        super.onDestroy()
        timer.cancel()
    }
}