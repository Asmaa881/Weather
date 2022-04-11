package com.example.weather.home.view

import android.Manifest
import android.annotation.SuppressLint
import android.app.AlertDialog
import android.app.Dialog
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.location.Geocoder
import android.location.Location
import android.location.LocationManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Looper
import android.provider.Settings
import android.util.Log
import android.view.MenuItem
import android.widget.Button
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.app.ActivityCompat
import androidx.core.location.LocationManagerCompat.isLocationEnabled
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.NavGraph
import androidx.navigation.NavHostController
import androidx.navigation.fragment.NavHostFragment
import androidx.work.OneTimeWorkRequest
import com.example.weather.R
import com.example.weather.home.alertsfragment.view.AlertDialogFragment
import com.example.weather.home.alertsfragment.view.AlertsFragment
import com.example.weather.home.favouritefragment.view.CommunicatorInterface
import com.example.weather.home.favouritefragment.view.FavoriteDetailsFragment
import com.example.weather.home.favouritefragment.view.FavoriteFragment
import com.example.weather.home.homefragment.view.HomeFragment
import com.example.weather.home.model.ResponseModel
import com.example.weather.home.settingfragment.view.SettingFragment
import com.google.android.gms.location.*
import com.google.android.material.navigation.NavigationView
import java.util.*
import kotlin.collections.ArrayList
import kotlin.properties.Delegates

class HomeActivity : AppCompatActivity(), CommunicatorInterface {

    lateinit var toggle: ActionBarDrawerToggle
    lateinit var drawerLayout: DrawerLayout
    lateinit var  navView  : NavigationView

    companion object {
        var opendMapFrom = 0
        lateinit var requests: ArrayList<OneTimeWorkRequest>
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        drawerLayout = findViewById(R.id.drawer_layout)
        navView = findViewById(R.id.nav_view)
        requests = ArrayList<OneTimeWorkRequest>()

        checkFragmentState()
        setNavigationDrawer()
    }

    private fun setNavigationDrawer(){
        toggle = ActionBarDrawerToggle(this, drawerLayout, R.string.open, R.string.close)
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        navView.setNavigationItemSelectedListener {
            it.isChecked = true
            when (it.itemId) {
                R.id.nav_home -> replaceFragments(HomeFragment(), it.title.toString())
                R.id.nav_favorite ->{
                   replaceFragments(FavoriteFragment(), it.title.toString())
                }
                R.id.nav_alarts ->{
                    replaceFragments(AlertsFragment(), it.title.toString())
                }
                R.id.nav_settings -> replaceFragments(SettingFragment(), it.title.toString())
                R.id.nav_share -> {
                    var txtShare: String = "Weather App"
                    var shareLink: String = "http://play.google.com/store/apps/details?id=com.example.weatherapp"
                    var share = Intent(Intent.ACTION_SEND)
                    share.type = "text/plain"
                    share.putExtra(Intent.EXTRA_TEXT, "${txtShare} \n${shareLink}")
                    startActivity(share)
                }
            }
            true
        }
    }
    private fun checkFragmentState() {
        when (opendMapFrom) {
            0 -> {
                replaceFragments(HomeFragment(), "Home")
            }
            // Home
            1 -> {
                val bundle: Bundle? = intent.extras
                val lat: Double = intent.getDoubleExtra("HomeLat", 0.0)
                val lon: Double = intent.getDoubleExtra("HomeLon", 0.0)
                Log.i("ttttt","$lat , $lon")
                val mBundle = Bundle()
                var homeFragment: HomeFragment = HomeFragment()
                mBundle.putDouble("lat",lat)
                mBundle.putDouble("lon",lon)
                homeFragment.arguments = mBundle
                replaceFragments(homeFragment, "Home")
            }
            // Favorite
            2 -> {
               replaceFragments(FavoriteFragment(), "Favorite")
            }
        }
    }
     fun replaceFragments(fragment: Fragment, title: String) {
        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.frameLayoutContainer, fragment)
        fragmentTransaction.commit()
        drawerLayout.closeDrawers()
        setTitle(title)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        if (toggle.onOptionsItemSelected(item)) {
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    override fun passData(lat: Double, lon: Double) {
        val bundle = Bundle()
        bundle.putDouble("lat",lat)
        bundle.putDouble("lon",lon)
        val transaction=this.supportFragmentManager.beginTransaction()
        val favoriteDetailsFragment=FavoriteDetailsFragment()
        favoriteDetailsFragment.arguments=bundle
        transaction.replace(R.id.frameLayoutContainer,favoriteDetailsFragment).addToBackStack(null)
        transaction.commit()
    }
    override fun goToAlertDialog() {
        val transaction=this.supportFragmentManager.beginTransaction()
        val alertDialogFragment=AlertDialogFragment()
        transaction.replace(R.id.frameLayoutContainer,alertDialogFragment).addToBackStack(null)
        transaction.commit()
    }

    override fun backToAlertFragment() {
        val transaction=this.supportFragmentManager.beginTransaction()
        val alertsFragment=AlertsFragment()
        transaction.replace(R.id.frameLayoutContainer,alertsFragment).addToBackStack(null)
        transaction.commit()
    }
}