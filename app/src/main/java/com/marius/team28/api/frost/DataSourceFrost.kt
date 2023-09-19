package com.marius.team28.api.frost

import android.util.Log
import com.github.kittinunf.fuel.Fuel
import com.github.kittinunf.fuel.coroutines.awaitString
import com.google.gson.Gson

class DataSourceFrost {
    private val gson = Gson()

    private suspend fun fetchStation(latitude: Double, longitude: Double): WeatherStation? {
        val path = "https://in2000-frostproxy.ifi.uio.no/sources/v0.jsonld?geometry=nearest(POINT($longitude%20$latitude))&elements=precipitation_amount"

        return try {
            gson.fromJson(Fuel.get(path).awaitString(), WeatherStation::class.java)
        } catch (exception: Exception) {
            Log.e("MAIN_ACTIVITY", "A network request exception was thrown: ${exception.message}")
            null
        }
    }

    suspend fun fetchPrecipitation(latitude: Double, longitude: Double, timestamp: String): Precipitation? {
        val weatherStation = fetchStation(latitude, longitude)
        val path = "https://in2000-frostproxy.ifi.uio.no/observations/v0.jsonld?sources=${weatherStation?.data?.get(0)?.id}&referencetime=$timestamp&elements=sum(precipitation_amount%20PT1H)"

        return try {
            gson.fromJson(Fuel.get(path).awaitString(), Precipitation::class.java)
        } catch (exception: Exception) {
            Log.e("MAIN_ACTIVITY", "A network request exception was thrown: ${exception.message}")
            null
        }
    }
}
