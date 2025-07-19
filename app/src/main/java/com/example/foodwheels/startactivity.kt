package com.example.foodwheels

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.foodwheels.databinding.ActivityMainBinding
import com.example.foodwheels.databinding.ActivityStartactivityBinding

class startactivity : AppCompatActivity() {
    private val binding: ActivityStartactivityBinding by lazy()
    {
        ActivityStartactivityBinding.inflate(layoutInflater)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContentView(binding.root)
        binding.nextButton.setOnClickListener {
            startActivity(Intent(this, loginpage::class.java))
        }
    }
}