package com.marius.team28.ui.information

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.marius.team28.MainViewModel
import com.marius.team28.R
import com.marius.team28.api.frost.Precipitation
import com.marius.team28.api.locationForecast.LocationForecast
import com.marius.team28.data.database.Crag
import com.marius.team28.data.database.Route
import com.marius.team28.databinding.ActivityCragBinding
import com.marius.team28.maps.MapsFragment
import java.text.SimpleDateFormat
import java.util.*

class CragActivity : AppCompatActivity() {
    private lateinit var binding: ActivityCragBinding

    private val informationViewModel: InformationViewModel by viewModels()
    private val viewModel: MainViewModel by viewModels()

    private lateinit var data : Crag

    private var forecastData: LocationForecast? = null
    private var fetchedForecast = false
    private var frostData: Precipitation? = null
    private var fetchedFrost = false

    companion object{
        const val INSTANCE_KEY = "savedInstance"
    }

    @SuppressLint("SetTextI18n", "SimpleDateFormat")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCragBinding.inflate(layoutInflater)
        setContentView(binding.root)

        data = Gson().fromJson(intent.getStringExtra("data"), Crag::class.java)

        binding.title.text = data.name
        binding.description.text = data.description
        binding.responsible.text = "Responsible for crag: ${data.responsible}"
        binding.weatherStatus.text = "Temperature: Loading..."

        binding.recyclerRoutes.layoutManager = LinearLayoutManager(this)

        // Display routes
        var adapter: RecyclerView.Adapter<RouteAdapter.ViewHolder>
        viewModel.fetchRoutes(data.name)
        viewModel.getRoutes().observe(this){
            adapter = RouteAdapter(it as MutableList<Route>, this)
            binding.recyclerRoutes.adapter = adapter
        }

        // Get data from API
        if (forecastData == null) getLocationForecast(data.latitude, data.longitude)

        // Make a calendar and date format
        val calendar = Calendar.getInstance()
        // Year-month-day format
        val sdf = SimpleDateFormat("yyyy-MM-dd")

        // Get the interval between yesterday and today
        val currentDate = sdf.format(calendar.time)
        calendar.add(Calendar.DATE, -1)
        val previousDate = sdf.format(calendar.time)
        val timeframe = "$previousDate/${currentDate}T23"

        // Get data from API
        if (frostData == null) getPrecipitation(data.latitude, data.longitude, timeframe)

        // Prepare map for navigation
        buildMap(data.latitude, data.longitude, data.name )
        supportActionBar?.setDisplayShowHomeEnabled(true)
    }

    private fun buildMap (latitude: Double, longitude: Double, cragName: String){
        val mFragmentManager = supportFragmentManager
        val mFragmentTransaction = mFragmentManager.beginTransaction()
        val mFragment = MapsFragment()

        binding.button.setOnClickListener{
            // Passes crag data to the map
            val mBundle = Bundle()
            mBundle.putDouble("latitude",latitude)
            mBundle.putDouble("longitude",longitude)
            mBundle.putString("placename", cragName)
            // Applies MapFragment instance onto FrameLayout
            mFragment.arguments = mBundle
            mFragmentTransaction.replace(
                R.id.frameLayout, mFragment).commit()
            binding.button.isEnabled = false
        }
    }

    @SuppressLint("SetTextI18n")
    private fun getLocationForecast(latitude: Double, longitude: Double) {
        informationViewModel.fetchLocationForecast(latitude, longitude)

        informationViewModel.getLocationForecast().observe(this) {
            forecastData = it
            fetchedForecast = true

            // Get current time by adding an offset of 2
            val currentData = it?.properties?.timeseries?.get(2)?.data

            // Extract data form current time
            val temperature = currentData?.instant?.details?.airTemperature
            val weatherIcon: String? = currentData?.next1Hours?.summary?.symbolCode

            // Checks data validity and displays it to the user
            if (temperature != null) {
                val weatherStatus = "Temperature: $temperature°C"

                binding.weatherStatus.text = weatherStatus
            } else {
                // No API response
                binding.weatherStatus.text = "Temperature unavailable.\nPlease make sure you're connected to WiFi."
            }

            if (weatherIcon != null) {
                val resId = resources.getIdentifier("@drawable/$weatherIcon", null, packageName)
                binding.weatherIcon.setImageResource(resId)
            }

            calculateWeather(forecastData, frostData)
        }
    }

    private fun getPrecipitation(latitude: Double, longitude: Double, timestamp: String) {
        informationViewModel.fetchPrecipitation(latitude, longitude, timestamp)

        informationViewModel.getPrecipitation().observe(this) {
            frostData = it
            fetchedFrost = true

            calculateWeather(forecastData, frostData)
        }
    }

    @SuppressLint("SetTextI18n")
    private fun calculateWeather(forecast: LocationForecast?, precipitation: Precipitation?) {
        // Make sure all data is ready
        if (!fetchedForecast || !fetchedFrost) return

        var sufficientData = true

        // Process Frost data
        val totalItemCount = precipitation?.totalItemCount
        var rainEarlier = false
        if (totalItemCount != null) {
            val lastFrost = precipitation.data[totalItemCount - 1]
            val frostPrecipitation = lastFrost.observations[0].value
            if (frostPrecipitation != null) {
                rainEarlier = frostPrecipitation > 0.0
            }
        } else {
            sufficientData = false
        }

        // Process LocationForecast data
        var rainLastHours = 0.0
        if (precipitation != null) {
            for (i in 0..2) {
                val forecastHour = forecast?.properties?.timeseries?.get(i)?.data
                val precipitationHour = forecastHour?.next1Hours?.details?.precipitationAmount
                rainLastHours += precipitationHour ?: 0.0
            }
        } else {
            sufficientData = false
        }

        if (!sufficientData) return

        // Make the prediction
        if (rainEarlier && rainLastHours == 0.0) {
            binding.prediction.text = "It may be wet"
        } else if (rainEarlier && rainLastHours > 0.1) {
            binding.prediction.text = "It is most likely wet"
        } else {
            binding.prediction.text = "It is most likely not wet"
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)

        super.onSaveInstanceState(outState)

        val obj = Gson().toJson(data)
        outState.putString(INSTANCE_KEY, obj)

        outState.putString("LocationForecast", Gson().toJson(forecastData))
        outState.putString("Frost", Gson().toJson(frostData))
    }
    @SuppressLint("SetTextI18n")
    override fun onRestoreInstanceState(savedInstanceState: Bundle) {

        super.onRestoreInstanceState(savedInstanceState)
        val stringObj = savedInstanceState.getString(INSTANCE_KEY)

        data = Gson().fromJson(stringObj, Crag::class.java)
        binding.title.text = data.name
        binding.description.text = data.description
        binding.responsible.text = "Responsible for crag: ${data.responsible}"

        forecastData = Gson().fromJson(savedInstanceState.getString("LocationForecast"), LocationForecast::class.java)
        fetchedForecast = true
        val temperature = forecastData?.properties?.timeseries?.get(2)?.data?.instant?.details?.airTemperature
        if (temperature != null) {
            binding.weatherStatus.text = "Temperature: ${temperature}°C"
        } else {
            binding.weatherStatus.text = "Temperature unavailable.\nPlease make sure you're connected to WiFi."
        }

        frostData = Gson().fromJson(savedInstanceState.getString("Frost"), Precipitation::class.java)
        fetchedFrost = true

        calculateWeather(forecastData, frostData)
    }
}