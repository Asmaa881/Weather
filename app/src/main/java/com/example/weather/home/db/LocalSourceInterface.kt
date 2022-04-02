package com.example.weather.home.db

import androidx.lifecycle.LiveData
import com.example.weather.home.model.FavoriteStored

interface LocalSourceInterface {

    fun insertToFav(favoriteStored: FavoriteStored)
    fun deleteMovie(favoriteStored: FavoriteStored)
    val allStoredCity: LiveData<List<FavoriteStored>>
}