package com.example.caloriq.utils

object CalorieCalculator {

    fun calculateDailyCalories(
        bmr: Double,
        workType: String,
        goal: String
    ): Int {
        val activityMultiplier = when (workType) {
            "Physical" -> 1.6
            "Mixed" -> 1.4
            else -> 1.2
        }

        val maintenanceCalories = bmr * activityMultiplier

        val goalAdjustment = when (goal) {
            "Weight Loss" -> -400
            "Weight Gain" -> 400
            else -> 0
        }

        return (maintenanceCalories + goalAdjustment).toInt()
    }
}