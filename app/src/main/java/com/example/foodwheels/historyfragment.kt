package com.example.foodwheels

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.foodwheels.adapter.historyadapter
import com.example.foodwheels.databinding.FragmentHistoryfragmentBinding
import com.example.foodwheels.databinding.Recycle3viewBinding


class historyfragment : Fragment() {
    private lateinit var binding: FragmentHistoryfragmentBinding
    private lateinit var adapter: historyadapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding=FragmentHistoryfragmentBinding.inflate(layoutInflater,container,false)
recyclesetup()
        return binding.root
    }

    private fun recyclesetup()
    {
         val foodName = arrayListOf("Pizza", "Burger", "Hotdog", "Noodles", "Ice Cream", "Gulab Jamun")

       val Price = arrayListOf("₹250", "₹150", "₹120", "₹180", "₹100", "₹80")

       val images = arrayListOf(
            R.drawable.pizza, R.drawable.burger, R.drawable.download,
            R.drawable.noodles, R.drawable.ice, R.drawable.gulab
        )
        adapter= historyadapter(foodName,Price,images)
        binding.recycleview.layoutManager= LinearLayoutManager(requireContext())
        binding.recycleview.adapter=adapter
    }



}