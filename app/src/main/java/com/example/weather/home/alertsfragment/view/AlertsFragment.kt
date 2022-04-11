package com.example.weather.home.alertsfragment.view

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.weather.R
import com.example.weather.home.alertsfragment.viewmodel.AlertViewModel
import com.example.weather.home.alertsfragment.viewmodel.AlertViewModelFactory
import com.example.weather.home.db.ConcreteLocalSource
import com.example.weather.home.favouritefragment.view.CommunicatorInterface
import com.example.weather.home.model.AlertsStored
import com.example.weather.home.model.WeatherRepository
import com.example.weather.home.network.WeatherClient
import com.google.android.material.floatingactionbutton.FloatingActionButton


class AlertsFragment : Fragment(), OnItemClickListener {

    private lateinit var communicatorInterface: CommunicatorInterface
   lateinit var alertsRecyclerView: RecyclerView
   lateinit var btnOpenAlertDialog: FloatingActionButton

    lateinit var alertViewModel: AlertViewModel
    lateinit var alertViewModelFactory: AlertViewModelFactory
    lateinit var alertRecyclerViewAdapter: AlertRecyclerViewAdapter
    lateinit var linearLayoutManager: LinearLayoutManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        communicatorInterface = activity as CommunicatorInterface

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        var view = inflater.inflate(R.layout.fragment_alerts, container, false)
        alertsRecyclerView = view.findViewById(R.id.alertsRecyclerView)
        btnOpenAlertDialog = view.findViewById(R.id.btnOpenAlertDialog)

        linearLayoutManager=LinearLayoutManager(requireContext())
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL)
        alertsRecyclerView.setLayoutManager(linearLayoutManager)
        alertRecyclerViewAdapter= AlertRecyclerViewAdapter(this)
        alertRecyclerViewAdapter.notifyDataSetChanged()
        alertsRecyclerView.setAdapter(alertRecyclerViewAdapter)

        alertViewModelFactory = AlertViewModelFactory(WeatherRepository.getInstance(
            WeatherClient.getInstance(), ConcreteLocalSource(requireContext()),requireContext()))
        alertViewModel = ViewModelProvider(this, alertViewModelFactory).get(AlertViewModel::class.java)

        alertViewModel.getAllAlerts().observe(requireActivity()) { alerts ->
            Log.i("TAG", "Observation: ${alerts}")
            if (alerts != null)
                alertRecyclerViewAdapter.setData(requireContext(),alerts)
            alertRecyclerViewAdapter.notifyDataSetChanged()
        }

        btnOpenAlertDialog.setOnClickListener(){
            communicatorInterface.goToAlertDialog()
        }
        return view
    }

    override fun onClick(alertsStored: AlertsStored) {
        alertViewModel.removeAlert(alertsStored)
        alertRecyclerViewAdapter.notifyDataSetChanged()
        Toast.makeText(requireContext(), "Deleted Successfully!!!", Toast.LENGTH_SHORT).show()
    }

}