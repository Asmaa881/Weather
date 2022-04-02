package com.example.weather.home.db

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.weather.home.model.FavoriteStored

@Dao
interface WeatherDAOInterface {

    @get:Query("SELECT * From FavoriteWeather")
    val getFavoriteCities: LiveData<List<FavoriteStored>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertToFavorite(favWeather: FavoriteStored)

    @Delete
    fun deleteFromFavorite(favWeather: FavoriteStored)

}