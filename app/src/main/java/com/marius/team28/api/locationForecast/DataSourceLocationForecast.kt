package com.marius.team28.api.locationForecast

import android.util.Log
import com.github.kittinunf.fuel.Fuel
import com.github.kittinunf.fuel.coroutines.awaitString
import com.google.gson.Gson

class DataSourceLocationForecast {
    private val gson = Gson()

    suspend fun fetchLocationForecast(latitude: Double, longitude: Double): LocationForecast? {
        val path = "https://in2000-apiproxy.ifi.uio.no/weatherapi/locationforecast/2.0/compact?lat=$latitude&lon=$longitude"

        return try {
            gson.fromJson(Fuel.get(path).awaitString(), LocationForecast::class.java)
        } catch (exception: Exception) {
            Log.e("MAIN_ACTIVITY", "A network request exception was thrown: ${exception.message}")
            null
        }
    }
}