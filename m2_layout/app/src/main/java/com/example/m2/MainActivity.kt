package com.example.m2

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.m2.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.customView.setUpText("I m from code")

        binding.customView.setDownText("And I too")
    }
}

