package com.example.demo.models

data class OpenBody(
    val card: Card? = null,
    val footer: String,
    val items: List<PlanItem>? = null,
    val subtitle: String,
    val title: String
)