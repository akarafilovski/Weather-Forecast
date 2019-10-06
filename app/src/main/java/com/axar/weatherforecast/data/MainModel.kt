package com.axar.weatherforecast.data

import com.google.gson.annotations.SerializedName

data class MainModel(
    @SerializedName("temp")
    var temp: Float,

    @SerializedName("pressure")
    var pressure: Int,

    @SerializedName("humidity")
    var humidity: Int,

    @SerializedName("temp_min")
    var temp_min: Float,

    @SerializedName("temp_max")
    var temp_max: Float
)