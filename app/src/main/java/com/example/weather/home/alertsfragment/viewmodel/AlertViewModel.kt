package com.example.weather.home.alertsfragment.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weather.home.model.AlertsStored
import com.example.weather.home.model.RepositoryInterface
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class AlertViewModel (iRepo: RepositoryInterface) : ViewModel(){

    private val _iRepo : RepositoryInterface = iRepo
    private val _alertList = MutableLiveData<List<AlertsStored>>()

    //add to Alert
    fun insertAlert(alertsStored: AlertsStored) {
        viewModelScope.launch(Dispatchers.IO){
            _iRepo.insertAlert(alertsStored)
        }
    }
    // Remove From Alert
    fun removeAlert(alertsStored: AlertsStored) {
        viewModelScope.launch(Dispatchers.IO){
            _iRepo.deleteAlert(alertsStored)
        }
    }
    fun getAllAlerts(): LiveData<List<AlertsStored>> {
        return _iRepo.storedAlert
    }
    override fun onCleared() {
        super.onCleared()
    }



}