package com.marius.team28.ui.dashboard

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.marius.team28.databinding.FragmentDashboardBinding
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.viewModels
import com.marius.team28.MainViewModel
import com.marius.team28.data.database.Crag


class DashboardFragment :  Fragment() {
    private lateinit var binding: FragmentDashboardBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentDashboardBinding.inflate(inflater, container, false)
        val viewModel: MainViewModel by viewModels()

        val root: View = binding.root

        val layoutManager: RecyclerView.LayoutManager?
        var adapter: RecyclerView.Adapter<CragAdapter.ViewHolder>?

        layoutManager = LinearLayoutManager(this.context)
        binding.recyclerView.layoutManager = layoutManager

        // Display crags when data is ready
        viewModel.getCrag().observe(viewLifecycleOwner) {
            adapter = CragAdapter(it as MutableList<Crag>, activity as AppCompatActivity)
            binding.recyclerView.adapter = adapter
        }
        return root
    }
}