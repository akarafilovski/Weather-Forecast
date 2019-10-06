package com.axar.weatherforecast.ui.details

import androidx.lifecycle.*
import com.axar.weatherforecast.api.LocationGeoApi
import com.axar.weatherforecast.api.LocationNameApi
import com.axar.weatherforecast.api.Resource
import com.axar.weatherforecast.data.LocationForecastModel
import kotlinx.coroutines.Dispatchers
import retrofit2.HttpException

class LocationForecastViewModel(
    private val nameApi: LocationNameApi,
    private val geoApi: LocationGeoApi
) : ViewModel() {

    var nameLiveData = MediatorLiveData<Resource<LocationForecastModel>>()

    // Fetch weather forecast by city name
    fun fetchForecastByCityName(cityName: String){
        fetchByName(getCityForecast(cityName))
    }
    private fun fetchByName(source : LiveData<Resource<LocationForecastModel>>){
        nameLiveData.value = Resource.Loading()
        nameLiveData.addSource(source) { resource ->
            nameLiveData.value = resource
            nameLiveData.removeSource(source)
        }
    }
    private fun getCityForecast( cityName : String) : LiveData<Resource<LocationForecastModel>>{
        return liveData(Dispatchers.IO){
            try{
                val forecastByName = nameApi.getForecastByCityName(cityName)
                emit( Resource.Success(forecastByName) )
            }catch (e: HttpException){
                emit( Resource.Error("Try later."))
            }
        }
    }

    // Fetch weather forecast lat and lon
    fun fetchForecastByLocation(lat: Double, lon: Double){
        fetchByLocation(getLocationForecast(lat, lon))
    }
    private fun fetchByLocation(source : LiveData<Resource<LocationForecastModel>>){
        nameLiveData.value = Resource.Loading()
        nameLiveData.addSource(source) { resource ->
            nameLiveData.value = resource
            nameLiveData.removeSource(source)
        }
    }
    private fun getLocationForecast(lat:Double, lon:Double) : LiveData<Resource<LocationForecastModel>>{
        return liveData(Dispatchers.IO){
            try{
                val forecastByLocation = geoApi.getForecastByLocation(lat,lon)
                emit( Resource.Success(forecastByLocation) )
            }catch (e: HttpException){
                emit( Resource.Error("Try later."))
            }
        }
    }
}
