package com.example.weather.home.db

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.weather.home.model.AlertsStored
import com.example.weather.home.model.FavoriteStored
import com.example.weather.home.model.ResponseModel

@Dao
interface WeatherDAOInterface {


    ///// Favorite ///////
    @get:Query("SELECT * From FavoriteWeather")
    val getFavoriteCities: LiveData<List<FavoriteStored>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertToFavorite(favWeather: FavoriteStored)
    @Delete
    fun deleteFromFavorite(favWeather: FavoriteStored)

    ///// Alert ///////
    @get:Query("SELECT * From AlertsWeather")
    val getAlerts: LiveData<List<AlertsStored>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertAlert(alertsStored : AlertsStored)
    @Delete
    fun deleteAlert(alertsStored : AlertsStored)

    ////////// Weather
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertWeather(responseModel: ResponseModel)
    @get:Query("SELECT * From ResponseWeather ORDER BY id DESC LIMIT 1")
    val getWeather: LiveData<ResponseModel>



}