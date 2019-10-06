package com.axar.weatherforecast.util

import android.Manifest
import android.app.Activity
import androidx.core.app.ActivityCompat

const val PERMISSIONS_REQUEST_CODE: Int = 1000
const val SETTINGS_LOCATION_REQUEST_CODE: Int = 2000

fun Activity.shouldShowPermissionRationale(): Boolean {
    return ActivityCompat.shouldShowRequestPermissionRationale(
        this,
        Manifest.permission.ACCESS_COARSE_LOCATION
    )
}

fun Activity.requestLocationPermission() {
    ActivityCompat.requestPermissions(
        this,
        arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION),
        PERMISSIONS_REQUEST_CODE
    )
}