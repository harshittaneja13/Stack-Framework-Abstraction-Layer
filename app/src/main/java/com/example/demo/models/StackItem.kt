package com.example.demo.models

data class StackItem(
    val closed_state: ClosedState,
    val cta_text: String,
    val open_state: OpenState
)