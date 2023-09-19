package com.marius.team28.ui.information

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.marius.team28.api.frost.DataSourceFrost
import com.marius.team28.api.frost.Precipitation
import com.marius.team28.api.locationForecast.DataSourceLocationForecast
import com.marius.team28.api.locationForecast.LocationForecast
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class InformationViewModel : ViewModel(){
    // LocationForecast
    private val dataSourceLocationForecast = DataSourceLocationForecast()

    private val locationForecast = MutableLiveData<LocationForecast?>()

    fun getLocationForecast(): LiveData<LocationForecast?> {
        return locationForecast
    }

    fun fetchLocationForecast(latitude: Double, longitude: Double) {
        // Do an asynchronous operation to fetch parties.
        viewModelScope.launch(Dispatchers.IO) {
            dataSourceLocationForecast.fetchLocationForecast(latitude, longitude).also {
                locationForecast.postValue(it)
            }
        }
    }

    // Frost
    private val dataSourceFrost = DataSourceFrost()

    private val precipitation = MutableLiveData<Precipitation?>()

    fun getPrecipitation(): LiveData<Precipitation?> {
        return precipitation
    }

    fun fetchPrecipitation(latitude: Double, longitude: Double, timestamp: String) {
        viewModelScope.launch(Dispatchers.IO) {
            dataSourceFrost.fetchPrecipitation(latitude, longitude, timestamp).also {
                precipitation.postValue(it)
            }
        }
    }
}