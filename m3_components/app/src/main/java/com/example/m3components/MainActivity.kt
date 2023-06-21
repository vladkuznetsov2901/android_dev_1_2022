package com.example.m3components

import android.os.Bundle
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.slider.Slider
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.math.roundToInt

class MainActivity : AppCompatActivity() {
    private var job: Job? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        var isButtonActive = false
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        var cnt: Int

        val stepSize = 1


        val progressBar = findViewById<ProgressBar>(R.id.progressBar)

        val counterTimer = findViewById<TextView>(R.id.counter)

        val slider = findViewById<Slider>(R.id.slider)

        val button = findViewById<Button>(R.id.button)


        counterTimer.text = "0"

        slider.valueFrom = 0F
        slider.valueTo = 100F
        slider.stepSize = stepSize.toFloat()

        slider.addOnChangeListener { _, value, _ ->
            val roundedValue = (value / stepSize).roundToInt() * stepSize

            slider.value = roundedValue.toFloat()

            counterTimer.text = slider.value.toInt().toString()

            progressBar.progress = slider.value.toInt()
        }

        button.setOnClickListener {
            if (!isButtonActive) {
                isButtonActive = true
                button.text = "Stop"
                slider.isEnabled = false
                cnt = slider.value.toInt()

                job = CoroutineScope(Dispatchers.Main).launch {
                    while (cnt > 0) {
                        delay(1000)
                        cnt -= 1
                        slider.value = cnt.toFloat()
                        counterTimer.text = cnt.toString()
                        progressBar.progress = cnt
                    }
                    button.text = "Start"
                    isButtonActive = false
                    showToast("Finish")
                    slider.isEnabled = true
                }
            } else {
                slider.isEnabled = true
                isButtonActive = false
                job!!.cancel()
                button.text = "Start"
            }

        }


    }

    override fun onDestroy() {
        super.onDestroy()
        job?.cancel()
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}










