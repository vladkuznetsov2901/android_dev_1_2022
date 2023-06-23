package com.example.skillbox_hw_quiz

import android.graphics.Picture
import android.graphics.drawable.Drawable
import android.graphics.drawable.PictureDrawable
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.skillbox_hw_quiz.databinding.MainActivityBinding
import com.example.skillbox_hw_quiz.ui.main.MainFragment


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        val binding = MainActivityBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.container, MainFragment.newInstance())
                .commitNow()
        }

        binding.welcomeImage.setImageResource(R.drawable.welcome_picture)

    }


}