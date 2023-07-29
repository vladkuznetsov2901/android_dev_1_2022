package com.example.m12.ui.main

import android.content.Context
import android.widget.Button
import android.widget.SearchView
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.m12.R
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch


class MainViewModel() : ViewModel() {


    private val _state = MutableStateFlow<State>(State.Start)
    val state = _state.asStateFlow()

    var textOnSearch = "Здесь будет отображаться результат запроса"

    fun checkedSearchCharacters(search: SearchView, button: Button) {
        search.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                if (query != null) {
                    if (query.length >= 3) {
                        onSearchButtonClicked()
                    }
                }

                return true
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
            state.value.isLoading = true
            delay(5000)
            _state.value = State.Success
            state.value.isLoading = false
        }
    }


}