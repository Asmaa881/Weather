package com.example.weather.home.alertsfragment.view

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.weather.R
import com.example.weather.home.model.AlertsStored


class AlertRecyclerViewAdapter (private val listener: OnItemClickListener) : RecyclerView.Adapter<AlertRecyclerViewAdapter.ViewHolder>(){

    var alertsStored: List<AlertsStored> = ArrayList<AlertsStored>()
    lateinit var context: Context

    fun setData(context: Context, alertsStored: List<AlertsStored>){
        this.context= context
        this.alertsStored = alertsStored
        notifyDataSetChanged()
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val view = layoutInflater.inflate(R.layout.alerts_row, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.txtAlertCityName.text = alertsStored[position].city_name
        holder.txtAlertStartDate.text = alertsStored[position].sDate
        holder.txtAlertEndDate.text = alertsStored[position].eDate
        holder.txtAlertTime.text = alertsStored[position].alertTime
        holder.AlertDeleteIcon.setOnClickListener {
            listener.onClick(alertsStored[position])
        }
    }

    override fun getItemCount(): Int {
        return alertsStored.size
    }
    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var txtAlertCityName: TextView = itemView.findViewById(R.id.txtAlertCityName)
        var txtAlertTime: TextView = itemView.findViewById(R.id.txtAlertTime)
        var txtAlertStartDate: TextView = itemView.findViewById(R.id.txtAlertStartDate)
        var txtAlertEndDate: TextView = itemView.findViewById(R.id.txtAlertEndDate)
        var AlertDeleteIcon: ImageView = itemView.findViewById(R.id.AlertDeleteIcon)
    }



}