package com.example.m12.ui.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch


class MainViewModel : ViewModel() {


    private val _state = MutableStateFlow<State>(State.Start)
    val state = _state.asStateFlow()

    var textFromSearch = "Здесь будет отображаться результат запроса"
    val searchText = MutableStateFlow("")
    private var searchJob: Job? = null


    @OptIn(FlowPreview::class)
    fun checkedSearchCharacters() {
        searchText.asStateFlow()
            .debounce(300)
            .onEach { query ->
                if (query.length >= 3) {
                    searchJob?.cancel()
                    searchJob = onSearchButtonClicked()
                }
            }
            .launchIn(viewModelScope)
    }

    private fun onSearchButtonClicked(): Job {
        return viewModelScope.launch {
            _state.value = State.Loading
            state.value.isLoading = true
            delay(5000)
            _state.value = State.Success
            state.value.isLoading = false

        }
    }


}