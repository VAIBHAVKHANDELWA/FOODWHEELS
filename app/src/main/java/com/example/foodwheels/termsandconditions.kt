package com.example.yourapp

import android.os.Bundle
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity

import com.example.foodwheels.databinding.ActivityTermsandconditionsBinding

class termsandconditions: AppCompatActivity() {

    val binding: ActivityTermsandconditionsBinding by lazy {
        ActivityTermsandconditionsBinding.inflate(layoutInflater)
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
