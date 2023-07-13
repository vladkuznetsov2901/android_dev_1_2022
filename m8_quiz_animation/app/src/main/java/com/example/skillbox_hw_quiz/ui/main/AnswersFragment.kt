package com.example.skillbox_hw_quiz.ui.main

import android.animation.ObjectAnimator
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.addCallback
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.skillbox_hw_quiz.R
import com.example.skillbox_hw_quiz.databinding.FragmentAnswersBinding

class AnswersFragment : Fragment() {

    private var _binding: FragmentAnswersBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentAnswersBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val results = getResults(this)

        binding.answersTextview.text = results

        ObjectAnimator.ofFloat(
            binding.answersTextview,
            "translationX",
            -1000f,
            0f
        ).apply {
            duration = 1000
            start()
        }

        val againButton = binding.buttonAgain

        againButton.setOnClickListener {
            findNavController().navigate(R.id.action_answersFragment_to_quizFragment)
        }

        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
            findNavController().navigate(R.id.action_answersFragment_to_mainFragment)
        }


    }

    companion object GetArgs {
        fun getResults(fragment: Fragment): String? {
            return fragment.arguments?.getString("results")
        }

    }


}