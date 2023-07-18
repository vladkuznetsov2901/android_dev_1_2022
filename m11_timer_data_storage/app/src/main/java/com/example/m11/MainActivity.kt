package com.example.m11

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.m11.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val repository = Repository()

        setTextView(repository)

        binding.saveButton.setOnClickListener {
            if (binding.editText.text.isEmpty()) {
                Toast.makeText(this, getString(R.string.enter_text), Toast.LENGTH_SHORT).show()
            } else {
                repository.saveText(binding.editText.text.toString(), this)
                setTextView(repository)
            }
        }

        binding.clearButton.setOnClickListener {
            repository.clearText(this)
            setTextView(repository)
        }

    }


    private fun setTextView(repository: Repository) {
        binding.textView.text = repository.getText(this)
    }

}