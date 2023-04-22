package com.example.weather.api

import com.example.weather.model.WeatherResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherApi {
    @GET("data/2.5/weather?")
    fun getCurrentWeatherData(
        @Query("q") city: String?,
        @Query("APPID") app_id: String?
    ): Call<WeatherResponse>
}