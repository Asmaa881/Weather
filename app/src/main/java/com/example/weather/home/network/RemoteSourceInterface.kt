package com.example.weather.home.network

import com.example.weather.home.model.ResponseModel

interface RemoteSourceInterface {

    suspend fun getWeatherOverNetwork(lat :Double, lon:Double, apiKey:String, language: String): ResponseModel

}