package com.example.weather.home.db

import android.content.Context
import androidx.room.*
import androidx.room.TypeConverter
import com.example.weather.home.model.AlertsStored
import com.example.weather.home.model.FavoriteStored
import com.example.weather.home.model.ResponseModel

@Database(entities = [FavoriteStored::class, AlertsStored:: class, ResponseModel::class], version = 10)
@TypeConverters(TypeConvertersRoom::class)
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