package com.example.skillbox_hw_quiz.ui.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.Toast
import androidx.core.view.indices
import androidx.core.view.iterator
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import androidx.fragment.app.replace
import com.example.skillbox_hw_quiz.R
import com.example.skillbox_hw_quiz.databinding.FragmentQuizBinding
import com.example.skillbox_hw_quiz.quiz.Quiz
import com.example.skillbox_hw_quiz.quiz.QuizStorage

class QuizFragment : Fragment() {

    private var _binding: FragmentQuizBinding? = null
    private val binding get() = _binding!!


    companion object {
        fun newInstance() = MainFragment()
    }

    private lateinit var viewModel: MainViewModel


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentQuizBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val fragmentManager = requireActivity().supportFragmentManager

        val quiz = QuizStorage.getQuiz(locale = QuizStorage.Locale.Ru)

        val answers = arrayListOf<Int>()



        binding.custom1.getTextView().text = quiz.questions[0].question
        setAnswers(binding.custom1.getRadioGroup(), quiz, 0)

        binding.custom2.getTextView().text = quiz.questions[1].question
        setAnswers(binding.custom2.getRadioGroup(), quiz, 1)

        binding.custom3.getTextView().text = quiz.questions[2].question
        setAnswers(binding.custom3.getRadioGroup(), quiz, 2)


        val sendButton = binding.buttonSend
        val backButton = binding.buttonBack

        backButton.setOnClickListener {
            fragmentManager.popBackStack()
        }


        sendButton.setOnClickListener {

            if (!checkRadioGroup(binding.custom1.getRadioGroup()) ||
                !checkRadioGroup(binding.custom2.getRadioGroup()) ||
                !checkRadioGroup(binding.custom3.getRadioGroup())
            ) {
                Toast.makeText(context, "Вы ответили не на все вопросы!", Toast.LENGTH_SHORT).show()

            } else {
                answers.add(searchAnswerInRadioGroup(binding.custom1))
                answers.add(searchAnswerInRadioGroup(binding.custom2))
                answers.add(searchAnswerInRadioGroup(binding.custom3))
                val results = QuizStorage.answer(quiz, answers)

                val bundle = Bundle().apply {
                    putString("results", results)
                }
                parentFragmentManager.commit {
                    replace<AnswersFragment>(containerViewId = R.id.container, args = bundle)
                    addToBackStack(AnswersFragment::class.java.simpleName)
                }

                binding.custom1.getRadioGroup().clearCheck()
                binding.custom2.getRadioGroup().clearCheck()
                binding.custom3.getRadioGroup().clearCheck()

            }
        }


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

}