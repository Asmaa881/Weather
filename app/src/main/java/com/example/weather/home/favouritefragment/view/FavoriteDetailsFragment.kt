package com.example.weather.home.favouritefragment.view

import android.app.Dialog
import android.location.Geocoder
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.weather.R
import com.example.weather.home.db.ConcreteLocalSource
import com.example.weather.home.homefragment.view.HomeFragment
import com.example.weather.home.homefragment.view.HourlyRecyclerAdapter
import com.example.weather.home.homefragment.view.SevenDaysRecyclerAdapter
import com.example.weather.home.homefragment.viewmodel.HomeViewModel
import com.example.weather.home.homefragment.viewmodel.HomeViewModelFactory
import com.example.weather.home.model.WeatherRepository
import com.example.weather.home.network.InternetConnectionChecker
import com.example.weather.home.network.WeatherClient
import com.google.android.gms.location.FusedLocationProviderClient
import java.util.*

class FavoriteDetailsFragment : Fragment() {

    lateinit var homeViewModel: HomeViewModel
    lateinit var homeViewModelFactory: HomeViewModelFactory
    lateinit var txtFavCityName: TextView
    lateinit var txtFavCurrentDateTime: TextView
    lateinit var txtFavWeatherDescription: TextView
    lateinit var txtFavTemperature: TextView
    lateinit var favNextTwentyFourHoursRecyclerView: RecyclerView
    lateinit var hourlyRecyclerAdapter: HourlyRecyclerAdapter
    lateinit var linearLayoutManager: LinearLayoutManager
    lateinit var favNextSevenDaysRecyclerView: RecyclerView
    lateinit var sevenDaysRecyclerAdapter: SevenDaysRecyclerAdapter
    lateinit var linearLayoutManagerVertical: LinearLayoutManager
    lateinit var txtFavWindSpeed: TextView
    lateinit var txtFavPressure: TextView
    lateinit var txtFavClouds: TextView
    lateinit var txtFavHumidity: TextView
    lateinit var txtFavUltraViolet: TextView
    lateinit var txtFavVisibility: TextView
    lateinit var fav_weather_icon: ImageView

    lateinit var day: String
    lateinit var date: String
    lateinit var time: String

    lateinit var internetConnectionChecker: InternetConnectionChecker
    var longitude: Double = 0.0
    var latitude: Double = 0.0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        internetConnectionChecker = InternetConnectionChecker(requireContext())
        getCurrentDateAndTime()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
       var view = inflater.inflate(R.layout.fragment_favorite_details, container, false)
        txtFavCityName = view.findViewById(R.id.txtFavCityName)
        txtFavCurrentDateTime = view.findViewById(R.id.txtFavCurrentDateTime)
        txtFavWeatherDescription = view.findViewById(R.id.txtFavWeatherDescription)
        txtFavTemperature = view.findViewById(R.id.txtFavTemperature)
        txtFavWindSpeed = view.findViewById(R.id.txtFavWindSpeed)
        txtFavPressure = view.findViewById(R.id.txtFavPressure)
        txtFavClouds = view.findViewById(R.id.txtFavClouds)
        txtFavHumidity = view.findViewById(R.id.txtFavHumidity)
        txtFavUltraViolet = view.findViewById(R.id.txtFavUltraViolet)
        txtFavVisibility = view.findViewById(R.id.txtFavVisibility)
        fav_weather_icon = view.findViewById(R.id.fav_weather_icon)
        favNextTwentyFourHoursRecyclerView = view.findViewById(R.id.favNextTwentyFourHoursRecyclerView)
        favNextSevenDaysRecyclerView = view.findViewById(R.id.favNextSevenDaysRecyclerView)
        //// Set Hourly Recycler Adapter /////
        linearLayoutManager = LinearLayoutManager(requireContext())
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL)
        favNextTwentyFourHoursRecyclerView.setLayoutManager(linearLayoutManager)
        hourlyRecyclerAdapter = HourlyRecyclerAdapter()
        hourlyRecyclerAdapter.notifyDataSetChanged()
        favNextTwentyFourHoursRecyclerView.setAdapter(hourlyRecyclerAdapter)
        //// Set Days Recycler Adapter //////
        linearLayoutManagerVertical = LinearLayoutManager(requireContext())
        linearLayoutManagerVertical.setOrientation(LinearLayoutManager.VERTICAL)
        favNextSevenDaysRecyclerView.setLayoutManager(linearLayoutManagerVertical)
        sevenDaysRecyclerAdapter = SevenDaysRecyclerAdapter()
        sevenDaysRecyclerAdapter.notifyDataSetChanged()
        favNextSevenDaysRecyclerView.setAdapter(sevenDaysRecyclerAdapter)

        if (internetConnectionChecker.isConnected()) {
            val bundle = arguments
            if (bundle != null) {
                latitude = bundle!!.getDouble("lat")
                longitude = bundle!!.getDouble("lon")
                setDataToViews();
            }
        } else {
            Toast.makeText(requireContext(), "No Internet..... \n Please Check Your internet..", Toast.LENGTH_SHORT).show()
        }


        return view
    }


    private fun setDataToViews() {
        homeViewModelFactory = HomeViewModelFactory(
            WeatherRepository.getInstance(WeatherClient.getInstance(), ConcreteLocalSource(requireContext()), requireContext()
            ), latitude, longitude, "4fb359fbc087d60f9071faa71d7f6fbf", HomeFragment.sharedPreferences.getString("langState","en")!!)
        homeViewModel = ViewModelProvider(this, homeViewModelFactory)
            .get(HomeViewModel::class.java)
        if (internetConnectionChecker.isConnected()) {
            homeViewModel.getWeather()
            homeViewModel.onlineWeather.observe(this) { weather ->
                txtFavCityName.text = getCityName(latitude, longitude)
                txtFavCurrentDateTime.text = "$date \n  $time \n$day"
                txtFavTemperature.text = "${(((weather[0].current?.temp!! - 273.25) * (9 / 5)) + 32).toInt()} \u2109"
                txtFavWeatherDescription.text =
                    weather[0].current?.weather?.get(0)?.description.toString()
                txtFavClouds.text = "${weather[0].current?.clouds.toString()} %"
                txtFavHumidity.text = "${weather[0].current?.humidity.toString()}"
                txtFavPressure.text = "${weather[0].current?.pressure.toString()} hpa"
                txtFavWindSpeed.text = "${weather[0].current?.wind_speed.toString()} m/s"
                txtFavUltraViolet.text = "${weather[0].current?.uvi.toString()}"
                txtFavVisibility.text = "${weather[0].current?.visibility.toString()} m"
                var iconLink = "https://openweathermap.org/img/w/${weather[0].current?.weather!![0].icon}.png"
                Glide.with(requireContext()).load(iconLink).error(R.drawable.icon)
                    .into(fav_weather_icon)
                if (weather != null) {
                    hourlyRecyclerAdapter.setData(requireContext(), weather[0].hourly)
                    sevenDaysRecyclerAdapter.setData(requireContext(), weather[0].daily)

                }
            }
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

    private fun getCurrentDateAndTime() {
        val c = Calendar.getInstance()
        val currentDay = c[Calendar.DAY_OF_MONTH]
        val currentMonth = c[Calendar.MONTH] + 1
        val currentYear = c[Calendar.YEAR]
        val currentHours = c[Calendar.HOUR]
        val currentMinutes = c[Calendar.MINUTE]
        val currentSeconds = c[Calendar.SECOND]
        val days = arrayOf("Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday")
        date = "$currentDay/$currentMonth/$currentYear"
        time = "$currentHours:$currentMinutes:$currentSeconds"
        day = days[c.get(Calendar.DAY_OF_WEEK) - 1]
    }




}