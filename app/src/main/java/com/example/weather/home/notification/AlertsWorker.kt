package com.example.weather.home.notification

import android.content.Context
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.weather.home.homefragment.view.HomeFragment
import com.example.weather.home.model.ResponseModel
import com.example.weather.home.network.InternetConnectionChecker
import com.example.weather.home.network.WeatherClient
import kotlinx.coroutines.runBlocking

class AlertsWorker(appContext: Context, params: WorkerParameters) : CoroutineWorker(appContext, params) {

    lateinit var internetConnectionChecker: InternetConnectionChecker
    lateinit var responseModel: ResponseModel
    lateinit var alertNotification: AlertNotification

    override suspend fun doWork(): Result {
        Log.i("doWork", "I am Inside do work")
        val data = inputData
        val latitude = data.getDouble("lat",0.0)
        val longitude = data.getDouble("lon",0.0)
        val client: WeatherClient = WeatherClient.getInstance()
        runBlocking {
            responseModel = client.getWeatherOverNetwork(latitude,longitude,"4fb359fbc087d60f9071faa71d7f6fbf", HomeFragment.sharedPreferences.getString("langState","en")!!)
        }
        val content = responseModel.current!!.temp.toString()
        val alert = responseModel.alerts

        if(alert== null){
            alertNotification = AlertNotification(applicationContext)
            val builder: NotificationCompat.Builder = alertNotification.getChannelNotification(
                "Weather Alert", "No Alerts For Today.. " )!!
            alertNotification.getManager().notify(1, builder.build())
       }
       else{
            alertNotification = AlertNotification(applicationContext)
            Log.i("dowork", "${alert[0].event} \n ${alert[0].tags.get(0)}")
            val nb: NotificationCompat.Builder = alertNotification.getChannelNotification(
                "Weather Alert", "${alert[0].description}" )!!
            alertNotification.getManager().notify(1, nb.build())
       }
        return Result.success()
    }

}