package com.example.weather.home.model

import androidx.annotation.NonNull
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters


@Entity(tableName = "FavoriteWeather")
data class FavoriteStored(
    var lat:Double?= null,
    var lon:Double?= null,
    @PrimaryKey
    @NonNull
    var city_name:String
)

@Entity(tableName = "AlertsWeather")
data class AlertsStored(
    var lat:Double?= null,
    var lon:Double?= null,
    var sDate: String? = null,
    var eDate: String? = null,
    var alertTime : String? = null,
    @PrimaryKey
    @NonNull
    var city_name:String
)

@Entity(tableName = "ResponseWeather")
@TypeConverters
data class ResponseModel(
    @PrimaryKey(autoGenerate = true)
    @NonNull
    var id: Long,
    var lat: Double? = null,
    var lon: Double? = null,
    var timezone: String? = null,
    var timezone_offset: Int? = null,
    @Embedded
    var current: Current? = Current(),
    var hourly: ArrayList<Hourly> = arrayListOf(),
    var daily: ArrayList<Daily> = arrayListOf(),
    var alerts: ArrayList<Alerts> = arrayListOf()
)


data class Current(
    var dt: Int? = null,
    var sunrise: Int? = null,
    var sunset: Int? = null,
    var temp: Double? = null,
    var feels_like: Double? = null,
    var pressure: Int? = null,
    var humidity: Int? = null,
    var dew_point: Double? = null,
    var uvi: Double? = null,
    var clouds: Int? = null,
    var visibility: Int? = null,
    var wind_speed: Double? = null,
    var wind_deg: Int? = null,
    var weather: ArrayList<Weather> = arrayListOf()
)

data class Weather(
    var id: Int? = null,
    var main: String? = null,
    var description: String? = null,
    var icon: String? = null
)

data class Temp(
    var day: Double? = null,
    var min: Double? = null,
    var max: Double? = null,
    var night: Double? = null,
    var eve: Double? = null,
    var morn: Double? = null
)

data class Minutely( var dt: Int? = null, var precipitation: Int? = null )

data class Hourly(
    var dt: Long? = null,
    var temp: Double? = null,
    var feels_like: Double? = null,
    var pressure: Int? = null,
    var humidity: Int? = null,
    var dew_point: Double? = null,
    var uvi: Double? = null,
    var clouds: Int? = null,
    var visibility: Int? = null,
    var wind_speed: Double? = null,
    var wind_deg: Int? = null,
    var wind_gust: Double? = null,
    var weather: ArrayList<Weather> = arrayListOf(),
    var pop: Double? = null
)

data class FeelsLike( var day: Double? = null, var night: Double? = null, var eve: Double? = null, var morn: Double? = null )

data class Daily(
    var dt: Long? = null,
    var sunrise: Int? = null,
    var sunset: Int? = null,
    var moonrise: Int? = null,
    var moonset: Int? = null,
    var moon_phase: Double? = null,
    var temp: Temp? = Temp(),
    var feels_like: FeelsLike? = FeelsLike(),
    var pressure: Int? = null,
    var humidity: Int? = null,
    var dew_point: Double? = null,
    var wind_speed: Double? = null,
    var wind_deg: Int? = null,
    var wind_gust: Double? = null,
    var weather: ArrayList<Weather> = arrayListOf(),
    var clouds: Int? = null,
    var pop: Double? = null,
    var uvi: Double? = null
)
data class Alerts(
    var sender_name: String? = null,
    var event: String? = null,
    var start: Int? = null,
    var end: Int? = null,
    var description: String? = null,
    var tags: ArrayList<String> = arrayListOf()
)

