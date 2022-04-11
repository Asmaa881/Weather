package com.example.weather.home.db

import android.content.Context
import androidx.lifecycle.LiveData
import com.example.weather.home.model.AlertsStored
import com.example.weather.home.model.FavoriteStored
import com.example.weather.home.model.ResponseModel

class ConcreteLocalSource (context: Context) : LocalSourceInterface {

    private val dao: WeatherDAOInterface?

    override val allStoredCity: LiveData<List<FavoriteStored>>
    override val allStoredAlert: LiveData<List<AlertsStored>>
    override val weatherStored: LiveData<ResponseModel>

    init {
        val db: AppDataBase = AppDataBase.getInstance(context)
        dao = db.weatherDAO()
        allStoredCity = dao.getFavoriteCities
        allStoredAlert = dao.getAlerts
        weatherStored = dao.getWeather
    }

    ////// Weather //////////
    override fun saveWeather(responseModel: ResponseModel) {
        dao?.insertWeather(responseModel)
    }
    ///// Alert ////////
    override fun insertToAlert(alertsStored: AlertsStored) {
        dao?.insertAlert(alertsStored)
    }

    override fun deleteAlert(alertsStored: AlertsStored) {
        dao?.deleteAlert(alertsStored)
    }

    // Favorite//////
    override fun insertToFav(favoriteStored: FavoriteStored) {
        dao?.insertToFavorite(favoriteStored)
    }

    override fun deleteMovie(favoriteStored: FavoriteStored) {
        dao?.deleteFromFavorite(favoriteStored)
    }
}