package com.example.foodwheels

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.foodwheels.adapter.cartitem
import com.example.foodwheels.databinding.FragmentCartfragmentBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class cartfragment : Fragment() {
    private lateinit var binding: FragmentCartfragmentBinding
    private lateinit var adapter: cartitem

    private lateinit var database: DatabaseReference
    private lateinit var auth: FirebaseAuth

    private val cartNames = mutableListOf<String>()
    private val cartPrices = mutableListOf<String>()
    private val cartImages = mutableListOf<String>()
    private val cartDescriptions = mutableListOf<String>()
    private val cartQuantities = mutableListOf<Int>()
    private val cartKeys = mutableListOf<String>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCartfragmentBinding.inflate(inflater, container, false)
        auth = FirebaseAuth.getInstance()

        val uid = auth.currentUser?.uid
        if (uid != null) {
            database = FirebaseDatabase.getInstance().getReference("users")
                .child(uid).child("cart")
            loadCartData()
        } else {
            Toast.makeText(requireContext(), "User not logged in", Toast.LENGTH_SHORT).show()
            showEmptyCartMessage()
        }

        binding.btnplaceorder.setOnClickListener {
            getorderitemdetails()
            val intent = Intent(requireContext(), PaymentActivity::class.java)
            startActivity(intent)
        }

        return binding.root
    }

    private fun getorderitemdetails() {
        val uid = auth.currentUser?.uid ?: return
        val orderIdReference = FirebaseDatabase.getInstance().reference
            .child("users").child(uid).child("cart")

        val foodName = mutableListOf<String>()
        val foodPrice = mutableListOf<String>()
        val foodImage = mutableListOf<String>()
        val foodDescription = mutableListOf<String>()
        val foodQuantity = mutableListOf<Int>()

        var totalAmount = 0

        orderIdReference.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for (foodSnapshot in snapshot.children) {
                    // Map data into variables (like in your screenshot)
                    val name = foodSnapshot.child("foodname").getValue(String::class.java)
                    val price = foodSnapshot.child("foodprice").getValue(String::class.java)
                    val image = foodSnapshot.child("foodimage").getValue(String::class.java)
                    val description = foodSnapshot.child("foodndescrip").getValue(String::class.java)
                    val quantity = foodSnapshot.child("quantity").getValue(Int::class.java)

                    // Add to respective lists
                    name?.let { foodName.add(it) }
                    price?.let {
                        foodPrice.add(it)
                        val intPrice = it.toIntOrNull() ?: 0
                        totalAmount += intPrice * (quantity ?: 1)
                    }
                    image?.let { foodImage.add(it) }
                    description?.let { foodDescription.add(it) }
                    quantity?.let {
                        foodQuantity.add(it)
                    }
                }

                // After gathering all data, launch PaymentActivity
                val intent = Intent(requireContext(), PaymentActivity::class.java)
                intent.putStringArrayListExtra("foodName", ArrayList(foodName))
                intent.putStringArrayListExtra("foodPrice", ArrayList(foodPrice))
                intent.putStringArrayListExtra("foodImage", ArrayList(foodImage))
                intent.putStringArrayListExtra("foodDescription", ArrayList(foodDescription))
                intent.putIntegerArrayListExtra("foodQuantity", ArrayList(foodQuantity))
                intent.putExtra("totalAmount", totalAmount)

                startActivity(intent)
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(requireContext(), "Failed to fetch cart details", Toast.LENGTH_SHORT).show()
            }
        })
    }


    private fun loadCartData() {
        database.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                Log.d("CartDebug", "Snapshot exists: ${snapshot.exists()}")
                Log.d("CartDebug", "Children count: ${snapshot.childrenCount}")

                cartNames.clear()
                cartPrices.clear()
                cartImages.clear()
                cartDescriptions.clear()
                cartQuantities.clear()
                cartKeys.clear()

                for (itemSnap in snapshot.children) {
                    val key = itemSnap.key ?: continue
                    val name = itemSnap.child("foodname").getValue(String::class.java) ?: continue
                    val price = itemSnap.child("foodprice").getValue(String::class.java) ?: "0"
                    val image = itemSnap.child("foodimage").getValue(String::class.java) ?: ""
                    val desc = itemSnap.child("foodndescrip").getValue(String::class.java) ?: ""
                    val quantity = itemSnap.child("quantity").getValue(Int::class.java) ?: 1

                    cartKeys.add(key)
                    cartNames.add(name)
                    cartPrices.add(price)
                    cartImages.add(image)
                    cartDescriptions.add(desc)
                    cartQuantities.add(quantity)
                }

                if (cartNames.isEmpty()) {
                    showEmptyCartMessage()
                } else {
                    setupRecyclerView()
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(requireContext(), "Error loading cart", Toast.LENGTH_SHORT).show()
                showEmptyCartMessage()
            }
        })
    }

    private fun setupRecyclerView() {
        adapter = cartitem(
            requireContext(),
            cartNames,
            cartPrices,
            cartImages,
            cartDescriptions,
            cartQuantities,
            cartKeys
        ) {
            showEmptyCartMessage()
        }

        binding.recycle.layoutManager = LinearLayoutManager(requireContext())
        binding.recycle.adapter = adapter

        binding.recycle.visibility = View.VISIBLE
        binding.emptyCartMessage.visibility = View.GONE
        binding.emptyCartImage.visibility = View.GONE
    }

    private fun showEmptyCartMessage() {
        binding.recycle.visibility = View.GONE
        binding.emptyCartMessage.visibility = View.VISIBLE
        binding.emptyCartImage.visibility = View.VISIBLE
    }
}
