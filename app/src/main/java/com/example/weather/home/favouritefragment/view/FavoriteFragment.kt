package com.example.weather.home.favouritefragment.view

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.weather.R
import com.example.weather.home.favouritefragment.viewmodel.FavoriteViewModel
import com.example.weather.home.favouritefragment.viewmodel.FavoriteViewModelFactory
import com.example.weather.home.model.FavoriteStored
import com.example.weather.home.view.HomeActivity
import com.example.weather.home.view.MapsActivity
import com.google.android.material.floatingactionbutton.FloatingActionButton

class FavoriteFragment : Fragment() {


    lateinit var favViewModel: FavoriteViewModel
    lateinit var favFactory: FavoriteViewModelFactory

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        var view =inflater.inflate(R.layout.fragment_favorite, container, false)
        var btnOpenMapToChooseFavLocation:FloatingActionButton = view.findViewById(R.id.btnOpenMapToChooseFavLocation)



        btnOpenMapToChooseFavLocation.setOnClickListener {

            HomeActivity.opendMapFrom =2
            var intent = Intent(requireContext(),MapsActivity::class.java)
            startActivity(intent)
        }
        return view
    }

}