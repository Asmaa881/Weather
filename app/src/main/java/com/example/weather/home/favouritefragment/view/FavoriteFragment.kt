package com.example.weather.home.favouritefragment.view

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.weather.R
import com.example.weather.home.db.ConcreteLocalSource
import com.example.weather.home.favouritefragment.viewmodel.FavoriteViewModel
import com.example.weather.home.favouritefragment.viewmodel.FavoriteViewModelFactory
import com.example.weather.home.model.FavoriteStored
import com.example.weather.home.model.WeatherRepository
import com.example.weather.home.network.WeatherClient
import com.example.weather.home.view.HomeActivity
import com.example.weather.home.view.MapsActivity
import com.google.android.material.floatingactionbutton.FloatingActionButton

class FavoriteFragment : Fragment(), OnFavoriteClickListener {

    lateinit var favViewModel: FavoriteViewModel
    lateinit var favFactory: FavoriteViewModelFactory
    lateinit var favouriteRecyclerViewAdapter: FavouriteRecyclerViewAdapter
    lateinit var linearLayoutManager: LinearLayoutManager
    lateinit var favoriteRecyclerView: RecyclerView
    private lateinit var communicatorInterface: CommunicatorInterface

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        var view =inflater.inflate(R.layout.fragment_favorite, container, false)
        var btnOpenMapToChooseFavLocation:FloatingActionButton = view.findViewById(R.id.btnOpenMapToChooseFavLocation)
        favoriteRecyclerView = view.findViewById(R.id.favoriteRecyclerView)

        communicatorInterface = activity as CommunicatorInterface

        linearLayoutManager=LinearLayoutManager(requireContext())
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL)
        favoriteRecyclerView.setLayoutManager(linearLayoutManager)
        favouriteRecyclerViewAdapter= FavouriteRecyclerViewAdapter(this,communicatorInterface)
        favouriteRecyclerViewAdapter.notifyDataSetChanged()
        favoriteRecyclerView.setAdapter(favouriteRecyclerViewAdapter)

        favFactory = FavoriteViewModelFactory(WeatherRepository.getInstance(WeatherClient.getInstance(),
            ConcreteLocalSource(requireContext()), requireContext()), 0.0, 0.0,"4fb359fbc087d60f9071faa71d7f6fbf")
        favViewModel = ViewModelProvider(this, favFactory).get(FavoriteViewModel::class.java)

        favViewModel.getAllFav().observe(requireActivity()) { fav ->
            Log.i("TAG", "Observation: ${fav}")
            if (fav != null)
                favouriteRecyclerViewAdapter.setData(requireContext(),fav)
            favouriteRecyclerViewAdapter.notifyDataSetChanged()
        }
        btnOpenMapToChooseFavLocation.setOnClickListener {
            HomeActivity.opendMapFrom =2
            var intent = Intent(requireContext(),MapsActivity::class.java)
            startActivity(intent)
        }
        return view
    }

    override fun onClick(favoriteStored: FavoriteStored) {
        favViewModel.removeFromFav(favoriteStored)
        Toast.makeText(requireContext(), "Deleted Successfully!!!", Toast.LENGTH_SHORT).show()
    }

}