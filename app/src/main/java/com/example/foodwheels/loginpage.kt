package com.example.foodwheels

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.foodwheels.databinding.ActivityLoginpageBinding
import com.example.yourapp.termsandconditions
import com.google.firebase.auth.FirebaseAuth

class loginpage : AppCompatActivity() {

    private val binding: ActivityLoginpageBinding by lazy {
        ActivityLoginpageBinding.inflate(layoutInflater)
    }

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()

        binding.loginbtn.setOnClickListener {
            val email = binding.email.text.toString().trim()
            val password = binding.password.text.toString().trim()

            if (email.isEmpty()) {
                binding.email.error = "Email cannot be empty"
                binding.email.requestFocus()
                return@setOnClickListener
            }

            if (password.isEmpty()) {
                binding.password.error = "Password cannot be empty"
                binding.password.requestFocus()
                return@setOnClickListener
            }

            // Validate email and password with Firebase Auth
            auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        // Login successful, move to TermsAndConditions activity
                        val intent = Intent(this, userlocation::class.java)
                        startActivity(intent)
                        finish()
                    } else {
                        // Login failed, show error and stay on login page
                        Toast.makeText(
                            this,
                            "Invalid email or password. Please try again.",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
        }

        // Signup text click - navigate to signup page
        binding.txtsignup.setOnClickListener {
            val intent = Intent(this, signuppage::class.java)
            startActivity(intent)
        }
    }
}
