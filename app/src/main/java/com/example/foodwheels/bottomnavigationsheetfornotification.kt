package com.example.foodwheels

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.foodwheels.adapter.notadapter
import com.example.foodwheels.databinding.FragmentBottomnavigationsheetfornotificationBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class bottomnavigationsheetfornotification : BottomSheetDialogFragment() {
    private lateinit var binding: FragmentBottomnavigationsheetfornotificationBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment using the provided inflater
        binding = FragmentBottomnavigationsheetfornotificationBinding.inflate(inflater, container, false)

        val msg = listOf("Sorry your order has been Cancelled", "Order is Out for delivery", "We are preparing your order")
        val msglist = listOf(R.drawable.cancelorder, R.drawable.orderdelivery, R.drawable.cook)

        val adapter = notadapter(msglist, msg)
        binding.rcv.layoutManager = LinearLayoutManager(requireContext())
        binding.rcv.adapter = adapter

        return binding.root
    }
}
