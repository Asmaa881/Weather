package com.example.weather.home.db

import android.content.Context
import androidx.lifecycle.LiveData
import com.example.weather.home.model.FavoriteStored

class ConcreteLocalSource (context: Context) : LocalSourceInterface {

    private val dao: WeatherDAOInterface?

    override val allStoredCity: LiveData<List<FavoriteStored>>
    init {
        val db: AppDataBase = AppDataBase.getInstance(context)
        dao = db.weatherDAO()
        allStoredCity = dao.getFavoriteCities
    }

    override fun insertToFav(favoriteStored: FavoriteStored) {
        dao?.insertToFavorite(favoriteStored)
    }

    override fun deleteMovie(favoriteStored: FavoriteStored) {
        dao?.deleteFromFavorite(favoriteStored)
    }
}