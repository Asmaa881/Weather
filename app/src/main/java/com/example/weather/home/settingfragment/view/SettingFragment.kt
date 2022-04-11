package com.example.weather.home.settingfragment.view

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.content.res.Configuration
import android.location.Geocoder
import android.location.Location
import android.location.LocationManager
import android.os.Bundle
import android.os.Looper
import android.provider.Settings
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.work.WorkManager
import com.example.weather.R
import com.example.weather.home.homefragment.view.HomeFragment
import com.example.weather.home.homefragment.view.HomeFragment.Companion.sharedPreferences
import com.example.weather.home.view.HomeActivity
import com.example.weather.home.view.MapsActivity
import com.google.android.gms.location.*
import java.util.*


class SettingFragment : Fragment() {

    lateinit var radioGroupLocationOptions :RadioGroup
    lateinit var radioGroupLanguageOptions :RadioGroup
    lateinit var radioGroupWindSpeedOptions :RadioGroup
    lateinit var radioGroupTemperatureOptions :RadioGroup
    lateinit var radioGroupNotificationOptions :RadioGroup
    /////
    lateinit var radioGps: RadioButton
    lateinit var radioMap: RadioButton
    lateinit var radioEnglish: RadioButton
    lateinit var radioArabic: RadioButton
    lateinit var radioMeterPerSec: RadioButton
    lateinit var radioMilePerHour: RadioButton
    lateinit var radioCelsius: RadioButton
    lateinit var radioKelvin: RadioButton
    lateinit var radioFahrenheit: RadioButton
    lateinit var radioEnable: RadioButton
    lateinit var radioDisable: RadioButton
    ///////
    lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    var longitude: Double = 0.0
    var latitude: Double = 0.0
    val PERMISSION_ID: Int = 5
     var tempMeasure: String = ""
    var windSpeedMeasure: String = ""
    var langState = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(requireContext())
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        var view =  inflater.inflate(R.layout.fragment_setting, container, false)
        radioGroupLocationOptions = view.findViewById(R.id.radioGroupLocationOptions)
        radioGroupLanguageOptions = view.findViewById(R.id.radioGroupLanguageOptions)
        radioGroupWindSpeedOptions = view.findViewById(R.id.radioGroupWindSpeedOptions)
        radioGroupTemperatureOptions = view.findViewById(R.id.radioGroupTemperatureOptions)
        radioGroupNotificationOptions = view.findViewById(R.id.radioGroupNotificationOptions)
        ///////////////
        radioGps = view.findViewById(R.id.radioGps)
        radioMap = view.findViewById(R.id.radioMap)
        radioEnglish = view.findViewById(R.id.radioEnglish)
        radioArabic = view.findViewById(R.id.radioArabic)
        radioCelsius = view.findViewById(R.id.radioCelsius)
        radioKelvin = view.findViewById(R.id.radioKelvin)
        radioFahrenheit = view.findViewById(R.id.radioFahrenheit)
        radioEnable = view.findViewById(R.id.radioEnable)
        radioDisable = view.findViewById(R.id.radioDisable)
        radioMeterPerSec = view.findViewById(R.id.radioMeterPerSec)
        radioMilePerHour = view.findViewById(R.id.radioMilePerHour)
        radioEnglish.setOnClickListener {
            setLocale("en")
            loadLocale()
            langState = "en"
        }
        radioArabic.setOnClickListener {
            setLocale("ar")
            loadLocale()
            langState = "ar"
        }
        radioGps.setOnClickListener {
            getLastLocation()
        }
        radioMap.setOnClickListener {
            HomeActivity.opendMapFrom =3
            var intent = Intent(requireContext(), MapsActivity::class.java)
            startActivity(intent)
        }
        radioCelsius.setOnClickListener {
            tempMeasure = "C"
        }
        radioFahrenheit.setOnClickListener {
            tempMeasure = "F"
        }
        radioKelvin.setOnClickListener {
            tempMeasure = "K"
        }
        radioMilePerHour.setOnClickListener {
            windSpeedMeasure = "MPerH"
        }
        radioMeterPerSec.setOnClickListener {
            windSpeedMeasure = "MiPerS"
        }
        radioDisable.setOnClickListener {
            WorkManager.getInstance().cancelAllWork()
        }
        radioEnable.setOnClickListener {}
        return view
    }
    ////// Language //////////
    private fun setLocale(language: String){
        var locale:Locale= Locale(language)
        Locale.setDefault(locale)
        var configuration:Configuration=Configuration()
        configuration.locale=locale
        resources.updateConfiguration(configuration,resources.displayMetrics)
        val sharedPreferences: SharedPreferences = requireActivity().getSharedPreferences("setting_languages",Context.MODE_PRIVATE)
        val editor:SharedPreferences.Editor =  sharedPreferences.edit()
        editor.putString("language",language)
        editor.apply()

    }
    private  fun loadLocale() {
        var sharedPreferences: SharedPreferences =
            requireActivity().getSharedPreferences("setting_languages", Context.MODE_PRIVATE)
        var languages: String? = sharedPreferences.getString("language", "")
        setLocale(languages!!)
    }
    /////////Gps , Map
    private fun isLocationEnabled(): Boolean {
        val locationManager =
            requireContext().getSystemService(AppCompatActivity.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(
            LocationManager.NETWORK_PROVIDER
        )
    }
    private fun checkPermission(): Boolean {
        return ActivityCompat.checkSelfPermission(
            requireContext(),
            Manifest.permission.ACCESS_COARSE_LOCATION
        ) ==
                PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
            requireContext(),
            Manifest.permission.ACCESS_FINE_LOCATION
        ) ==
                PackageManager.PERMISSION_GRANTED
    }
    fun requestPermission() {
        ActivityCompat.requestPermissions(
            requireActivity(),
            arrayOf(
                android.Manifest.permission.ACCESS_COARSE_LOCATION,
                android.Manifest.permission.ACCESS_FINE_LOCATION
            ), PERMISSION_ID
        )
    }
    @SuppressLint("MissingPermission")
    fun NewLocationData() {
        var locationRequest = LocationRequest()
        locationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        locationRequest.interval = 0
        locationRequest.fastestInterval = 0
        locationRequest.numUpdates = 1
        fusedLocationProviderClient =
            LocationServices.getFusedLocationProviderClient(requireContext())
        fusedLocationProviderClient.requestLocationUpdates(
            locationRequest, locationCallback,
            Looper.myLooper()!!
        )
    }
    private val locationCallback = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult) {
            var lastLocation: Location = locationResult.lastLocation
            Log.d("Debug:", "your last last location: " + lastLocation.longitude.toString())
            Toast.makeText(
                requireContext(),
                "You Last Location is : Long: " + lastLocation.longitude + " , Lat: " + lastLocation.latitude + "\n" +
                        getCityName(lastLocation.latitude, lastLocation.longitude),
                Toast.LENGTH_LONG
            ).show()
            longitude = lastLocation.longitude
            latitude = lastLocation.latitude
        }
    }
    fun getLastLocation() {
        if (checkPermission()) {
            if (isLocationEnabled()) {
                fusedLocationProviderClient.lastLocation.addOnCompleteListener { task ->
                    var location: Location? = task.result
                    if (location == null) {
                        NewLocationData()
                    } else {
                        Log.d("Debug:", "Your Location:" + location.longitude)
                        Toast.makeText(
                            requireContext(),
                            "You Current Location is : Long: " + location.longitude + " , Lat: " + location.latitude + "\n"
                                    + getCityName(location.latitude, location.longitude),
                            Toast.LENGTH_SHORT
                        ).show()
                        longitude = location.longitude
                        latitude = location.latitude
                    }
                }
            } else {
                Toast.makeText(requireContext(), "Please Turn on Your device Location", Toast.LENGTH_SHORT).show()
                val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                startActivity(intent)
            }
        } else {
            requestPermission()
        }
    }
    private fun getCityName(lat: Double, long: Double): String {
        var cityName: String = ""
        var geoCoder = Geocoder(requireContext(), Locale.getDefault())
        var Adress = geoCoder.getFromLocation(lat, long, 3)
        cityName = Adress.get(0).adminArea
        Log.d("Debug:", "Your City: " + cityName)
        return cityName
    }

    override fun onStop() {
        super.onStop()
          if (latitude != 0.0 && longitude != 0.0) {
             HomeFragment.sharedPreferences.edit().putString("latitude", latitude.toString())
                 .commit();
             HomeFragment.sharedPreferences.edit().putString("longtitude", longitude.toString())
                 .commit();
        }
        if(tempMeasure!= "") {
            sharedPreferences.edit().putString("tempMeasure", tempMeasure).commit()
        }
        if(windSpeedMeasure != ""){
            sharedPreferences.edit().putString("windSpeed", windSpeedMeasure).commit()
        }
        if(langState!= ""){
            sharedPreferences.edit().putString("langState", langState).commit()
        }
    }


}