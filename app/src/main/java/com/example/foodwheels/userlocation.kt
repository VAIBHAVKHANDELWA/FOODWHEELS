package com.example.foodwheels

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Button
import android.widget.CheckBox
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.foodwheels.databinding.ActivityUserlocationBinding
import com.example.yourapp.conditions2
import com.example.yourapp.termsandconditions


class userlocation : AppCompatActivity() {
    private val binding: ActivityUserlocationBinding by lazy {
        ActivityUserlocationBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)

        // Location list setup
        val locationlist = arrayOf(
            "JAIPUR", "DEHRADUN", "AGRA", "MUMBAI", "DELHI",
            "RAJASTHAN", "PUNJAB", "KOLKATA", "WEST BENGAL"
        )
        val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, locationlist)
        val autoCompleteTextView: AutoCompleteTextView = binding.autoview
        autoCompleteTextView.setAdapter(adapter)

        // User agreement components
        val checkBoxAgree: CheckBox = binding.checkboxAgree
        val submitButton: Button = binding.submitButton
        val userAgreementLink: TextView = binding.userAgreement

        // Initially disable the submit button
        submitButton.isEnabled = false

        // Enable submit button when checkbox is checked
        checkBoxAgree.setOnCheckedChangeListener { _, isChecked ->
            submitButton.isEnabled = isChecked
        }

        // Handle user agreement link click
        userAgreementLink.setOnClickListener {
            val intent = Intent(this, conditions2::class.java)
            startActivity(intent)

        }
        // Handle submit button click
        submitButton.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }

    }
}
