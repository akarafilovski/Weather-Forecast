package com.axar.weatherforecast.util

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.net.ConnectivityManager
import android.os.Build
import android.provider.Settings
import androidx.core.app.ActivityCompat

fun Context.arePermissionsGranted(): Boolean{
    if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
        val permissionCoarse = ActivityCompat.checkSelfPermission(
            this,
            Manifest.permission.ACCESS_COARSE_LOCATION
        )
        val permissionFine = ActivityCompat.checkSelfPermission(
            this,
            Manifest.permission.ACCESS_FINE_LOCATION
        )
        return permissionCoarse == PackageManager.PERMISSION_GRANTED && permissionFine == PackageManager.PERMISSION_GRANTED
    }
    return true
}

fun Context.getLocationMode(): Int {
    return try {
        Settings.Secure.getInt(
            this.applicationContext.contentResolver,
            Settings.Secure.LOCATION_MODE
        )
    } catch (e: Settings.SettingNotFoundException) {
        e.printStackTrace()
        -1
    }
}

fun Context.isConnectedToNetwork(): Boolean {
    val connectivityManager = this.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    return connectivityManager.activeNetworkInfo?.isConnectedOrConnecting() ?: false
}