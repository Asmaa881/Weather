package com.example.weather.home.alertsfragment.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.weather.home.model.RepositoryInterface
import java.lang.IllegalArgumentException

class AlertViewModelFactory (private val repo: RepositoryInterface) : ViewModelProvider.Factory{
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return if (modelClass.isAssignableFrom(AlertViewModel::class.java)) {
            AlertViewModel(repo) as T
        } else {
            throw IllegalArgumentException("ViewModel Class not found")
        }
    }
}