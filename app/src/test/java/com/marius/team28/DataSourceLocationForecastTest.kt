package com.marius.team28

import com.marius.team28.api.locationForecast.DataSourceLocationForecast
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

internal class DataSourceLocationForecastTest {
    val dataSource = DataSourceLocationForecast()
    // runBlocking is only used for unit tests

    @Test
    fun correct_longitude() = runBlocking {
        val latitude = 59.0
        val longitude = 10.0
        val forecast = dataSource.fetchLocationForecast(latitude, longitude)

        assertEquals(forecast?.geometry?.coordinates?.get(0), longitude)
    }

    @Test
    fun correct_latitude() = runBlocking {
        val latitude = 59.0
        val longitude = 10.0
        val forecast = dataSource.fetchLocationForecast(latitude, longitude)

        assertEquals(forecast?.geometry?.coordinates?.get(1), latitude)
    }
}