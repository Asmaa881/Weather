package com.example.weather.home.homefragment.view

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.weather.R
import com.example.weather.home.model.Hourly
import com.example.weather.home.model.ResponseModel
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class HourlyRecyclerAdapter : RecyclerView.Adapter<HourlyRecyclerAdapter.ViewHolder>(){

    var weatherHourly: List<Hourly> = ArrayList<Hourly>()
    lateinit var context: Context

    fun setData(context: Context,weatherHourlyItem: List<Hourly>){
        this.context= context
        weatherHourly = weatherHourlyItem
        notifyDataSetChanged()
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val view = layoutInflater.inflate(R.layout.twenty_four_hours_recyclerview_raw, parent, false)
        val viewHolder = ViewHolder(view)
        return viewHolder
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentHour = weatherHourly[position]
        var simpleDateFormat = SimpleDateFormat("hh a")
        var format = Date(currentHour.dt!!*1000)
       // var hour = simpleDateFormat.format(format)
        holder.txtHour.text = simpleDateFormat.format(format)
        holder.txtHourTemp.text= currentHour.temp.toString()
        var iconLink = "https://openweathermap.org/img/w/${currentHour.weather[0].icon}.png"
        Glide.with(context).load(iconLink).error(R.drawable.icon)
            .into(holder.hourIcon)
    }
    override fun getItemCount(): Int {
        return weatherHourly.size
    }


    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var hourIcon: ImageView = itemView.findViewById(R.id.hourIcon)
        var txtHour: TextView = itemView.findViewById(R.id.txtHour)
        var txtHourTemp: TextView = itemView.findViewById(R.id.txtHourTemp)
        var cardViewHourly: CardView = itemView.findViewById(R.id.cardViewHourly)
    }


}