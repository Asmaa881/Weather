package com.example.weather.home.homefragment.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.weather.home.model.RepositoryInterface
import java.lang.IllegalArgumentException

class HomeViewModelFactory (private val repo: RepositoryInterface, private val lat :Double,
                            private val lon:Double, private val apiKey:String,private val language:String) : ViewModelProvider.Factory{

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return if (modelClass.isAssignableFrom(HomeViewModel::class.java)) {
            HomeViewModel(repo,lat,lon,apiKey,language) as T
        } else {
            throw IllegalArgumentException("ViewModel Class not found")
        }
    }
}

