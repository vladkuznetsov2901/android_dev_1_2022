package com.example.m2

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.LinearLayout
import com.example.m2.databinding.MyCustomViewBinding
import java.time.format.DecimalStyle

class CustomView
@JvmOverloads
constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyle: Int = 0,
) : LinearLayout(context, attrs, defStyle) {

    val binding = MyCustomViewBinding.inflate(LayoutInflater.from(context))

    init {
        addView(binding.root)
    }

    fun setUpText(text: String) {

        binding.customText.text = text
    }

    fun setDownText(text: String) {

        binding.customTextDown.text = text
    }

}
