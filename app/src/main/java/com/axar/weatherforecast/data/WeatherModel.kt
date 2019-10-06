package com.axar.weatherforecast.data

import com.google.gson.annotations.SerializedName

data class WeatherModel(
    @SerializedName("id")
    var id: Long,

    @SerializedName("main")
    var main: String,

    @SerializedName("description")
    var description: String,

    @SerializedName("icon")
    var icon: String
)