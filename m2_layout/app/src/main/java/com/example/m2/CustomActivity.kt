package com.example.m2

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.LinearLayout
import com.example.m2.databinding.ActivityCustomBinding

class CustomActivity
@JvmOverloads
constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyle: Int = 0,
) : LinearLayout(context, attrs, defStyle) {

    private val binding = ActivityCustomBinding.inflate(LayoutInflater.from(context))

    init {
        binding.root.layoutParams =
            ViewGroup.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT)
        addView(binding.root)
    }

    fun setUpText(text: String) {

        binding.customText.text = text
    }

    fun setDownText(text: String) {

        binding.customTextDown.text = text
    }

}
