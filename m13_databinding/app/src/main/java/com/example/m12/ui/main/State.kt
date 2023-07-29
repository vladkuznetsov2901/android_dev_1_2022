package com.example.m12.ui.main

sealed class State(
    var isLoading: Boolean = false
) {
    object Start : State()
    object Loading : State()
    object Success : State()
}
