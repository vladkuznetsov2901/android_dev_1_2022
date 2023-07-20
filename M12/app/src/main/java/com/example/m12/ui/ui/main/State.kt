package com.example.m12.ui.ui.main

sealed class State {
    object Loading : State()
    object Success : State()
}
