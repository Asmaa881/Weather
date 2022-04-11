package com.example.weather.home.view

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.Location
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.lifecycle.ViewModelProvider
import com.example.weather.R
import com.example.weather.home.db.ConcreteLocalSource
import com.example.weather.home.favouritefragment.viewmodel.FavoriteViewModel
import com.example.weather.home.favouritefragment.viewmodel.FavoriteViewModelFactory
import com.example.weather.home.homefragment.view.HomeFragment
import com.example.weather.home.model.FavoriteStored
import com.example.weather.home.model.WeatherRepository
import com.example.weather.home.network.WeatherClient
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import java.util.*

class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    var mMap: GoogleMap? = null
    private var fusedLocationProviderClient: FusedLocationProviderClient? = null
    var currentLocation : Location?= null
    var currentMarker: Marker? = null

    var cityName: String? = null
    lateinit var favFactory: FavoriteViewModelFactory
    lateinit var favViewModel: FavoriteViewModel
    lateinit var favoriteStored: FavoriteStored

    companion object{
        var lat :Double = 0.0
        var lon : Double = 0.0
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)
        fetchLocation()
    }
    private fun fetchLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
            != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), 100)
            return
        }
       val task = fusedLocationProviderClient?.lastLocation
        task?.addOnSuccessListener {location ->
            if(location != null){
                this.currentLocation = location
               val mapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
                mapFragment.getMapAsync(this)
            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        when (requestCode) {
            1000 -> if (grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                fetchLocation()
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
       // val latLng = LatLng(currentLocation?.latitude!!,currentLocation?.longitude!!)
       // drawMarker(latLng)

        mMap?.setOnMapClickListener(object : GoogleMap.OnMapClickListener{
            override fun onMapClick(p0: LatLng) {
                if(currentMarker != null){
                    currentMarker?.remove()
                }
                val newLatLng = LatLng(p0.latitude,p0.longitude)
                drawMarker(newLatLng)
            }
        })
    }

    private fun drawMarker(latLng: LatLng){
      val markerOptions=  MarkerOptions().position(latLng).title("Your Location")
            .snippet(getAddress(latLng.latitude, latLng.longitude)).draggable(true)
        mMap?.animateCamera(CameraUpdateFactory.newLatLng(latLng))
        mMap?.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng,15f))
        currentMarker=  mMap?.addMarker(markerOptions)
        currentMarker?.showInfoWindow()
        lat = latLng.latitude
        lon = latLng.longitude
        var geoCoder = Geocoder(this, Locale.getDefault())
        var Adress = geoCoder.getFromLocation(lat!!, lon!!,2)
        cityName = "${Adress.get(0).adminArea}"

        Toast.makeText(this,"${lat}, $lon",Toast.LENGTH_SHORT).show()
            when(HomeActivity.opendMapFrom){
                // Home
                1 -> {
                    val intent = Intent(this, HomeActivity()::class.java)
                    intent.putExtra("HomeLat",lat)
                    intent.putExtra("HomeLon",lon)
                    Log.i("latlon", "${lat}, ${lon}")
                    startActivity(intent)
                }
                // Favorite
                2 -> {
                    favFactory = FavoriteViewModelFactory(WeatherRepository.getInstance(WeatherClient.getInstance(),
                        ConcreteLocalSource(this), this), lat!!, lon!!,"4fb359fbc087d60f9071faa71d7f6fbf")
                    favViewModel = ViewModelProvider(this, favFactory).get(FavoriteViewModel::class.java)
                     favoriteStored= FavoriteStored(lat,lon, cityName!!)
                    favViewModel.insertToFav(favoriteStored)
                    Toast.makeText(this, "Weather added!", Toast.LENGTH_SHORT).show()
                    val intent = Intent(this, HomeActivity()::class.java)
                    startActivity(intent)
                }
                // Setting
                3 -> {
                    finish()
                }
                // Alerts
                4 ->{
                   finish()
                }
            }
    }

    private fun getAddress(lat:Double, lon: Double): String?{
        val geocoder= Geocoder(this, Locale.getDefault())
        val address = geocoder.getFromLocation(lat,lon,1)
       return address[0].getAddressLine(0).toString()
    }

    override fun onStop() {
        super.onStop()
        if (HomeActivity.opendMapFrom==3 && lat != 0.0 && lon != 0.0) {
            HomeFragment.sharedPreferences.edit().putString("latitude", lat.toString())
                .commit();
            HomeFragment.sharedPreferences.edit().putString("longtitude", lon.toString())
                .commit();
        }
    }

    }


