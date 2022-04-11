package com.example.weather.home.favouritefragment.view

import com.example.weather.home.model.ResponseModel

interface CommunicatorInterface {

    fun passData(lat: Double, lon: Double)
    fun goToAlertDialog()
    fun backToAlertFragment()
}