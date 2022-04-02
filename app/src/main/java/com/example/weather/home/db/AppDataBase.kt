package com.example.weather.home.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.weather.home.model.FavoriteStored

@Database(entities = [FavoriteStored::class], version = 1)
abstract class AppDataBase : RoomDatabase() {

    abstract fun weatherDAO(): WeatherDAOInterface
    companion object{
        private var instance: AppDataBase? = null
        @Synchronized
        fun getInstance(context: Context): AppDataBase{
            return instance?: Room.databaseBuilder(context.applicationContext,
                AppDataBase::class.java,"FavoriteWeather")
                .fallbackToDestructiveMigration().build()
        }
    }

}