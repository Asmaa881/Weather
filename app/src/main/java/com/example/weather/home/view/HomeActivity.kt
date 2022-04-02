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
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import com.example.weather.R
import com.example.weather.home.alertsfragment.view.AlertsFragment
import com.example.weather.home.favouritefragment.view.FavoriteFragment
import com.example.weather.home.homefragment.view.HomeFragment
import com.example.weather.home.settingfragment.view.SettingFragment
import com.google.android.gms.location.*
import com.google.android.material.navigation.NavigationView
import java.util.*
import kotlin.properties.Delegates

class HomeActivity : AppCompatActivity() {

    lateinit var toggle: ActionBarDrawerToggle
    lateinit var drawerLayout: DrawerLayout
    lateinit var  navView  : NavigationView
    companion object {
        var opendMapFrom = 0
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        drawerLayout = findViewById(R.id.drawer_layout)
        navView = findViewById(R.id.nav_view)

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
                R.id.nav_favorite -> replaceFragments(FavoriteFragment(), it.title.toString())
                R.id.nav_alarts -> replaceFragments(AlertsFragment(), it.title.toString())
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
                replaceFragments(HomeFragment(), "Home")
            }
            // Favorite
            2 -> {
                val bundle: Bundle? = intent.extras
                replaceFragments(FavoriteFragment(), "Favorite")
            }
            // Setting
            3 -> {
            }
        }
    }


    public fun replaceFragments(fragment: Fragment, title: String) {
        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.frameLayout, fragment)
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


}