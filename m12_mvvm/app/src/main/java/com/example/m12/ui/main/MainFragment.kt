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
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.m12.databinding.FragmentMainBinding
import kotlinx.coroutines.launch


const val KEY_TEXT = "text_view"

class MainFragment : Fragment() {


    private lateinit var binding: FragmentMainBinding

    companion object {
        fun newInstance() = MainFragment()
    }

    private lateinit var viewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this)[MainViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentMainBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val searchView = binding.searchView
        val searchButton = binding.searchButton
        val progressBar = binding.progressBar
        val textView = binding.requestText


        savedInstanceState?.let {bundle ->
            textView.text = bundle.getString(KEY_TEXT)
        }

        viewModel.checkedSearchCharacters(searchView, searchButton)

        searchButton.setOnClickListener {
             viewModel.onSearchButtonClicked()
        }

        stateCheck(progressBar, searchView, searchButton, textView)

    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(KEY_TEXT, binding.requestText.text.toString())
    }


    private fun stateCheck(
        progressBar: ProgressBar,
        searchView: SearchView,
        searchButton: Button,
        textView: TextView,
    ) {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.state.collect { state ->
                    when (state) {
                        State.Start -> {
                            progressBar.isVisible = false
                            searchView.isEnabled = true
                        }

                        State.Loading -> {
                            progressBar.isVisible = true
                            searchView.isEnabled = false
                            searchButton.isEnabled = false
                        }

                        State.Success -> {
                            val result = "По запросу ${searchView.query} ничего не найдено"
                            progressBar.isVisible = false
                            searchView.isEnabled = true
                            searchButton.isEnabled = true
                            textView.text = result
                        }
                    }
                }
            }

        }
    }


}