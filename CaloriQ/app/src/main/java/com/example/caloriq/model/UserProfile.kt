package com.example.caloriq.model

data class UserProfile(
    val age: Int = 0,
    val gender: String = "",
    val heightCm: Double = 0.0,
    val currentWeightKg: Double = 0.0,
    val targetWeightKg: Double = 0.0,
    val goal: String = "",
    val workType: String = "",
    val workoutStatus: String = "",
    val mealsPerDay: Int = 3,
    val allergies: String = "",
    val dislikedFoods: String = "",
    val favoriteFoods: String = "",
    val budget: String = ""
)
