package com.example.caloriq.utils

object BmiCalculator {

    fun calculateBmi(weightKg: Double, heightCm: Double): Double {
        val heightMeter = heightCm / 100
        return weightKg / (heightMeter * heightMeter)
    }

    fun getBmiStatus(bmi: Double): String {
        return when {
            bmi < 18.5 -> "Underweight"
            bmi < 25.0 -> "Normal"
            bmi < 30.0 -> "Overweight"
            else -> "Obese"
        }
    }
}