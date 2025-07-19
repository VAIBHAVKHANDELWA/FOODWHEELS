package com.example.foodwheels

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.foodwheels.adapter.menuadapter
import com.example.foodwheels.databinding.FragmentMenubottomsheetfragmentBinding
import com.example.foodwheels.model1
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.firebase.database.*

class menubottomsheetfragment : BottomSheetDialogFragment() {

    private lateinit var binding: FragmentMenubottomsheetfragmentBinding
    private lateinit var database: FirebaseDatabase
    private lateinit var menuList: MutableList<model1>
    private lateinit var adapter: menuadapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMenubottomsheetfragmentBinding.inflate(inflater, container, false)

        // Initialize the list and adapter
        menuList = mutableListOf()
        adapter = menuadapter(menuList,requireContext())

        // Setup RecyclerView
        binding.recyclevvv.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclevvv.adapter = adapter

        // Dismiss bottom sheet on close button click
        binding.imgbtn.setOnClickListener {
            dismiss()
        }

        // Load data from Firebase
        retrieveData()

        return binding.root
    }

    private fun retrieveData() {
        database = FirebaseDatabase.getInstance()
        val ref = database.reference.child("menu")

        ref.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                menuList.clear()
                for (itemSnapshot in snapshot.children) {
                    val item = itemSnapshot.getValue(model1::class.java)
                    if (item != null) {
                        menuList.add(item)
                    }
                }
                adapter.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {
                // You can log the error or show a toast
                // Log.e("FirebaseError", error.message)
            }
        })
    }
}
