package com.example.weather.home.homefragment.view

import android.Manifest
import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context.MODE_PRIVATE
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
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
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.weather.R
import com.example.weather.home.db.ConcreteLocalSource
import com.example.weather.home.homefragment.viewmodel.HomeViewModel
import com.example.weather.home.homefragment.viewmodel.HomeViewModelFactory
import com.example.weather.home.model.*
import com.example.weather.home.network.InternetConnectionChecker
import com.example.weather.home.network.WeatherClient
import com.example.weather.home.view.HomeActivity
import com.example.weather.home.view.MapsActivity
import com.google.android.gms.location.*
import java.util.*

class HomeFragment : Fragment() {
    lateinit var homeViewModel: HomeViewModel
    lateinit var homeViewModelFactory: HomeViewModelFactory
    lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    lateinit var dialog: Dialog
    lateinit var radioGroupOptions: RadioGroup
    lateinit var radioGps: RadioButton
    lateinit var radioMap: RadioButton
    lateinit var dialogOkBtn: Button
    lateinit var txtCityName: TextView
    lateinit var txtCurrentDateTime: TextView
    lateinit var txtWeatherDescription: TextView
    lateinit var txtTemperature: TextView
    lateinit var nextTwentyFourHoursRecyclerView: RecyclerView
    lateinit var hourlyRecyclerAdapter: HourlyRecyclerAdapter
    lateinit var linearLayoutManager: LinearLayoutManager
    lateinit var nextSevenDaysRecyclerView: RecyclerView
    lateinit var sevenDaysRecyclerAdapter: SevenDaysRecyclerAdapter
    lateinit var linearLayoutManagerVertical: LinearLayoutManager
    lateinit var txtWindSpeed: TextView
    lateinit var txtPressure: TextView
    lateinit var txtClouds: TextView
    lateinit var txtHumidity: TextView
    lateinit var txtUltraViolet: TextView
    lateinit var txtVisibility: TextView
    lateinit var weather_icon: ImageView
    lateinit var rlLayout: RelativeLayout
    lateinit var day: String
    lateinit var date: String
    lateinit var time: String
    var longitude: Double = 0.0
    var latitude: Double = 0.0
    val PERMISSION_ID: Int = 5
    lateinit var tempMeasure:String
    lateinit var windSpeed: String

    lateinit var internetConnectionChecker: InternetConnectionChecker
    companion object{
        lateinit var sharedPreferences: SharedPreferences

}

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        internetConnectionChecker = InternetConnectionChecker(requireContext())
        dialog = Dialog(requireContext())
        fusedLocationProviderClient =
            LocationServices.getFusedLocationProviderClient(requireContext())
        getCurrentDateAndTime()
        sharedPreferences = requireContext().getSharedPreferences(
            "com.example.weather.home.homefragment.view", MODE_PRIVATE)
    }

    @SuppressLint("FragmentLiveDataObserve")
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        var view: View = inflater.inflate(R.layout.fragment_home, container, false)

        /////////////Define Variables///////////////
        rlLayout = view.findViewById(R.id.rlLayout)
        txtCityName = view.findViewById(R.id.txtCityName)
        txtCurrentDateTime = view.findViewById(R.id.txtCurrentDateTime)
        txtWeatherDescription = view.findViewById(R.id.txtWeatherDescription)
        txtTemperature = view.findViewById(R.id.txtTemperature)
        txtWindSpeed = view.findViewById(R.id.txtWindSpeed)
        txtPressure = view.findViewById(R.id.txtPressure)
        txtClouds = view.findViewById(R.id.txtClouds)
        txtHumidity = view.findViewById(R.id.txtHumidity)
        txtUltraViolet = view.findViewById(R.id.txtUltraViolet)
        txtVisibility = view.findViewById(R.id.txtVisibility)
        weather_icon = view.findViewById(R.id.weather_icon)
        nextTwentyFourHoursRecyclerView = view.findViewById(R.id.nextTwentyFourHoursRecyclerView)
        nextSevenDaysRecyclerView = view.findViewById(R.id.nextSevenDaysRecyclerView)
        //// Set Hourly Recycler Adapter /////
        linearLayoutManager = LinearLayoutManager(requireContext())
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL)
        nextTwentyFourHoursRecyclerView.setLayoutManager(linearLayoutManager)
        hourlyRecyclerAdapter = HourlyRecyclerAdapter()
        hourlyRecyclerAdapter.notifyDataSetChanged()
        nextTwentyFourHoursRecyclerView.setAdapter(hourlyRecyclerAdapter)
        //// Set Days Recycler Adapter //////
        linearLayoutManagerVertical = LinearLayoutManager(requireContext())
        linearLayoutManagerVertical.setOrientation(LinearLayoutManager.VERTICAL)
        nextSevenDaysRecyclerView.setLayoutManager(linearLayoutManagerVertical)
        sevenDaysRecyclerAdapter = SevenDaysRecyclerAdapter()
        sevenDaysRecyclerAdapter.notifyDataSetChanged()
        nextSevenDaysRecyclerView.setAdapter(sevenDaysRecyclerAdapter)

        //////////////////
        if (internetConnectionChecker.isConnected()) {
            val bundle = arguments
            if (bundle != null) {
                latitude = bundle!!.getDouble("lat")
                longitude = bundle!!.getDouble("lon")
                sharedPreferences.edit().putString("latitude", latitude.toString()).commit();
                sharedPreferences.edit().putString("longtitude", longitude.toString()).commit();
                setDataToViews();
            } else {
                if (sharedPreferences.getBoolean("firstOne", true) == true) {
                    openDialog()
                } else {
                    Toast.makeText(requireContext(), "Not First Time", Toast.LENGTH_SHORT).show()
                    latitude = sharedPreferences.getString("latitude", "0.0")!!.toDouble()
                    longitude = sharedPreferences.getString("longtitude", "0.0")!!.toDouble()
                    setDataToViews()
                }
            }
        } else {
            Toast.makeText(requireContext(), "No Internet", Toast.LENGTH_SHORT).show()
            txtWeatherDescription.text = "No Internet.... \n Please check your connection.."
            //setDataToViews()
        }
        return view
    }

    private fun setDataToViews() {
            homeViewModelFactory = HomeViewModelFactory(
                WeatherRepository.getInstance(
                    WeatherClient.getInstance(), ConcreteLocalSource(requireContext()), requireContext()
                ), latitude, longitude, "4fb359fbc087d60f9071faa71d7f6fbf",
                sharedPreferences.getString("langState","en")!!
            )
        homeViewModel = ViewModelProvider(this, homeViewModelFactory)
            .get(HomeViewModel::class.java)
        if (internetConnectionChecker.isConnected()) {
            homeViewModel.getWeather()
            //Observe exposed data of viewModel
            homeViewModel.onlineWeather.observe(this) { weather ->
                Log.i("TAG", "onCreate: ${weather[0].lat}")
                txtCityName.text = getCityName(latitude, longitude)
                txtCurrentDateTime.text = "$date \n  $time \n$day"

                if(sharedPreferences.getString("tempMeasure","")== "C"){
                    tempMeasure =
                        "${(weather[0].current?.temp!! - 273.15).toInt()} \u2103"
                }
                else if(sharedPreferences.getString("tempMeasure","")== "F"){
                    tempMeasure =
                        "${(((weather[0].current?.temp!! - 273.15) * (9 / 5)) + 32).toInt()} \u2109"
                }
                else if(sharedPreferences.getString("tempMeasure","")== "K"){
                    tempMeasure =
                        "${(weather[0].current?.temp!!.toInt())} K"
                }
                else{
                        tempMeasure = "${(((weather[0].current?.temp!! - 273.15) * (9 / 5)) + 32).toInt()} \u2109"
                }
                txtTemperature.text = tempMeasure
                txtWeatherDescription.text = weather[0].current?.weather?.get(0)?.description.toString()

                txtClouds.text = "${weather[0].current?.clouds.toString()} %"
                txtHumidity.text = "${weather[0].current?.humidity.toString()}"
                txtPressure.text = "${weather[0].current?.pressure.toString()} hpa"

                if(sharedPreferences.getString("windSpeed","")== "MiPerS"){
                    windSpeed = "\n ${weather[0].current?.wind_speed.toString()} m/s"
                }
                else if(sharedPreferences.getString("windSpeed","")== "MPerH"){
                    windSpeed = "\n ${weather[0].current?.wind_speed.toString()} m/h"
                }
                else{
                    windSpeed = "\n ${weather[0].current?.wind_speed.toString()} m/s"
                }
               // txtWindSpeed.append(windSpeed)
                txtWindSpeed.text = "$windSpeed"
                txtUltraViolet.text = "${weather[0].current?.uvi.toString()}"
                txtVisibility.text = "${weather[0].current?.visibility.toString()} m"
              //  txtUltraViolet.append("\n ${weather[0].current?.uvi.toString()}")
              //  txtVisibility.append("\n ${weather[0].current?.visibility.toString()} m")
                var iconLink = "https://openweathermap.org/img/w/${weather[0].current?.weather!![0].icon}.png"
                Glide.with(requireContext()).load(iconLink).error(R.drawable.icon)
                    .into(weather_icon)
                if (weather != null) {
                    hourlyRecyclerAdapter.setData(requireContext(), weather[0].hourly)
                    sevenDaysRecyclerAdapter.setData(requireContext(), weather[0].daily)
                    //  if (weather[0].alerts== null){
                    //      weather[0].alerts = ArrayList()
                    //   }
                    //     else{
                        //   homeViewModel.saveWeatherToRoom(weather[0])
                  //  }
                }
            }
        } else {
            homeViewModel.getWeatherFromRoom().observe(requireActivity()) { getWeatherFromRoom ->
                Log.i("offline", "Observation: ${getWeatherFromRoom}")
                txtCityName.text = getCityName(latitude, longitude)
                txtCurrentDateTime.text = "$date \n  $time \n$day"
                txtTemperature.text =
                    "${(((getWeatherFromRoom.current?.temp!! - 273.25) * (9 / 5)) + 32).toInt()} \u2109"
                txtWeatherDescription.text =
                    getWeatherFromRoom.current?.weather?.get(0)?.description.toString()
                // °
                txtClouds.text = "${getWeatherFromRoom.current?.clouds.toString()} %"
                txtHumidity.text = "${getWeatherFromRoom.current?.humidity.toString()}"
                txtPressure.text = "${getWeatherFromRoom.current?.pressure.toString()} hpa"
                txtWindSpeed.text = "${getWeatherFromRoom.current?.wind_speed.toString()} m/s"
                txtUltraViolet.text = "${getWeatherFromRoom.current?.uvi.toString()}"
                txtVisibility.text = "${getWeatherFromRoom.current?.visibility.toString()} m"
                var iconLink =
                    "https://openweathermap.org/img/w/${getWeatherFromRoom.current?.weather!![0].icon}.png"
                // Glide.with(requireContext()).load(iconLink).error(R.drawable.icon)
                //     .into(weather_icon)
                if (getWeatherFromRoom != null) {
                    hourlyRecyclerAdapter.setData(requireContext(), getWeatherFromRoom.hourly)
                    sevenDaysRecyclerAdapter.setData(requireContext(), getWeatherFromRoom.daily)
                }
            }
        }
    }

    private fun openDialog() {
        dialog.setContentView(R.layout.location_dialog)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        radioGroupOptions = dialog.findViewById(R.id.radioGroupOptions)
        radioGps = dialog.findViewById(R.id.radioGps)
        radioMap = dialog.findViewById(R.id.radioMap)
        dialogOkBtn = dialog.findViewById(R.id.dialogOkBtn)

        dialogOkBtn.setOnClickListener {
            dialog.dismiss()
            setDataToViews()
        }
        radioGps.setOnClickListener {
            getLastLocation()
        }
        radioMap.setOnClickListener {
            HomeActivity.opendMapFrom = 1
            val intent = Intent(requireContext(), MapsActivity()::class.java)
            startActivity(intent)
        }
        dialog.show()
    }


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
        var adress = geoCoder.getFromLocation(lat, long, 3)
        cityName = adress.get(0).adminArea
        Log.d("Debug:", "Your City: " + cityName)
        return cityName
    }
    private fun getCurrentDateAndTime() {
        val c = Calendar.getInstance()
        val currentDay = c[Calendar.DAY_OF_MONTH]
        val currentMonth = c[Calendar.MONTH] + 1
        val currentYear = c[Calendar.YEAR]
        val currentHours = c[Calendar.HOUR]
        val currentMinutes = c[Calendar.MINUTE]
        val currentSeconds = c[Calendar.SECOND]
        val days =
            arrayOf("Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday")
        date = "$currentDay/$currentMonth/$currentYear"
        time = "$currentHours:$currentMinutes:$currentSeconds"
        day = days[c.get(Calendar.DAY_OF_WEEK) - 1]
    }

    override fun onStop() {
        super.onStop()
        if (sharedPreferences.getBoolean("firstOne", true)) {
            sharedPreferences.edit().putString("latitude", latitude.toString()).commit();
            sharedPreferences.edit().putString("longtitude", longitude.toString()).commit();
            sharedPreferences.edit().putBoolean("firstOne", false).commit();

        }
    }

}