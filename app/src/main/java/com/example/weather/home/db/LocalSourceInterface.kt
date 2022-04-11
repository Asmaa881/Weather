package com.example.weather.home.db

import androidx.lifecycle.LiveData
import com.example.weather.home.model.Alerts
import com.example.weather.home.model.AlertsStored
import com.example.weather.home.model.FavoriteStored
import com.example.weather.home.model.ResponseModel

interface LocalSourceInterface {

    ///// Favorite
    fun insertToFav(favoriteStored: FavoriteStored)
    fun deleteMovie(favoriteStored: FavoriteStored)
    val allStoredCity: LiveData<List<FavoriteStored>>

    //// Weather Data
    fun saveWeather(responseModel: ResponseModel)
    val weatherStored: LiveData<ResponseModel>
    ///// Alert
    fun insertToAlert(alertsStored : AlertsStored)
    fun deleteAlert(alertsStored : AlertsStored)
    val allStoredAlert: LiveData<List<AlertsStored>>



}