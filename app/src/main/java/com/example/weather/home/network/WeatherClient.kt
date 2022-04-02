package com.example.weather.home.network

import com.example.weather.home.model.ResponseModel

class WeatherClient private constructor() : RemoteSourceInterface {

    companion object{
        private var instance:WeatherClient? = null
        fun getInstance():WeatherClient{
            return instance?: WeatherClient()
        }
    }
    override suspend fun getWeatherOverNetwork(lat: Double, lon: Double, apiKey: String): ResponseModel {
        val retrofitService = RetrofitHelper.getClient()?.create(RetrofitServiceInterface::class.java)
        var response = retrofitService?.getWeather(lat,lon,apiKey)
        return response!!
    }


}