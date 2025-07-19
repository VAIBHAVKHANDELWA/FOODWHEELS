package com.example.foodwheels

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.foodwheels.databinding.FragmentProfilefragmentBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class profilefragment : Fragment() {

    private lateinit var binding: FragmentProfilefragmentBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var databaseRef: DatabaseReference

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentProfilefragmentBinding.inflate(inflater, container, false)

        auth = FirebaseAuth.getInstance()
        val uid = auth.currentUser?.uid

        if (uid != null) {
            databaseRef = FirebaseDatabase.getInstance().getReference("users").child(uid).child("profile")

            // Fetch existing profile data
            databaseRef.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    binding.nameInput.setText(snapshot.child("name").getValue(String::class.java) ?: "")
                    binding.emailInput.setText(snapshot.child("email").getValue(String::class.java) ?: "")
                    binding.addressInput.setText(snapshot.child("address").getValue(String::class.java) ?: "")
                    binding.phoneInput.setText(snapshot.child("phone").getValue(String::class.java) ?: "")
                }

                override fun onCancelled(error: DatabaseError) {
                    Toast.makeText(context, "Failed to load profile", Toast.LENGTH_SHORT).show()
                }
            })
        }

        binding.saveButton.setOnClickListener {
            val name = binding.nameInput.text.toString().trim()
            val email = binding.emailInput.text.toString().trim()
            val address = binding.addressInput.text.toString().trim()
            val phone = binding.phoneInput.text.toString().trim()

            if (uid == null) {
                Toast.makeText(context, "User not logged in", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val updatedProfile = mapOf(
                "name" to name,
                "email" to email,
                "address" to address,
                "phone" to phone
            )

            databaseRef.setValue(updatedProfile)
                .addOnSuccessListener {
                    Toast.makeText(context, "Information Successfully Saved", Toast.LENGTH_SHORT).show()
                }
                .addOnFailureListener {
                    Toast.makeText(context, "Failed to save profile", Toast.LENGTH_SHORT).show()
                }
        }

        return binding.root
    }
}
