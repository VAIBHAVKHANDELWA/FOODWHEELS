package com.example.foodwheels

import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.bumptech.glide.Glide
import com.example.foodwheels.databinding.ActivityItemdetailsBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class itemdetails : AppCompatActivity() {
    private var foodname: String? = null
    private var foodprice: String? = null
    private var foodimage: String? = null
    private var fooddescription: String? = null
    private var ingredients: String? = null
    private lateinit var auth:FirebaseAuth

    private lateinit var binding: ActivityItemdetailsBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityItemdetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        auth=FirebaseAuth.getInstance()
        foodname = intent.getStringExtra("itemname")
        foodprice = intent.getStringExtra("itemprice")
        foodimage = intent.getStringExtra("itemimage")
        fooddescription = intent.getStringExtra("itemdescription")
        ingredients = intent.getStringExtra("itemingredients")
        with(binding)
        {
            txttt.text = foodname
            t1t1.text = fooddescription
            t1t2.text = ingredients
            Glide.with(this@itemdetails).load(Uri.parse(foodimage)).into(ttt)

        }
        binding.imgbtn.setOnClickListener {
            finish()
        }
        binding.btn.setOnClickListener {
            additemtocart()
        }
    }

    private fun additemtocart() {
        val database = FirebaseDatabase.getInstance().reference
        val userid = auth.currentUser?.uid ?: return

        val cartRef = database.child("users").child(userid).child("cart")
        val itemName = foodname.toString()

        // First, check if item already exists
        cartRef.orderByChild("foodname").equalTo(itemName)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                        // Item exists: increase quantity
                        for (child in snapshot.children) {
                            val currentQty = child.child("quantity").getValue(Int::class.java) ?: 1
                            val updatedQty = currentQty + 1
                            child.ref.child("quantity").setValue(updatedQty)
                                .addOnSuccessListener {
                                    Toast.makeText(
                                        this@itemdetails,
                                        "Quantity updated",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                        }
                    } else {
                        // Item doesn't exist: add new
                        val cart = cartitems(
                            foodname = itemName,
                            foodprice = foodprice.toString(),
                            foodimage = foodimage.toString(),
                            foodndescrip = fooddescription.toString(),
                            quantity = 1
                        )
                        cartRef.push().setValue(cart)
                            .addOnSuccessListener {
                                Toast.makeText(
                                    this@itemdetails,
                                    "Item added to cart",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                            .addOnFailureListener {
                                Toast.makeText(
                                    this@itemdetails,
                                    "Item not added to cart",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    Toast.makeText(
                        this@itemdetails,
                        "Failed: ${error.message}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            })
    }

}
