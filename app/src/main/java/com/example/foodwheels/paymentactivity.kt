package com.example.foodwheels

import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class PaymentActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mapView: MapView
    private lateinit var googleMap: GoogleMap

    private lateinit var etName: EditText
    private lateinit var etPhone: EditText
    private lateinit var etAddress: EditText
    private lateinit var spinnerPaymentMode: Spinner
    private lateinit var btnProceedToPay: Button
    private lateinit var tvOrderDetails: TextView

    private val auth = FirebaseAuth.getInstance()
    private val database = FirebaseDatabase.getInstance().reference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_paymentactivity)

        // Initialize views
        mapView = findViewById(R.id.mapView)
        etName = findViewById(R.id.etName)
        etPhone = findViewById(R.id.etPhone)
        etAddress = findViewById(R.id.etAddress)
        spinnerPaymentMode = findViewById(R.id.spinnerPaymentMode)
        btnProceedToPay = findViewById(R.id.btnProceedToPay)
        tvOrderDetails = findViewById(R.id.tvOrderDetails)

        // Initialize map
        mapView.onCreate(savedInstanceState)
        mapView.getMapAsync(this)

        // Spinner setup
        val paymentModes = listOf("Select Payment Method", "UPI", "Credit Card", "Cash on Delivery")
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, paymentModes)
        spinnerPaymentMode.adapter = adapter

        // Get intent data
        val foodNames = intent.getStringArrayListExtra("foodName") ?: arrayListOf()
        val foodPrices = intent.getStringArrayListExtra("foodPrice") ?: arrayListOf()
        val foodImages = intent.getStringArrayListExtra("foodImage") ?: arrayListOf()
        val foodDescriptions = intent.getStringArrayListExtra("foodDescription") ?: arrayListOf()
        val foodQuantities = intent.getIntegerArrayListExtra("foodQuantity") ?: arrayListOf()
        val totalAmount = intent.getIntExtra("totalAmount", 0)

        // Load profile (if exists)
        val uid = auth.currentUser?.uid
        if (uid != null) {
            database.child("users").child(uid).child("profile")
                .addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        etName.setText(snapshot.child("name").getValue(String::class.java) ?: "")
                        etPhone.setText(snapshot.child("phone").getValue(String::class.java) ?: "")
                        etAddress.setText(snapshot.child("address").getValue(String::class.java) ?: "")
                    }

                    override fun onCancelled(error: DatabaseError) {
                        Toast.makeText(this@PaymentActivity, "Failed to load profile", Toast.LENGTH_SHORT).show()
                    }
                })
        }

        // Order details popup
        tvOrderDetails.setOnClickListener {
            showOrderDetailsDialog(foodNames, foodPrices, foodQuantities, totalAmount)
        }

        // Payment button logic
        btnProceedToPay.setOnClickListener {
            val name = etName.text.toString().trim()
            val phone = etPhone.text.toString().trim()
            val address = etAddress.text.toString().trim()
            val paymentMethod = spinnerPaymentMode.selectedItem.toString()

            if (name.isEmpty() || phone.isEmpty() || address.isEmpty() || paymentMethod == "Select Payment Method") {
                Toast.makeText(this, "Please fill all details and select a payment method.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val uid = auth.currentUser?.uid ?: return@setOnClickListener
            val orderRef = database.child("orders").child(uid).push()
            val orderId = orderRef.key ?: "unknown"

            // Save profile for next time
            val profileMap = mapOf(
                "name" to name,
                "phone" to phone,
                "address" to address,
                "email" to auth.currentUser?.email
            )
            database.child("users").child(uid).child("profile").setValue(profileMap)

            // Order data
            val orderData = mutableMapOf<String, Any>()
            orderData["name"] = name
            orderData["phone"] = phone
            orderData["address"] = address
            orderData["paymentMethod"] = paymentMethod
            orderData["totalAmount"] = totalAmount
            orderData["orderId"] = orderId
            orderData["timestamp"] = System.currentTimeMillis() // ✅ Timestamp added

            val foodList = mutableListOf<Map<String, Any>>()
            for (i in foodNames.indices) {
                foodList.add(
                    mapOf(
                        "foodName" to foodNames[i],
                        "foodPrice" to foodPrices[i],
                        "foodImage" to foodImages[i],
                        "foodDescription" to foodDescriptions[i],
                        "foodQuantity" to foodQuantities[i]
                    )
                )
            }
            orderData["items"] = foodList

            // Store order in database
            orderRef.setValue(orderData).addOnSuccessListener {
                // Clear cart
                database.child("users").child(uid).child("cart").removeValue()

                // Show success bottom sheet
                val bot = Bottom()
                bot.dismissListener = {
                    finish()
                }
                bot.show(supportFragmentManager, "bottom")
            }.addOnFailureListener {
                Toast.makeText(this, "Failed to place order", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onMapReady(map: GoogleMap) {
        googleMap = map
        val location = LatLng(28.6139, 77.2090) // New Delhi sample location
        googleMap.addMarker(MarkerOptions().position(location).title("Delivery Location"))
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(location, 15f))
    }

    private fun showOrderDetailsDialog(
        foodNames: ArrayList<String>,
        foodPrices: ArrayList<String>,
        foodQuantities: ArrayList<Int>,
        totalAmount: Int
    ) {
        val dialog = BottomSheetDialog(this)
        val view = layoutInflater.inflate(R.layout.dialog_order_details, null)
        val linearContainer = view.findViewById<LinearLayout>(R.id.orderDetailsContainer)
        val totalText = view.findViewById<TextView>(R.id.tvFinalTotal)

        for (i in foodNames.indices) {
            val itemName = foodNames[i]
            val quantity = foodQuantities[i]
            val price = foodPrices[i].toIntOrNull() ?: 0
            val itemTotal = price * quantity

            val itemText = TextView(this).apply {
                text = "$itemName x $quantity = ₹$itemTotal"
                textSize = 16f
                setPadding(16, 12, 16, 12)
            }
            linearContainer.addView(itemText)
        }

        totalText.text = "Total Amount = ₹$totalAmount"
        dialog.setContentView(view)
        dialog.show()
    }

    override fun onResume() {
        super.onResume()
        mapView.onResume()
    }

    override fun onPause() {
        super.onPause()
        mapView.onPause()
    }

    override fun onDestroy() {
        super.onDestroy()
        mapView.onDestroy()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        mapView.onLowMemory()
    }
}
