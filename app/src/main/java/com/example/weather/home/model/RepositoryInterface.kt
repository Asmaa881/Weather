package com.example.weather.home.model

import androidx.lifecycle.LiveData

interface RepositoryInterface {

    //from network
    suspend fun getNetworkWeather(lat :Double, lon:Double, apiKey:String): ResponseModel


    val storedFavCities: LiveData<List<FavoriteStored>>
    fun insertToFav(favoriteStored: FavoriteStored)
    fun deleteToFav(favoriteStored: FavoriteStored)


  /*
    //Favorite
    val storedWeather: LiveData<List<ResponseModel>>
    fun insertWeather(responseModel: ResponseModel)
    fun deleteWeather(responseModel: ResponseModel)
*/





}