package com.example.foodwheels

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.foodwheels.adapter.menuadapter
import com.example.foodwheels.databinding.FragmentSearchfragmentBinding

class searchfragment : Fragment() {
    private lateinit var binding: FragmentSearchfragmentBinding

    private val foodName = listOf(
        "Pizza", "Burger", "Hotdog", "Noodles", "Ice Cream", "Gulab Jamun", "Shahi Paneer",
        "Aloo Gobi", "Paneer Butter Masala", "Dal Tadka", "Chole Bhature", "Rajma Chawal",
        "Masala Dosa", "Idli Sambhar", "Vada Pav", "Pav Bhaji", "Veg Biryani",
        "Vegetable Pulao", "Kadhi Pakora", "Dhokla"
    )

    private val Price = listOf(
        "₹250", "₹150", "₹120", "₹180", "₹100", "₹80", "₹300", "₹90", "₹250", "₹180",
        "₹130", "₹140", "₹200", "₹120", "₹50", "₹80", "₹200", "₹190", "₹160", "₹120"
    )

    private val images = listOf(
        R.drawable.pizza, R.drawable.burger,
        R.drawable.download, R.drawable.noodles, R.drawable.ice,
        R.drawable.gulab, R.drawable.shahee, R.drawable.aloogobhi,
        R.drawable.butter, R.drawable.tadka, R.drawable.bhature,
        R.drawable.rajma, R.drawable.dosa, R.drawable.idli,
        R.drawable.vadapav, R.drawable.bahji, R.drawable.biryani,
        R.drawable.pulao, R.drawable.kadhi, R.drawable.dhokla
    )

    private lateinit var adapter: menuadapter
    private var filteredList = mutableListOf<Triple<String, String, Int>>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSearchfragmentBinding.inflate(inflater, container, false)

        // Initialize filteredList with all items initially
        filteredList = foodName.mapIndexed { index, food -> Triple(food, Price[index], images[index]) }.toMutableList()

//        adapter = menuadapter(
//            filteredList.map { it.first }.toMutableList(),
//            filteredList.map { it.second }.toMutableList(),
//            filteredList.map { it.third }.toMutableList()
//        ,requireContext())

        binding.rec.layoutManager = LinearLayoutManager(requireContext())
        binding.rec.adapter = adapter

        setupSearchView()
        return binding.root
    }

    private fun setupSearchView() {
        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                filterMenuItems(query)
                return true
            }

            override fun onQueryTextChange(newText: String): Boolean {
                filterMenuItems(newText)
                return true
            }
        })
    }

    private fun filterMenuItems(query: String) {
        filteredList = if (query.isBlank()) {
            foodName.mapIndexed { index, food -> Triple(food, Price[index], images[index]) }.toMutableList()
        } else {
            foodName.mapIndexedNotNull { index, food ->
                if (food.lowercase().contains(query.lowercase())) {
                    Triple(food, Price[index], images[index])
                } else null
            }.toMutableList()
        }


//        adapter = menuadapter(
//            filteredList.map { it.first }.toMutableList(),
//            filteredList.map { it.second }.toMutableList(),
//            filteredList.map { it.third }.toMutableList(),requireContext()
//        )
        binding.rec.adapter = adapter
    }
}
