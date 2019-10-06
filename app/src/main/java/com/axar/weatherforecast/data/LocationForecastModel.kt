package com.axar.weatherforecast.data

import com.google.gson.annotations.SerializedName

data class LocationForecastModel(
    @SerializedName("id")
    var id: Long,

    @SerializedName("name")
    var name: String,

    @SerializedName("weather")
    var weatherList: List<WeatherModel>,

    @SerializedName("main")
    var main: MainModel
)