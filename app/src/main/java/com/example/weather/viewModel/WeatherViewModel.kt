package com.example.weather.viewModel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.weather.api.RetrofitClient
import com.example.weather.model.WeatherResponse
import com.example.weather.model.WeatherData
import com.example.weather.utils.kelvinToCelsius
import com.example.weather.utils.unixTimestampToDateTimeString
import com.example.weather.utils.unixTimestampToTimeString
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class WeatherViewModel : ViewModel() {
    private var weatherLiveData = MutableLiveData<List<WeatherData>>()
    fun getWeatherData(city : String) {
        RetrofitClient.api.getCurrentWeatherData(city,"e18ae6fd078e5d8e10128baabb4333f5")
            .enqueue(object  :
                Callback<WeatherResponse> {
                override fun onResponse(call: Call<WeatherResponse>, response: Response<WeatherResponse>) {
                    if (response.body()!=null){
                        Log.d("TAG","${response.body()}")
                        weatherLiveData.value = listOf(
                            WeatherData(
                                dateTime = response.body()!!.dt.unixTimestampToDateTimeString(),
                                temperature = response.body()!!.main.temp.kelvinToCelsius().toString(),
                                cityAndCountry = "${response.body()!!.name}, ${response.body()!!.sys.country}",
                                weatherConditionIconUrl = "http://openweathermap.org/img/w/${response.body()!!.weather[0].icon}.png",
                                weatherConditionIconDescription = response.body()!!.weather[0].description,
                                humidity = "${response.body()!!.main.humidity}%",
                                pressure = "${response.body()!!.main.pressure} mBar",
                                visibility = "${response.body()!!.visibility/1000.0} KM",
                                sunrise = response.body()!!.sys.sunrise.unixTimestampToTimeString(),
                                sunset = response.body()!!.sys.sunset.unixTimestampToTimeString())

                            )
                    } else{
                        return
                    }
                }

                override fun onFailure(call: Call<WeatherResponse>, t: Throwable) {
                    Log.d("TAG",t.message.toString())
                }
            })
    }
    fun observeWeatherLiveData() : MutableLiveData<List<WeatherData>> {
        return weatherLiveData
    }
}