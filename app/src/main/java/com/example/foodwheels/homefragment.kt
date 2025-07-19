package com.example.foodwheels

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.denzcoskun.imageslider.constants.ScaleTypes
import com.denzcoskun.imageslider.interfaces.ItemClickListener
import com.denzcoskun.imageslider.models.SlideModel
import com.example.foodwheels.adapter.popularitems
import com.example.foodwheels.databinding.FragmentHomefragmentBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener


class homefragment : Fragment() {
      private lateinit var binding: FragmentHomefragmentBinding
      private lateinit var database:FirebaseDatabase
      private lateinit var menuitem:MutableList<model1>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding=FragmentHomefragmentBinding.inflate(inflater,container,false)
        binding.tvExplore.setOnClickListener {
            val bottom=menubottomsheetfragment()
            bottom.show(parentFragmentManager,"Test")
        }
        retriveanddisplay()

        return binding.root;


    }

    private fun retriveanddisplay() {
        database = FirebaseDatabase.getInstance()
        val foodname: DatabaseReference = database.reference.child("menu")
        menuitem = mutableListOf()

        foodname.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    for (foodSnap in snapshot.children) {
                        val item = foodSnap.getValue(model1::class.java)
                        if (item != null) {
                            menuitem.add(item)
                        }
                    }
                    val adapter = popularitems(menuitem, requireContext())
                    binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
                    binding.recyclerView.adapter = adapter
                } else {
                    Toast.makeText(requireContext(), "No data found", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(requireContext(), "Error: ${error.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val imageList = ArrayList<SlideModel>()
        imageList.add(SlideModel(R.drawable.banner1, ScaleTypes.FIT))
        imageList.add(SlideModel(R.drawable.banner3,ScaleTypes.FIT))
        imageList.add(SlideModel(R.drawable.banner5,ScaleTypes.FIT))
        imageList.add(SlideModel(R.drawable.banner2 ,ScaleTypes.FIT))
        imageList.add(SlideModel(R.drawable.banner4 ,ScaleTypes.FIT))
        val imageslider=binding.imageSlider
        imageslider.setImageList(imageList)
        imageslider.setImageList(imageList, ScaleTypes.FIT)
        imageslider.setItemClickListener(object : ItemClickListener {
            override fun doubleClick(position: Int) {
                TODO("Not yet implemented")
            }

            override fun onItemSelected(position: Int) {
              val Itempos=imageList[position]
                val itemmsg="Selected Image $position"
                Toast.makeText(requireContext(),itemmsg, Toast.LENGTH_SHORT).show()
            }
        })

    }


    companion object{

    }



}