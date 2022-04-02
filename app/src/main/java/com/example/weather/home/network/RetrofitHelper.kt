package com.example.weather.home.network

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RetrofitHelper {


    // https://api.openweathermap.org/data/2.5/onecall?lat=31.205753&lon=29.924526&appid=4fb359fbc087d60f9071faa71d7f6fbf

    companion object {
        var base_url = "https://api.openweathermap.org/data/2.5/"
        private var retrofit: Retrofit? = null
        fun getClient(): Retrofit? {
            if (retrofit == null) {
                retrofit = Retrofit.Builder().baseUrl(base_url)
                    .addConverterFactory(GsonConverterFactory.create()).build()
            }
            return retrofit
        }
    }



}