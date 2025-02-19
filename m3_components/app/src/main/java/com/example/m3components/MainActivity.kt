package com.example.m3components

import android.content.res.Configuration
import android.os.Bundle
import android.os.PersistableBundle
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.m3components.databinding.ActivityMainBinding
import com.google.android.material.slider.Slider
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.math.roundToInt

class MainActivity : AppCompatActivity() {


    private lateinit var binding: ActivityMainBinding
    private var isButtonActive: Boolean = false
    var cnt = 0

    private var job: Job? = null
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)

        setContentView(binding.root)

        savedInstanceState?.let { bundle ->
            binding.slider.isEnabled = bundle.getBoolean("sliderEnabled")
            binding.button.text = bundle.getString("textView")
            cnt = bundle.getInt("cnt")
            timerWork()
        }

        val stepSize = 1


        binding.counter.text = "0"

        binding.slider.valueFrom = 0F
        binding.slider.valueTo = 100F
        binding.slider.stepSize = stepSize.toFloat()

        binding.slider.addOnChangeListener { _, value, _ ->
            val roundedValue = (value / stepSize).roundToInt() * stepSize

            binding.slider.value = roundedValue.toFloat()

            binding.counter.text = binding.slider.value.toInt().toString()

            binding.progressBar.progress = binding.slider.value.toInt()
        }



        binding.button.setOnClickListener {
            timerWork()
        }


    }


    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putBoolean("sliderEnabled", binding.slider.isEnabled)
        outState.putString("textView", binding.button.text.toString())
        outState.putInt("cnt", binding.slider.value.toInt())
    }


    override fun onDestroy() {
        super.onDestroy()
        job?.cancel()
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    private fun timerWork(
    ) {


        if (!isButtonActive) {
            isButtonActive = true
            binding.button.text = "Stop"
            binding.slider.isEnabled = false
            cnt = binding.slider.value.toInt()

            job = CoroutineScope(Dispatchers.Main).launch {
                while (cnt > 0) {
                    delay(1000)
                    cnt -= 1
                    binding.slider.value = cnt.toFloat()
                    binding.counter.text = cnt.toString()
                    binding.progressBar.progress = cnt
                }
                binding.button.text = "Start"
                isButtonActive = false
                showToast("Finish")
                binding.slider.isEnabled = true
            }
        } else {
            binding.slider.isEnabled = true
            isButtonActive = false
            job?.cancel()
            binding.button.text = "Start"
        }
    }


}










