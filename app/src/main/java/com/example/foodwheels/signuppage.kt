package com.example.foodwheels

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.foodwheels.databinding.ActivitySignuppageBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class signuppage : AppCompatActivity() {

    private lateinit var binding: ActivitySignuppageBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var database: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignuppageBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance().reference

        binding.signup.setOnClickListener {
            val name = binding.name.text.toString().trim()
            val email = binding.email.text.toString().trim()
            val password = binding.password.text.toString().trim()

            if (name.isEmpty() || email.isEmpty() || password.isEmpty()) {
                if (name.isEmpty()) binding.name.error = "Name cannot be empty"
                if (email.isEmpty()) binding.email.error = "Email cannot be empty"
                if (password.isEmpty()) binding.password.error = "Password cannot be empty"
                return@setOnClickListener
            }

            createAccount(email, password, name)
        }

        binding.signin.setOnClickListener {
            startActivity(Intent(this, loginpage::class.java))
            finish()
        }
    }

    private fun createAccount(email: String, password: String, name: String) {
        auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val uid = auth.currentUser?.uid ?: ""
                val userMap = mapOf("name" to name, "email" to email)

                database.child("users").child(uid).setValue(userMap)
                    .addOnSuccessListener {
                        Toast.makeText(this, "Signup successful. Please login.", Toast.LENGTH_SHORT).show()
                        startActivity(Intent(this, loginpage::class.java))
                        finish()
                    }
                    .addOnFailureListener {
                        Toast.makeText(this, "Signup succeeded but data saving failed.", Toast.LENGTH_SHORT).show()
                        Log.e("DB_ERROR", "Error saving user data", it)
                    }
            } else {
                Toast.makeText(this, "Signup failed: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                Log.e("SIGNUP_ERROR", "Signup failed", task.exception)
            }
        }
    }
}
