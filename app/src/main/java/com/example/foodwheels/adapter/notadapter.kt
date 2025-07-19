package com.example.foodwheels.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.foodwheels.databinding.NavigationsheetitemsBinding

class notadapter(private val listimages: List<Int>, private val list: List<String>) :
    RecyclerView.Adapter<notadapter.adaptnot>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): adaptnot {
        val binding = NavigationsheetitemsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return adaptnot(binding)
    }

    override fun onBindViewHolder(holder: adaptnot, position: Int) {
        holder.bind(position)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    inner class adaptnot(private val binding: NavigationsheetitemsBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(position: Int) {
            binding.apply {
                imgvvv.setImageResource(listimages[position]) // Image View
                itemName.text = list[position] // Text View
            }
        }
    }
}
