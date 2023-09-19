@file:Suppress("DEPRECATION")

package com.marius.team28.maps


import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.location.Location
import android.os.AsyncTask
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.*
import com.google.gson.Gson
import com.marius.team28.MainViewModel
import com.marius.team28.data.database.Crag
import com.marius.team28.databinding.FragmentMapsBinding
import com.marius.team28.ui.information.CragActivity
import okhttp3.OkHttpClient
import okhttp3.Request


@Suppress("DEPRECATION")
class MapsFragment : Fragment(),  OnMapReadyCallback {
    private var _binding: FragmentMapsBinding? = null
    private lateinit var mMap: GoogleMap
    private var fusedLocationProviderClient: FusedLocationProviderClient? = null
    private val viewModel: MainViewModel by viewModels()

    private var currentLocation : Location? = null

    companion object {
        const val  PERMISSION_LOCATION_REQUEST_CODE = 1
    }

    private val binding get() = _binding!!
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(context as Activity)
        val bundle = arguments

        if (bundle == null) {
            binding.cragInfo.isEnabled = false
            binding.cragInfo.visibility = View.GONE
            cragsOverview()
        }

        setUpDirections()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMapsBinding.inflate(inflater, container, false)

        return binding.root
    }

    private fun setUpDirections(){
        // Check if location has permission to be used
        if (ActivityCompat.checkSelfPermission(
                context as Activity,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                context as Activity,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // Ask for permission
            ActivityCompat.requestPermissions(context as Activity, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), PERMISSION_LOCATION_REQUEST_CODE)
            Toast.makeText(context, "Make sure your device has access to your location and try again", Toast.LENGTH_LONG).show()
            return
        }

        mMap.isMyLocationEnabled= true

        val bundle = arguments
        if (bundle != null){
            binding.cragInfo.isEnabled = false
            binding.cragInfo.visibility = View.GONE

            // Extract crag data from bundle
            val latPassed = bundle.getDouble("latitude", 0.0)
            val lngPassed = bundle.getDouble("longitude", 0.0)
            val placeName = bundle.getString("placename")
            val currentSelectedPlace = LatLng(latPassed, lngPassed)

            // Adds crag marker to map and zooms in
            mMap.addMarker(MarkerOptions().position(currentSelectedPlace).title(placeName))
            mMap.moveCamera(CameraUpdateFactory.newLatLng(currentSelectedPlace))
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentSelectedPlace, 10f))

            // Gets the last known location from phone
            val task = fusedLocationProviderClient?.lastLocation
            task?.addOnSuccessListener {  location ->
                if(location != null){
                    currentLocation = location

                    // Adds a marker to the location
                    val userLocation = LatLng(currentLocation?.latitude!!, currentLocation?.longitude!!)
                    mMap.addMarker(MarkerOptions().position(userLocation).title("You are here").icon(
                        BitmapDescriptorFactory.defaultMarker(
                            BitmapDescriptorFactory.HUE_AZURE
                        )
                    ))
                    val url  = getDirectionURL(userLocation, currentSelectedPlace)
                    GetDirection(url).execute()

                } else {
                    Toast.makeText(context, "Could not access current location, make sure you have given location permission", Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    private fun cragsOverview(){
        viewModel.getCrag()
        var placePos : LatLng
        var name : String
        // Sets bounds to Oslo
        val cragsBounds = LatLngBounds(
            LatLng(59.92996241159201, 10.518653928327186),
            LatLng(59.9938420447521, 11.210482421430052)
        )

        viewModel.getCrag().observe(viewLifecycleOwner) { crag ->
            if (crag != null) {
                // Generate all map markers
                for (i in crag){
                    placePos = LatLng(i.latitude, i.longitude)
                    name = i.name
                    val currentMarker = mMap.addMarker(MarkerOptions().position(placePos).title(name))
                    currentMarker?.tag = crag.indexOf(i)
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(cragsBounds.center, 9f))

                    mMap.setOnInfoWindowClickListener { marker ->
                        clickMarkerInfo(marker, crag)
                    }

                    mMap.setOnMarkerClickListener { marker ->
                        clickMarker(marker, crag)

                        true
                    }

                    mMap.setOnCameraMoveListener {
                        binding.cragInfo.isEnabled = false
                        binding.cragInfo.visibility = View.GONE
                    }
                }
            }
        }
    }

    // Click on "crag info" button
    private fun clickMarkerInfo(marker: Marker, list: List<Crag>){
        val clickCount = marker.tag as? Int

        clickCount?.let {
            // Starts CragActivity with the clicked crag
            val intent = Intent(activity, CragActivity::class.java)
            val obj = Gson().toJson(list[clickCount])
            intent.putExtra("data", obj)
            requireActivity().startActivity(intent)
        }
    }

    // Click on name of crag
    private fun clickMarker(marker: Marker, list: List<Crag>){
        marker.showInfoWindow()
        val clickCount = marker.tag as? Int
        clickCount?.let {
            binding.cragInfo.background.alpha = 150

            binding.cragInfo.isEnabled = true
            binding.cragInfo.visibility = View.VISIBLE

            binding.cragInfo.setOnClickListener {
                // Starts CragActivity with the clicked crag
                val intent = Intent(activity, CragActivity::class.java)
                val obj = Gson().toJson(list[clickCount])
                intent.putExtra("data", obj)
                requireActivity().startActivity(intent)
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.mapView.onCreate(savedInstanceState)
        binding.mapView.onResume()
        binding.mapView.getMapAsync(this)
    }


    private fun getDirectionURL(origin: LatLng, dest: LatLng): String {
        return "https://maps.googleapis.com/maps/api/directions/json?origin=${origin.latitude},${origin.longitude}" +
                "&destination=${dest.latitude},${dest.longitude}" +
                "&sensor=false" +
                "&mode=driving" +
                "&key=AIzaSyCyKWIQMqG_3RmW9-SIkzPrT0oeK-mDmq8"
    }

    @SuppressLint("StaticFieldLeak")
    private inner class GetDirection(val url : String) : AsyncTask<Void, Void, List<List<LatLng>>>() {
        @Deprecated("Deprecated in Java")
        override fun doInBackground(vararg params: Void?): List<List<LatLng>> {
            val client = OkHttpClient()
            val request = Request.Builder().url(url).build()
            val response = client.newCall(request).execute()
            val data = response.body!!.string()

            val result = ArrayList<List<LatLng>>()
            try {
                val respObj = Gson().fromJson(data, GoogleMapsClass::class.java)
                val path = ArrayList<LatLng>()
                // Create navigation path
                for (i in 0 until respObj.routes[0].legs[0].steps.size) {
                    val startLatLng = LatLng(respObj.routes[0].legs[0].steps[i].start_location.lat.toDouble(),
                        respObj.routes[0].legs[0].steps[i].start_location.lng.toDouble())

                    path.add(startLatLng)
                    val endLatLng = LatLng(respObj.routes[0].legs[0].steps[i].end_location.lat.toDouble(),
                        respObj.routes[0].legs[0].steps[i].end_location.lng.toDouble())
                    path.add(endLatLng)

                }
                result.add(path)
            } catch (e: Exception) {
                e.printStackTrace()
            }
            return result

        }
        @Deprecated("Deprecated in Java")
        override fun onPostExecute(result: List<List<LatLng>>) {
            // Draws path between markers
            val lineOption = PolylineOptions()
            for (i in result.indices){
                lineOption.addAll(result[i])
                lineOption.width(10f)
                lineOption.color(Color.BLUE)
                lineOption.geodesic(true)
            }
            mMap.addPolyline(lineOption)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onResume() {
        super.onResume()
        binding.mapView.onResume()
    }

    override fun onStart() {
        super.onStart()
        binding.mapView.onStart()
    }

    override fun onStop() {
        super.onStop()
        binding.mapView.onStop()
    }

    override fun onPause() {
        super.onPause()
        binding.mapView.onPause()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        binding.mapView.onLowMemory()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)

        binding.mapView.onSaveInstanceState(outState)
    }
}
