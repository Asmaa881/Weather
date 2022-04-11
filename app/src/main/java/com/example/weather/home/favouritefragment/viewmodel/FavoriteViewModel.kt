package com.example.weather.home.favouritefragment.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weather.home.model.FavoriteStored
import com.example.weather.home.model.RepositoryInterface
import com.example.weather.home.model.ResponseModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class FavoriteViewModel (iRepo: RepositoryInterface, var lat :Double, var lon:Double, var apiKey:String) : ViewModel(){

    private val _iRepo : RepositoryInterface = iRepo
   // private val _movieList = MutableLiveData<List<ResponseModel>>()

    //add to fav
    fun insertToFav(favoriteStored: FavoriteStored) {
        viewModelScope.launch(Dispatchers.IO){
            _iRepo.insertToFav(favoriteStored)
        }
    }
    // Remove From Fav
    fun removeFromFav(favoriteStored: FavoriteStored) {
        viewModelScope.launch(Dispatchers.IO){
            _iRepo.deleteToFav(favoriteStored)
        }
    }

    fun getAllFav(): LiveData<List<FavoriteStored>> {
       return _iRepo.storedFavCities

    }

    override fun onCleared() {
        super.onCleared()
    }

}