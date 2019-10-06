package com.axar.weatherforecast.util

import android.content.Context
import android.content.SharedPreferences

const val PREFS_FILENAME = "com.axar.weatherforecast.prefs"
const val SHARED_PREFS_LOCATION_NAME = "SHARED_PREFS_LOCATION_NAME"
const val SHARED_PREFS_AFTER_RATIONALE = "SHARED_PREFS_AFTER_RATIONALE"
class PrefsUtil (context: Context) {

    private val prefs: SharedPreferences = context.getSharedPreferences(PREFS_FILENAME, 0)

    var locationName : String
        get() = prefs.getString(SHARED_PREFS_LOCATION_NAME, "").toString()
        set(value) = prefs.edit().putString(SHARED_PREFS_LOCATION_NAME, value).apply()

    var afterRationale : Boolean
        get() = prefs.getBoolean(SHARED_PREFS_AFTER_RATIONALE, false)
        set(value) = prefs.edit().putBoolean(SHARED_PREFS_AFTER_RATIONALE, value).apply()
}