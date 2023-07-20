package com.example.m12.ui.main

sealed class State {
    object Start : State()
    object Loading : State()
    object Success : State()
}
