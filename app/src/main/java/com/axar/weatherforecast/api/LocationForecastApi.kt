package com.axar.weatherforecast.api

import com.axar.weatherforecast.data.LocationForecastModel
import retrofit2.http.GET
import retrofit2.http.Query

const val OPENWEATHERMAP_API_KEY = "fc53786b5fe7d19362712f3fb53281a2"

interface LocationNameApi {
    @GET("weather?appid=${OPENWEATHERMAP_API_KEY}&units=metric")
    suspend fun getForecastByCityName(
        @Query("q") cityName: String
    ) : LocationForecastModel
}
interface LocationGeoApi {
    @GET("weather?appid=${OPENWEATHERMAP_API_KEY}&units=metric")
    suspend fun getForecastByLocation(
        @Query("lat") lat: Double,
        @Query("lon") lon: Double
    ) : LocationForecastModel
}