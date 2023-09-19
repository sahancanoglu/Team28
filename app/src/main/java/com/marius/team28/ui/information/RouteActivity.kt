package com.marius.team28.ui.information

import android.graphics.drawable.Drawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.google.gson.Gson
import com.marius.team28.data.database.Route
import com.marius.team28.databinding.ActivityRouteBinding
import java.io.FileNotFoundException
import java.io.InputStream

class RouteActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRouteBinding
    private lateinit var data : Route

    companion object{
        const val INSTANCE_KEY = "savedInstanceRoute"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRouteBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Extract data about the route
        data = Gson().fromJson(intent.getStringExtra("route"), Route::class.java)

        val grade = "Grade: ${data.grade}"

        binding.routeName.text = data.name
        binding.routeGrade.text= grade

        val fileString = "routes-img/${data.name}.png"

        val inputStream: InputStream
        try {
            // Try to find an image based on the route
            inputStream = assets.open(fileString)
            val d: Drawable? = Drawable.createFromStream(inputStream, null)
            binding.routeImage.setImageDrawable(d)
            inputStream.close()
        }
        catch (e: FileNotFoundException){
            Log.e("Warning", "Image not found")
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        Log.i("MyTag", "onSaveInstanceState")
        val obj = Gson().toJson(data)
        outState.putString(INSTANCE_KEY, obj)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        Log.i("MyTag", "onRestoreInstanceState")
        val stringObj= savedInstanceState.getString(INSTANCE_KEY)
        data = Gson().fromJson(stringObj, Route::class.java)
        binding.routeName.text = data.name
        binding.routeGrade.text= data.grade
    }
}