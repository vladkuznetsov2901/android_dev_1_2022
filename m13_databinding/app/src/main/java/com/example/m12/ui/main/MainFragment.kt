package com.example.m12.ui.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ProgressBar
import android.widget.SearchView
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.m12.databinding.FragmentMainBinding
import kotlinx.coroutines.launch


const val KEY_TEXT = "text_view"

class MainFragment : Fragment() {

    private val viewModel: MainViewModel by viewModels()

    private var _binding: FragmentMainBinding? = null
    private val binding get() = _binding!!

    companion object {
        fun newInstance() = MainFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentMainBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner

        savedInstanceState?.let {bundle ->
            binding.requestText.text = bundle.getString(KEY_TEXT)
        }

        viewModel.checkedSearchCharacters(binding.searchView, binding.searchButton)

        binding.searchButton.setOnClickListener {
             viewModel.onSearchButtonClicked()
        }

        stateCheck(binding.searchView)


    }

    override fun onDestroy() {
        super.onDestroy()
        _binding  = null
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(KEY_TEXT, binding.requestText.text.toString())
    }


    private fun stateCheck(
        searchView: SearchView,

    ) {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.state.collect { state ->
                    when (state) {
                        State.Start -> {

                        }

                        State.Loading -> {

                        }

                        State.Success -> {
                            val result = "По запросу ${searchView.query} ничего не найдено"
                            viewModel.textOnSearch = result
                            binding.invalidateAll()
                        }
                    }
                }
            }

        }
    }


}