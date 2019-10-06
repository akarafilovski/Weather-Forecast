package com.axar.weatherforecast.ui

import android.content.Intent
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.view.View
import com.axar.weatherforecast.R
import androidx.appcompat.app.AlertDialog
import com.axar.weatherforecast.util.*
import kotlinx.android.synthetic.main.activity_main.*
import android.location.LocationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.IntentFilter
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import com.axar.weatherforecast.ui.details.LocationForecastViewModel
import com.google.android.gms.location.*
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel

class WeatherForecastActivity : AppCompatActivity() {

    private val locationForecastViewModel: LocationForecastViewModel by viewModel()
    private val prefsUtil : PrefsUtil by inject()

    private val fusedLocationClient: FusedLocationProviderClient by inject()
    private val locationRequest: LocationRequest by inject()
    private val locationCallback : LocationCallback = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult?) {
            locationResult ?: return
            val location = locationResult.locations[0]
            locationForecastViewModel.fetchForecastByLocation(location.latitude, location.longitude)
            fusedLocationClient.removeLocationUpdates(this)
        }
    }

    private val gpsReceiver : BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            if (intent.action == LocationManager.PROVIDERS_CHANGED_ACTION) {
                requestGPSLocation()
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        init()
    }

    private fun init(){

        initLayout()

        if(this.isConnectedToNetwork())
            fetchLocationByName(prefsUtil.locationName)

        if (this.arePermissionsGranted()) {
            requestGPSLocation()
        } else {
            askUserForLocationPermission()
        }
    }

    private fun initLayout(){
        supportActionBar?.elevation = 0f
        supportActionBar?.setDisplayShowTitleEnabled(false)
        updateGoToSettingsVisibility(false)
        tvGoToSettings.setOnClickListener{
            startActivityForResult(
                Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS),
                SETTINGS_LOCATION_REQUEST_CODE)
        }
    }

    private fun fetchLocationByName(name : String) {
        locationForecastViewModel.fetchForecastByCityName(name)
    }

    private fun requestGPSLocation() {
        if (hasPermissionsAndGPS()){
            getGPSLocation()
        }
    }

    private fun getGPSLocation() {
        fusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, null)
    }

    private fun hasPermissionsAndGPS(): Boolean {

        if (!this.arePermissionsGranted()) {
            showRationaleDialog()
            return false
        }

        if (!checkSettingsLocationOn()) {
            updateGoToSettingsVisibility(true)
            return false
        }

        if(!this.isConnectedToNetwork()){
            Toast.makeText(this,getString(R.string.no_network_connection), Toast.LENGTH_LONG).show()
            return false
        }

        updateGoToSettingsVisibility(false)
        return true
    }

    private fun checkSettingsLocationOn(): Boolean = this.getLocationMode() == 3

    private fun askUserForLocationPermission() {
        if (this.shouldShowPermissionRationale()) {
            prefsUtil.afterRationale = true
            showRationaleDialog()
        } else {
            if(!prefsUtil.afterRationale){
                this.requestLocationPermission()
            }
        }
    }

    private fun showRationaleDialog() {
        val builder = AlertDialog.Builder(this)
        builder
            .setTitle(getString(R.string.rationale_dialog_title))
            .setMessage(getString(R.string.rationale_dialog_message))
            .setPositiveButton(android.R.string.ok) { dialog, _ ->
                dialog.dismiss()
                this.requestLocationPermission()
            }
        val dialog = builder.create()
        dialog.show()
    }

    private fun updateGoToSettingsVisibility(show: Boolean){
        if(show)
            rlGoToSettingsContainer.visibility = View.VISIBLE
        else
            rlGoToSettingsContainer.visibility = View.GONE
    }

    override fun onResume() {
        super.onResume()
        registerReceiver(gpsReceiver, IntentFilter(LocationManager.PROVIDERS_CHANGED_ACTION))
    }
    override fun onPause() {
        super.onPause()
        unregisterReceiver(gpsReceiver)
    }
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        if (requestCode == PERMISSIONS_REQUEST_CODE) {
            if (grantResults.size == 2
                && grantResults[0] == PackageManager.PERMISSION_GRANTED
                && grantResults[1] == PackageManager.PERMISSION_GRANTED
            ) {
                requestGPSLocation()
            } else {
                askUserForLocationPermission()
            }
        }

    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == SETTINGS_LOCATION_REQUEST_CODE){
            requestGPSLocation()
        }
    }
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.toolbar_menu, menu)

        val searchItem = menu.findItem(R.id.action_search)
        val searchView = searchItem.actionView as SearchView
        searchView.maxWidth = Int.MAX_VALUE
        searchView.queryHint = getString(R.string.action_search)
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextChange(newText: String): Boolean {
                return false
            }

            override fun onQueryTextSubmit(query: String): Boolean {
                fetchLocationByName(query)
                searchView.clearFocus()
                (menu.findItem(R.id.action_search)).collapseActionView()
                return false
            }
        })

        return true
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return if(item.itemId == R.id.action_current_location){
            requestGPSLocation()
            false
        }else{
            super.onOptionsItemSelected(item)
        }
    }
}
