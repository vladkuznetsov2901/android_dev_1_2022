package com.example.skillbox_hw_quiz.ui.main

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

        val results = arguments?.getString("results")

        binding.answersTextview.text = results

        val againButton = binding.buttonAgain



        againButton.setOnClickListener {
            findNavController().navigate(R.id.action_answersFragment_to_quizFragment)
        }

        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
            findNavController().navigate(R.id.action_answersFragment_to_mainFragment)
        }


    }
}