package com.example.weather.home.network


import com.example.weather.home.model.ResponseModel
import retrofit2.http.GET
import retrofit2.http.Query

interface RetrofitServiceInterface {

    @GET("onecall")
    suspend fun getWeather(@Query("lat") lat :Double, @Query("lon") lon:Double,
                           @Query("appid")apiKey:String,@Query("lang")language:String): ResponseModel

}