package com.axar.weatherforecast.di

import android.app.Application
import com.axar.weatherforecast.api.LocationGeoApi
import com.axar.weatherforecast.api.LocationNameApi
import com.axar.weatherforecast.ui.details.LocationForecastViewModel
import com.axar.weatherforecast.util.PrefsUtil
import com.google.android.gms.location.*
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import retrofit2.Retrofit

val forecastModule = module{
    single { provideLocationNameApi( get() ) }
    single { provideLocationGeoApi( get() ) }
    single { providePreferenceUtil( get() ) }

    single { provideFusedLocationProviderClient( get() ) }
    single { provideLocationRequest() }


    viewModel { LocationForecastViewModel(get(), get()) }
}

fun provideLocationNameApi(retrofit: Retrofit) : LocationNameApi
        = retrofit.create(LocationNameApi::class.java)

fun provideLocationGeoApi(retrofit: Retrofit) : LocationGeoApi
        = retrofit.create(LocationGeoApi::class.java)

fun providePreferenceUtil(app: Application) : PrefsUtil =
    PrefsUtil(app.applicationContext)

fun provideFusedLocationProviderClient(app: Application) : FusedLocationProviderClient =
    LocationServices.getFusedLocationProviderClient(app.applicationContext)

fun provideLocationRequest() : LocationRequest{
    val locationRequest = LocationRequest.create()
    locationRequest.interval = 10000
    locationRequest.fastestInterval = 5000
    locationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
    return locationRequest
}