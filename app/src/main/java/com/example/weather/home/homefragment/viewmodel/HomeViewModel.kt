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

class HomeViewModel (iRepo: RepositoryInterface, var lat :Double, var lon:Double, var apiKey:String,var language:String) : ViewModel(){

    private val _iRepo : RepositoryInterface = iRepo
    private val weatherList = MutableLiveData<List<ResponseModel>>()

    init {
        getWeather()
    }
    //Expose returned online Data
    val onlineWeather: LiveData<List<ResponseModel>> = weatherList
    fun getWeather(){
        viewModelScope.launch{
            val weather = _iRepo.getNetworkWeather(lat, lon,apiKey,language)
            withContext(Dispatchers.Main){
                weatherList.postValue(listOf(weather))
            }
        }
    }

    //// weather info
    fun saveWeatherToRoom(responseModel: ResponseModel) {
        viewModelScope.launch(Dispatchers.IO){
            _iRepo.insertWeather(responseModel)
        }
    }
    fun getWeatherFromRoom(): LiveData<ResponseModel> {
        return _iRepo.storedWeather
    }




    override fun onCleared() {
        super.onCleared()
    }


}