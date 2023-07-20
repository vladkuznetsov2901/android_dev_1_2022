package com.example.m12.ui.main

import android.widget.Button
import android.widget.SearchView
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch


class MainViewModel : ViewModel() {


    private val _state = MutableStateFlow<State>(State.Start)
    val state = _state.asStateFlow()

    fun checkedSearchCharacters(search: SearchView, button: Button) {
        search.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                TODO("Not yet implemented")
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                val characterCount = newText?.length ?: 0

                button.isEnabled = characterCount >= 3

                return true
            }
        })
    }

    fun onSearchButtonClicked() {
        viewModelScope.launch {
            _state.value = State.Loading
            delay(5000)
            _state.value = State.Success
        }
    }


}