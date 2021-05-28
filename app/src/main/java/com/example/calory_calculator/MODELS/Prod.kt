package com.example.calory_calculator.MODELS

data class Prod(
        val number: Int,
        val offset: Int,
        val results: List<Result>,
        val totalResults: Int
)