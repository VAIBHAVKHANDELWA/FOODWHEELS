package com.example.foodwheels

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.example.foodwheels.databinding.ActivityMainBinding
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {
    private lateinit var binding:ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navController = findNavController(R.id.fragmentNavHost)
        val bottomNav: BottomNavigationView = findViewById(R.id.bottomNavigation)
        bottomNav.setupWithNavController(navController)
        binding.foodImage.setOnClickListener {
            val bottom=bottomnavigationsheetfornotification()
            bottom.show(supportFragmentManager,"Test")
        }
    }
}
