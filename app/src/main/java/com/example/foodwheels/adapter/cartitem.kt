package com.example.foodwheels.adapter

import android.content.Context
import android.net.Uri
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.foodwheels.databinding.Recycleview2dataBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class cartitem(
    private val context: Context,
    private val cartItems: MutableList<String>,
    private val cartitemprice: MutableList<String>,
    private val cartimage: MutableList<String>,
    private val cartdescription: MutableList<String>,
    private val cartQuantities: MutableList<Int>,
    private val cartKeys: MutableList<String>,
    private val onCartEmpty: () -> Unit
) : RecyclerView.Adapter<cartitem.CartItemViewHolder>() {

    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
    private val database: FirebaseDatabase = FirebaseDatabase.getInstance()
    private val userId: String = auth.currentUser?.uid ?: ""
    private val cartRef: DatabaseReference = database.getReference("users").child(userId).child("cart")

    // Local copy of quantities that updates on UI changes
    private var itemQuantity: MutableList<Int> = cartQuantities.toMutableList()

    // Public method to fetch updated item quantities
    fun getUpdatedItemQuantities(): List<Int> {
        return itemQuantity.toList()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartItemViewHolder {
        val binding = Recycleview2dataBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CartItemViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CartItemViewHolder, position: Int) {
        holder.bind(position)
    }

    override fun getItemCount(): Int {
        Log.d("RecyclerView", "Total items: ${cartItems.size}")
        return cartItems.size
    }

    inner class CartItemViewHolder(private val binding: Recycleview2dataBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(position: Int) {
            binding.apply {
                val quantity = itemQuantity[position]
                foodname.text = cartItems[position]
                price.text = cartitemprice[position]
                tvQuantity.text = quantity.toString()

                val uri = Uri.parse(cartimage[position])
                Glide.with(context).load(uri).into(imgv)

                btnIncrease.setOnClickListener {
                    itemQuantity[position]++
                    tvQuantity.text = itemQuantity[position].toString()
                    val key = cartKeys[position]
                    cartRef.child(key).child("quantity").setValue(itemQuantity[position])
                }

                btnDecrease.setOnClickListener {
                    if (itemQuantity[position] > 1) {
                        itemQuantity[position]--
                        tvQuantity.text = itemQuantity[position].toString()
                        val key = cartKeys[position]
                        cartRef.child(key).child("quantity").setValue(itemQuantity[position])
                    }
                }

                btnDelete.setOnClickListener {
                    val keyToRemove = cartKeys[position]
                    cartRef.child(keyToRemove).removeValue()

                    // Remove all associated lists
                    cartItems.removeAt(position)
                    cartitemprice.removeAt(position)
                    cartimage.removeAt(position)
                    cartdescription.removeAt(position)
                    itemQuantity.removeAt(position)
                    cartKeys.removeAt(position)

                    notifyItemRemoved(position)
                    notifyItemRangeChanged(position, cartItems.size)

                    if (cartItems.isEmpty()) {
                        onCartEmpty()
                    }
                }
            }
        }
    }
}
