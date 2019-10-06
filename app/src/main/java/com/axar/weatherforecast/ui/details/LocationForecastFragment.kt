package com.axar.weatherforecast.ui.details


import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.axar.weatherforecast.R
import com.axar.weatherforecast.api.Resource
import com.axar.weatherforecast.data.LocationForecastModel
import com.axar.weatherforecast.util.PrefsUtil
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.fragment_location_forecast.*
import kotlinx.android.synthetic.main.fragment_location_forecast.view.*
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class LocationForecastFragment : Fragment(){

    private val locationForecastViewModel: LocationForecastViewModel by sharedViewModel()
    private val prefsUtil : PrefsUtil by inject()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view: View = inflater.inflate(R.layout.fragment_location_forecast, container, false)
        init(view)
        return view
    }

    private fun init(view: View) {
        initObservers()
        initLayout(view)
    }

    private fun initLayout(view: View) {
        view.groupForecast.visibility = View.GONE
        view.tvEmptyMessage.visibility = View.VISIBLE
    }

    private fun initObservers() {
        locationForecastViewModel.nameLiveData.observe(this, Observer {
            it?.let {
                when (it.status) {
                    Resource.Status.LOADING -> {
                        progressBar.visibility = View.VISIBLE
                        tvEmptyMessage.visibility = View.GONE
                        groupForecast.visibility = View.GONE
                    }
                    Resource.Status.ERROR -> {
                        progressBar.visibility = View.GONE
                        tvEmptyMessage.visibility = View.VISIBLE
                        groupForecast.visibility = View.GONE
                    }
                    Resource.Status.SUCCESS -> {
                        progressBar.visibility = View.GONE
                        groupForecast.visibility = View.VISIBLE

                        it.data?.let { data ->
                            prefsUtil.locationName = data.name
                            populateViews(data)
                        }

                    }
                    else -> { }
                }
            }
        })
    }

    private fun populateViews(forecastModel: LocationForecastModel) {
        tvLocationName.text = forecastModel.name
        tvLocationTemp.text = getString(R.string.temp, forecastModel.main.temp.toInt().toString())
        tvLocationTempMax.text =
            getString(R.string.max_temp, forecastModel.main.temp_max.toInt().toString())
        tvLocationTempMin.text =
            getString(R.string.min_temp, forecastModel.main.temp_min.toInt().toString())
        tvLocationDescription.text = getString(
            R.string.main_description,
            forecastModel.weatherList[0].main,
            forecastModel.weatherList[0].description
        )
        tvLocationPressure.text =
            getString(R.string.pressure, forecastModel.main.pressure.toString())
        tvLocationHumidity.text = forecastModel.main.humidity.toString() + " %"
        Glide.with(requireContext())
            .load("http://openweathermap.org/img/wn/${forecastModel.weatherList[0].icon}@2x.png")
            .into(ivLocationWeatherIcon)
    }

}
