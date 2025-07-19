package com.example.foodwheels

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth

class splashscreen : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splashscreen)

        Handler(Looper.getMainLooper()).postDelayed({
            val user = FirebaseAuth.getInstance().currentUser
            if (user != null) {
                // ✅ User is already signed in → Go directly to MainActivity
                startActivity(Intent(this, MainActivity::class.java))
            } else {
                // 🚫 No user → Go to startactivity (which leads to login/register)
                startActivity(Intent(this, startactivity::class.java))
            }
            finish()
        }, 3000)
    }
}
