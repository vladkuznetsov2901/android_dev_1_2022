package com.example.m17.presentation

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.m17.R
import com.example.m17.presentation.ui.main.MainFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

    }
}