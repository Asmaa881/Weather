package com.example.weather.home.favouritefragment.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.weather.home.homefragment.viewmodel.HomeViewModel
import com.example.weather.home.model.RepositoryInterface
import java.lang.IllegalArgumentException

class FavoriteViewModelFactory (private val repo: RepositoryInterface, private val lat :Double,
                                private val lon:Double, private val apiKey:String) : ViewModelProvider.Factory{
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return if (modelClass.isAssignableFrom(FavoriteViewModel::class.java)) {
            FavoriteViewModel(repo,lat,lon,apiKey) as T
        } else {
            throw IllegalArgumentException("ViewModel Class not found")
        }
    }
}