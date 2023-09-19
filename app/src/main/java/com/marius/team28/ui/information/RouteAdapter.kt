package com.marius.team28.ui.information

import android.annotation.SuppressLint
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.marius.team28.data.database.Route
import com.marius.team28.databinding.RouteBinding

class RouteAdapter (val data : MutableList<Route>, private val activity : AppCompatActivity) : RecyclerView.Adapter<RouteAdapter.ViewHolder>() {

    class ViewHolder(val binding: RouteBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = RouteBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.routeName.text = data[position].name
        holder.binding.grade.text = "Difficulty: ${data[position].grade}"

        holder.itemView.setOnClickListener{
            val intent = Intent(activity, RouteActivity::class.java)
            val obj = Gson().toJson(data[position])
            intent.putExtra("route", obj)
            // Starting activity with the data received
            activity.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return data.size
    }
}

