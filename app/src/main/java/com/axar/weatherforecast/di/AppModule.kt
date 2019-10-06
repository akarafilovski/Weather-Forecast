package com.axar.weatherforecast.di

import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

const val SERVER_ENDPOINT = "http://api.openweathermap.org/data/2.5/"
// TODO by city name
//http://api.openweathermap.org/data/2.5/weather?q=London&units=metric&appid=fc53786b5fe7d19362712f3fb53281a2
// TODO by lat & lon
//http://api.openweathermap.org/data/2.5/weather?lat=35&lon=139&units=metric&appid=fc53786b5fe7d19362712f3fb53281a2
// TODO image for forecast
//http://openweathermap.org/img/wn/03d@2x.png


val appModule = module{

    // provide Retrofit
    single { provideRetrofit() }
}

fun provideRetrofit() : Retrofit = Retrofit.Builder()
    .baseUrl(SERVER_ENDPOINT)
    .addConverterFactory(GsonConverterFactory.create())
    .build()