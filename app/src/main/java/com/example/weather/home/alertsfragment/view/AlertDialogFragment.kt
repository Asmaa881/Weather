package com.example.weather.home.alertsfragment.view

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Intent
import android.location.Geocoder
import android.os.Build
import android.os.Bundle
import android.text.format.DateFormat
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.work.Data
import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequest
import androidx.work.WorkManager
import com.example.weather.R
import com.example.weather.home.notification.AlertsWorker
import com.example.weather.home.alertsfragment.viewmodel.AlertViewModel
import com.example.weather.home.alertsfragment.viewmodel.AlertViewModelFactory
import com.example.weather.home.db.ConcreteLocalSource
import com.example.weather.home.favouritefragment.view.CommunicatorInterface
import com.example.weather.home.model.AlertsStored
import com.example.weather.home.model.WeatherRepository
import com.example.weather.home.network.WeatherClient
import com.example.weather.home.view.HomeActivity
import com.example.weather.home.view.MapsActivity
import com.google.android.material.button.MaterialButton
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit

class AlertDialogFragment : Fragment() {

    lateinit var alertViewModel: AlertViewModel
    lateinit var alertViewModelFactory: AlertViewModelFactory
    lateinit var alertsStored: AlertsStored
    lateinit var txtSelectStartDate: TextView
    lateinit var txtSelectEndDate: TextView
    lateinit var txtSelectTime: TextView
    lateinit var txtSelectLocation: TextView
    lateinit var alertDialogOkBtn: MaterialButton
    lateinit var date1: String
    lateinit var date2: String
    var startYear =0
    var startMonth =0
    var startDay =0
    var endYear =0
    var endMonth =0
    var endDay =0
    var minute = 0
    var hour = 0
    var cityName: String? = null
    private lateinit var communicatorInterface: CommunicatorInterface


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        communicatorInterface = activity as CommunicatorInterface
        arguments?.let {
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        var view = inflater.inflate(R.layout.fragment_alert_dialog, container, false)

        txtSelectStartDate = view.findViewById(R.id.txtSelectStartDate)
        txtSelectEndDate = view.findViewById(R.id.txtSelectEndDate)
        txtSelectTime = view.findViewById(R.id.txtSelectTime)
        txtSelectLocation = view.findViewById(R.id.txtSelectLocation)
        alertDialogOkBtn = view.findViewById(R.id.btnOkAlertDialog)

        txtSelectStartDate.setOnClickListener {
            createDatePickDialogForStartDate()
        }
        txtSelectEndDate.setOnClickListener {
            createDatePickDialogForEndDate()
        }
        txtSelectTime.setOnClickListener {
            createTimePicker()
             txtSelectLocation.text = getAddress(MapsActivity.lat,MapsActivity.lon)
        }
        txtSelectLocation.setOnClickListener {
            HomeActivity.opendMapFrom =4
            var intent = Intent(requireContext(), MapsActivity::class.java)
            startActivity(intent)
        }
        alertDialogOkBtn.setOnClickListener {
            ///////// Worker ////////////////
            val inputData = Data.Builder()
                .putDouble("lat",MapsActivity.lat)
                .putDouble("lon",MapsActivity.lon)
                .build()
            val simpleDateFormat = SimpleDateFormat("dd/MM/yyyy")
            val today = Calendar.getInstance()
            var startDate = Calendar.getInstance()
            var endDate = Calendar.getInstance()
            startDate.set(startYear, startMonth, startDay, hour, minute)
            endDate.set(endYear, endMonth, endDay, hour, minute)
            var diffInMinutes = (startDate.timeInMillis - today.timeInMillis) / 60000
            var d1: Date? = null
            var d2: Date? = null
            try {
                d1 = simpleDateFormat.parse(date1)
                d2 = simpleDateFormat.parse(date2)
            } catch (e: ParseException) {
                e.printStackTrace()
            }
            val differenceInTime = d2!!.time - d1!!.time
            val diffInDays = differenceInTime / (1000 * 60 * 60 * 24) % 365
            Log.i("Tagggg","Days: "+diffInDays)
            Log.i("Tagggg", "diff In Minutes: $diffInMinutes")
            Log.i("Tagggg", diffInMinutes.toString())
            var workRequest = OneTimeWorkRequest.Builder(AlertsWorker::class.java)
                .setInitialDelay(diffInMinutes, TimeUnit.MINUTES)
              .setInputData(inputData).build()
            if (diffInMinutes > 0) {
                HomeActivity.requests.add(workRequest)
                Log.i("Tagggg", "done")
            }
            for (i in 1..diffInDays) {
                val duration = Math.abs(diffInMinutes + 1440 * i)
                workRequest = OneTimeWorkRequest.Builder(AlertsWorker::class.java)
                    .setInitialDelay(duration, TimeUnit.MINUTES)
                    .setInputData(inputData).build()
                HomeActivity.requests.add(workRequest)
            }
            val name = "${MapsActivity.lat} - ${MapsActivity.lon}"
            WorkManager.getInstance().enqueueUniqueWork(name, ExistingWorkPolicy.REPLACE, HomeActivity.requests)
            /////////
            alertViewModelFactory = AlertViewModelFactory(WeatherRepository.getInstance(
                WeatherClient.getInstance(), ConcreteLocalSource(requireContext()),requireContext()))
            alertViewModel = ViewModelProvider(this, alertViewModelFactory).get(AlertViewModel::class.java)
            cityName = getAddress(MapsActivity.lat, MapsActivity.lon)
            if(MapsActivity.lat==0.0 || MapsActivity.lon==0.0){
                Toast.makeText(requireContext(),"Please choose location!!",Toast.LENGTH_SHORT).show()
            }
            if(date1.isNullOrEmpty() || date2.isNullOrEmpty()){
                Toast.makeText(requireContext(),"Please choose Dates!!",Toast.LENGTH_SHORT).show()
            }
            var time = "$hour"
            if(time.isNullOrEmpty()){
                Toast.makeText(requireContext(),"Please choose Times!!",Toast.LENGTH_SHORT).show()
            }
            alertsStored= AlertsStored(MapsActivity.lat, MapsActivity.lon,date1, date2,txtSelectTime.text.toString(),
                cityName!!
            )
            alertViewModel.insertAlert(alertsStored)
            Toast.makeText(requireContext(), "Alert added!", Toast.LENGTH_SHORT).show()
            communicatorInterface.backToAlertFragment()
        }
        return view
    }
    @RequiresApi(api = Build.VERSION_CODES.O)
    private fun createDatePickDialogForStartDate() {
        val calendar = Calendar.getInstance()
         startYear = calendar[Calendar.YEAR]
         startMonth = calendar[Calendar.MONTH]
         startDay = calendar[Calendar.DAY_OF_MONTH]
        val datePickerDialog = DatePickerDialog(
            context!!, { datePicker, year, month, day ->
                var month = month
                month = month + 1
               date1 = "$day/$month/$year"
               txtSelectStartDate.setText(date1)
            }, startYear, startMonth, startDay
        )
        datePickerDialog.datePicker.minDate = calendar.timeInMillis - 1000
        datePickerDialog.show()
    }
    @RequiresApi(api = Build.VERSION_CODES.O)
    private fun createDatePickDialogForEndDate() {
        val calendar = Calendar.getInstance()
        endYear = calendar[Calendar.YEAR]
        endMonth = calendar[Calendar.MONTH]
        endDay = calendar[Calendar.DAY_OF_MONTH]
        val datePickerDialog = DatePickerDialog(
            context!!, { datePicker, year, month, day ->
                var month = month
                month = month + 1
                 date2 = "$day/$month/$year"
                txtSelectEndDate.setText(date2)
            }, endYear, endMonth, endDay
        )
        datePickerDialog.datePicker.minDate = calendar.timeInMillis - 1000
        datePickerDialog.show()
    }

    private fun createTimePicker(){
        val timePickerDialog = TimePickerDialog(context,
            { timePicker, hourOfDay, minutes ->
                 hour = hourOfDay
                 minute = minutes
                val calendar = Calendar.getInstance()
                calendar[0, 0, 0, hour] = minute
                txtSelectTime.setText(DateFormat.format("hh:mm aa", calendar))
            }, 12, 0, false
        )
        timePickerDialog.updateTime(hour, minute)
        timePickerDialog.show()
    }
    private fun getAddress(lat:Double, lon: Double): String?{
        var geoCoder = Geocoder(requireContext(), Locale.getDefault())
        var adress = geoCoder.getFromLocation(lat, lon, 3)
        return adress.get(0).adminArea
    }

}