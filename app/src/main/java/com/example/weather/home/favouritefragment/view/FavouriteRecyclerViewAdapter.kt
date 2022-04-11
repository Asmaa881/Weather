package com.example.weather.home.favouritefragment.view

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.weather.R
import com.example.weather.home.model.FavoriteStored
import kotlin.collections.ArrayList

class FavouriteRecyclerViewAdapter (private val listener: OnFavoriteClickListener, private var communicatorInterface: CommunicatorInterface) : RecyclerView.Adapter<FavouriteRecyclerViewAdapter.ViewHolder>(){

    var favoriteStored: List<FavoriteStored> = ArrayList<FavoriteStored>()
    lateinit var context: Context

    fun setData(context: Context, favoriteStored: List<FavoriteStored>){
        this.context= context
        this.favoriteStored = favoriteStored
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder( parent: ViewGroup, viewType: Int): FavouriteRecyclerViewAdapter.ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val view = layoutInflater.inflate(R.layout.favorite_row, parent, false)
        val viewHolder = ViewHolder(view)
        return viewHolder
    }

    override fun onBindViewHolder(holder: FavouriteRecyclerViewAdapter.ViewHolder, position: Int) {
        holder.txtFavCityName.text = favoriteStored[position].city_name
        holder.cardViewFavorite.setOnClickListener {
           communicatorInterface.passData(favoriteStored[position].lat!!,favoriteStored[position].lon!!)
        }
        holder.FavDeleteIcon.setOnClickListener {
            listener.onClick(favoriteStored[position])
        }
    }
    override fun getItemCount(): Int {
        return favoriteStored.size
    }
    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var FavDeleteIcon: ImageView = itemView.findViewById(R.id.FavDeleteIcon)
        var txtFavCityName: TextView = itemView.findViewById(R.id.txtFavCityName)
        var cardViewFavorite: CardView = itemView.findViewById(R.id.cardViewFavorite)
    }



}