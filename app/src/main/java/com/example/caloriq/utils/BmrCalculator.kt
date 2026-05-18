package com.example.caloriq.utils

object BmrCalculator {

    fun calculateBmr(
        gender: String,
        weightKg: Double,
        heightCm: Double,
        age: Int
    ): Double {
        return if (gender.lowercase() == "male") {
            10 * weightKg + 6.25 * heightCm - 5 * age + 5
        } else {
            10 * weightKg + 6.25 * heightCm - 5 * age - 161
        }
    }
}