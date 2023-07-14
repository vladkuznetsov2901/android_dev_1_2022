package com.example.skillbox_hw_quiz.ui.main

import android.animation.ObjectAnimator
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.core.view.indices
import androidx.core.view.iterator
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.skillbox_hw_quiz.R
import com.example.skillbox_hw_quiz.databinding.FragmentQuizBinding
import com.example.skillbox_hw_quiz.quiz.Quiz
import com.example.skillbox_hw_quiz.quiz.QuizStorage
import java.util.Locale

class QuizFragment : Fragment() {

    private var _binding: FragmentQuizBinding? = null
    private val binding get() = _binding!!


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentQuizBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val myLang = Locale.getDefault().language


        val quiz = QuizStorage.getQuiz(getLocale(myLang))


        val answers = arrayListOf<Int>()



        binding.custom1.getTextView().text = quiz.questions[0].question
        setAnswers(binding.custom1.getRadioGroup(), quiz, 0)

        binding.custom2.getTextView().text = quiz.questions[1].question
        setAnswers(binding.custom2.getRadioGroup(), quiz, 1)

        binding.custom3.getTextView().text = quiz.questions[2].question
        setAnswers(binding.custom3.getRadioGroup(), quiz, 2)

        transparencyAnim(binding.custom1.getTextView())
        transparencyAnim(binding.custom1.getRadioGroup())

        transparencyAnim(binding.custom2.getTextView())
        transparencyAnim(binding.custom2.getRadioGroup())

        transparencyAnim(binding.custom3.getTextView())
        transparencyAnim(binding.custom3.getRadioGroup())


        val sendButton = binding.buttonSend
        val backButton = binding.buttonBack

        backButton.setOnClickListener {
            findNavController().popBackStack()
        }


        sendAnswers(quiz, answers, sendButton)


    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setAnswers(radioGroup: RadioGroup, quiz: Quiz, index: Int) {
        for (i in radioGroup.indices) {
            val button = radioGroup.getChildAt(i) as RadioButton
            button.text = quiz.questions[index].answers[i]
        }
    }

    private fun checkRadioGroup(radioGroup: RadioGroup): Boolean {
        return radioGroup.checkedRadioButtonId != -1
    }

    private fun searchRadioButton(custom: QuizCustom): String {
        val selectedRadioButtonId = custom.getRadioGroup().checkedRadioButtonId
        val selectedRadioButton =
            custom.getRadioGroup().findViewById<RadioButton>(selectedRadioButtonId)
        return selectedRadioButton.text.toString()
    }

    private fun sendAnswers(quiz: Quiz, answers: ArrayList<Int>, sendButton: Button) {
        sendButton.setOnClickListener {

            if (!checkRadioGroup(binding.custom1.getRadioGroup()) ||
                !checkRadioGroup(binding.custom2.getRadioGroup()) ||
                !checkRadioGroup(binding.custom3.getRadioGroup())
            ) {
                Toast.makeText(context, R.string.toast_from_answers, Toast.LENGTH_SHORT).show()

            } else {
                answers.add(searchAnswerInRadioGroup(binding.custom1))
                answers.add(searchAnswerInRadioGroup(binding.custom2))
                answers.add(searchAnswerInRadioGroup(binding.custom3))
                val results = QuizStorage.answer(quiz, answers)

                val bundle = bundleOf("results" to results)

                findNavController().navigate(R.id.action_quizFragment_to_answersFragment, bundle)

                binding.custom1.getRadioGroup().clearCheck()
                binding.custom2.getRadioGroup().clearCheck()
                binding.custom3.getRadioGroup().clearCheck()

            }
        }
    }

    private fun searchAnswerInRadioGroup(custom: QuizCustom): Int {
        var index = 0
        for (button in custom.getRadioGroup()) {
            if (button is RadioButton) {
                if (button.text == searchRadioButton(custom)) {
                    return index
                } else index++
            }
        }
        return -1
    }

    private fun transparencyAnim(view: View) {
        ObjectAnimator.ofFloat(
            view,
            "alpha",
            0f,
            1f
        ).apply {
            duration = 2000
            start()
        }
    }


    private fun getLocale(lang: String): QuizStorage.Locale {
        if (lang == "ru") {
            return QuizStorage.Locale.Ru
        } else QuizStorage.Locale.En

        return QuizStorage.Locale.En

    }

}
