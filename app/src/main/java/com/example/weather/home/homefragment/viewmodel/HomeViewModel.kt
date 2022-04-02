package com.example.weather.home.homefragment.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weather.home.model.RepositoryInterface
import com.example.weather.home.model.ResponseModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class HomeViewModel (iRepo: RepositoryInterface, var lat :Double, var lon:Double, var apiKey:String) : ViewModel(){

    private val _iRepo : RepositoryInterface = iRepo
    private val _movieList = MutableLiveData<List<ResponseModel>>()

    init {
        getWeather()
    }
    //Expose returned online Data
    val onlineWeather: LiveData<List<ResponseModel>> = _movieList
    fun getWeather(){
        viewModelScope.launch{
            val weather = _iRepo.getNetworkWeather(lat, lon,apiKey)
            withContext(Dispatchers.Main){
                //Log.i("TAG", "view Model: ${weather}")
                _movieList.postValue(listOf(weather))
            }
        }
    }


    override fun onCleared() {
        super.onCleared()
    }


}