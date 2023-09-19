package com.marius.team28.ui.dashboard

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.marius.team28.databinding.CragBinding
import com.marius.team28.ui.information.CragActivity
import android.content.Intent
import com.google.gson.Gson
import com.marius.team28.data.database.Crag


class CragAdapter(val data: MutableList<Crag>, private val activity: AppCompatActivity) : RecyclerView.Adapter<CragAdapter.ViewHolder>() {

    class ViewHolder(val binding: CragBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = CragBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.name.text = data[position].name

        holder.itemView.setOnClickListener{
            val intent = Intent(activity, CragActivity::class.java)
            val obj = Gson().toJson(data[position])
            intent.putExtra("data", obj)
            // Starting activity with the data received
            activity.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return data.size
    }

}