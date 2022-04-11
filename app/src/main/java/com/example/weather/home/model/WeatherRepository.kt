package com.example.weather.home.model

import android.content.Context
import androidx.lifecycle.LiveData
import com.example.weather.home.db.LocalSourceInterface
import com.example.weather.home.network.RemoteSourceInterface

class WeatherRepository  private constructor( var remoteSourceInterface: RemoteSourceInterface,
                                              var localSourceInterface: LocalSourceInterface, var context: Context) : RepositoryInterface {

    companion object{
        private var instance: WeatherRepository? = null
        fun getInstance(remoteSourceInterface: RemoteSourceInterface,
                         localSourceInterface: LocalSourceInterface, context: Context): WeatherRepository{
            return instance?: WeatherRepository(remoteSourceInterface,
                localSourceInterface, context)
        }
    }
    //// Network
    override suspend fun getNetworkWeather(lat :Double, lon:Double, apiKey:String,language:String): ResponseModel {
        return remoteSourceInterface.getWeatherOverNetwork(lat,lon,apiKey,language)
    }
    ///// Favorite
    override val storedFavCities: LiveData<List<FavoriteStored>>
        get() = localSourceInterface.allStoredCity
    override fun insertToFav(favoriteStored: FavoriteStored) {
        localSourceInterface.insertToFav(favoriteStored)
    }
    override fun deleteToFav(favoriteStored: FavoriteStored) {
        localSourceInterface.deleteMovie(favoriteStored)    }

    override val storedAlert: LiveData<List<AlertsStored>>
        get() = localSourceInterface.allStoredAlert

    override fun insertAlert(alertsStored: AlertsStored) {
        localSourceInterface.insertToAlert(alertsStored)
    }

    override fun deleteAlert(alertsStored: AlertsStored) {
        localSourceInterface.deleteAlert(alertsStored)
    }

    /// Weather Data
    override val storedWeather: LiveData<ResponseModel>
        get() = localSourceInterface.weatherStored

    override fun insertWeather(responseModel: ResponseModel) {
        localSourceInterface.saveWeather(responseModel)
    }


}