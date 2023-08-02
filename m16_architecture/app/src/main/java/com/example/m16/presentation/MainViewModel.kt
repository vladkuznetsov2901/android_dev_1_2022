package com.example.m16.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.m16.domain.GetUsefulActivityUseCase
import com.example.m16.entity.UsefulActivity
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

class MainViewModel @Inject constructor(private val getUsefulActivityUseCase: GetUsefulActivityUseCase) :
    ViewModel() {

    private val _usefulActivityState = MutableStateFlow<UsefulActivity?>(null)
    val usefulActivityState: StateFlow<UsefulActivity?> get() = _usefulActivityState.asStateFlow()

    fun reloadUsefulActivity() {
        viewModelScope.launch {
            val usefulActivity = getUsefulActivityUseCase.execute()
            _usefulActivityState.value = usefulActivity
        }
    }


}