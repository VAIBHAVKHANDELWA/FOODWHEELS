package com.example.yourapp

import android.os.Bundle
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import com.example.foodwheels.databinding.ActivityConditions2Binding

import com.example.foodwheels.databinding.ActivityTermsandconditionsBinding

class conditions2: AppCompatActivity() {

    val binding: ActivityConditions2Binding by lazy {
        ActivityConditions2Binding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.backButton.setOnClickListener {
            finish()
        }



        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                finish()
            }
        })
    }

    override fun onBackPressed() {
        super.onBackPressedDispatcher.onBackPressed()
    }
}
