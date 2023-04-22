package com.example.weather.ui

import android.R
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.example.weather.databinding.ActivityMainBinding
import com.example.weather.viewModel.WeatherViewModel


class MainActivity : AppCompatActivity(){

    private lateinit var binding: ActivityMainBinding
    private lateinit var viewModel: WeatherViewModel
    var city = ""

    var items = arrayOf("Dallas", "San Jose, Calif", "Texas ", "New York","america","washington")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        viewModel = ViewModelProvider(this)[WeatherViewModel::class.java]

        val adapter: ArrayAdapter<String> = ArrayAdapter<String>(
            this@MainActivity,
            R.layout.simple_spinner_item, items
        )
        adapter.setDropDownViewResource(R.layout.simple_spinner_dropdown_item)
        binding.list.adapter = adapter

        binding.list.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View,
                position: Int,
                id: Long
            ) {
                city = parent.getItemAtPosition(position).toString()
                Log.d("MainActivity", city)
                viewModel.getWeatherData(city)
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }

        viewModel.observeWeatherLiveData().observe(this) { weather ->
            Log.d("MainActivity", "$weather")
            binding.tvDateTime.text = weather[0].dateTime
            binding.tvTemperature.text = weather[0].temperature
            binding.tvCityCountry.text = weather[0].cityAndCountry
            binding.ivWeatherCondition.setImageURI(Uri.parse(weather[0].weatherConditionIconUrl))
            binding.tvWeatherCondition.text = weather[0].weatherConditionIconDescription
            Glide.with(this)
                .load(weather[0].weatherConditionIconUrl)
                .override(200, 200)
                .centerCrop()
                .into(binding.ivWeatherCondition)
        }
    }
}