package com.example.weather

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.example.weather.home.view.HomeActivity
import com.example.weather.home.view.MapsActivity

class MainActivity : AppCompatActivity() {

    lateinit var btn: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        btn = findViewById(R.id.btn)
        btn
            .setOnClickListener {
                var intent = Intent(this,HomeActivity()::class.java)
                startActivity(intent)
            }
    }
}