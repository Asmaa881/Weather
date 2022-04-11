package com.example.weather.home.homefragment.view

import android.content.Context
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.weather.R
import com.example.weather.home.model.Daily
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class SevenDaysRecyclerAdapter : RecyclerView.Adapter<SevenDaysRecyclerAdapter.ViewHolder>(){


    var weatherSevenDays: List<Daily> = ArrayList<Daily>()
    lateinit var context: Context
    lateinit var tempMeasure: String

    fun setData(context: Context, weatherSevenDaysItem: List<Daily>){
        this.context= context
        weatherSevenDays = weatherSevenDaysItem
        notifyDataSetChanged()
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var dayWeatherIcon: ImageView = itemView.findViewById(R.id.dayWeatherIcon)
        var txtDayName: TextView = itemView.findViewById(R.id.txtDayName)
        var txtDayTemp: TextView = itemView.findViewById(R.id.txtDayTemp)
        var txtDayWeatherDescription: TextView = itemView.findViewById(R.id.txtDayWeatherDescription)
        var cardViewDays: CardView = itemView.findViewById(R.id.cardViewDays)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val view = layoutInflater.inflate(R.layout.seven_days_recyclerview_row, parent, false)
        val viewHolder = ViewHolder(view)
        return viewHolder
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val cuurentDay = weatherSevenDays[position]
        var simpleDateFormat = SimpleDateFormat("EEEE")
        var format = Date(cuurentDay.dt!!*1000)
        //var dayName = simpleDateFormat.format(format)
        holder.txtDayName.text = simpleDateFormat.format(format)
       // "${(((currentHour.temp!! - 273.25) * (9 / 5)) + 32).toInt()} \u2109"
        var tempMax= 0
        var tempMin= 0

        if(HomeFragment.sharedPreferences.getString("tempMeasure","")== "C"){
            tempMax = (cuurentDay.temp!!.max!! - 273.15).toInt()
            tempMin = (cuurentDay.temp!!.min!! - 273.15).toInt()
            tempMeasure = "${tempMax} / ${tempMin} \u2103"
        }
        else if(HomeFragment.sharedPreferences.getString("tempMeasure","")== "F"){
            tempMax = (((cuurentDay.temp!!.max!! - 273.15) * (9 / 5)) + 32).toInt()
            tempMin = (((cuurentDay.temp!!.min!! - 273.15) * (9 / 5)) + 32).toInt()
            tempMeasure = "${tempMax} / ${tempMin} ℉"
        }
        else if(HomeFragment.sharedPreferences.getString("tempMeasure","")== "K"){
            tempMax = (cuurentDay.temp!!.max!!).toInt()
            tempMin = (cuurentDay.temp!!.min!!).toInt()
            tempMeasure = "${tempMax} / ${tempMin} K"
        }
        else{
            tempMax = (((cuurentDay.temp!!.max!! - 273.15) * (9 / 5)) + 32).toInt()
            tempMin = (((cuurentDay.temp!!.min!! - 273.15) * (9 / 5)) + 32).toInt()
            tempMeasure = "${tempMax} / ${tempMin} ℉"
        }
        holder.txtDayTemp.text = tempMeasure
        holder.txtDayWeatherDescription.text = cuurentDay.weather[0].description
        var iconLink = "https://openweathermap.org/img/w/${cuurentDay.weather[0].icon}.png"
         Glide.with(context).load(iconLink).error(R.drawable.icon)
             .into(holder.dayWeatherIcon)
        }

    override fun getItemCount(): Int {
        return weatherSevenDays.size
    }

}