package com.example.skillbox_hw_quiz.ui.main

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.LinearLayout
import android.widget.RadioGroup
import android.widget.TextView
import com.example.skillbox_hw_quiz.databinding.CustomQuizBinding

class QuizCustom
@JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
) : LinearLayout(context, attrs, defStyleAttr) {
    private val binding = CustomQuizBinding.inflate(LayoutInflater.from(context))

    init {
        addView(binding.root)
    }

    fun getTextView(): TextView {
        return binding.textView1
    }

    fun getRadioGroup(): RadioGroup {
        return binding.group
    }

}