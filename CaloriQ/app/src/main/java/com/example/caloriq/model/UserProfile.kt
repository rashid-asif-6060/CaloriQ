package com.example.caloriq.model

data class UserProfile(
    val age: Int,
    val gender: String,
    val heightCm: Double,
    val currentWeightKg: Double,
    val targetWeightKg: Double,
    val goal: String,
    val workType: String,
    val workoutStatus: String,
    val mealsPerDay: Int,
    val allergies: String,
    val dislikedFoods: String,
    val favoriteFoods: String,
    val budget: String
)