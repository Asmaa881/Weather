package com.example.weather.home.model

import androidx.lifecycle.LiveData

interface RepositoryInterface {

    //from network
    suspend fun getNetworkWeather(lat :Double, lon:Double, apiKey:String,language:String): ResponseModel
     ////Favorite/////////
    val storedFavCities: LiveData<List<FavoriteStored>>
    fun insertToFav(favoriteStored: FavoriteStored)
    fun deleteToFav(favoriteStored: FavoriteStored)
    /////////// Alerts ////////////
    val storedAlert: LiveData<List<AlertsStored>>
    fun insertAlert(alertsStored : AlertsStored)
    fun deleteAlert(alertsStored : AlertsStored)

      /////Saved Weather //////////
    val storedWeather: LiveData<ResponseModel>
    fun insertWeather(responseModel: ResponseModel)






}