package com.example.caloriq.repository

import android.content.Context
import com.example.caloriq.model.UserProfile

object UserProfileRepository {

    private const val PREFERENCE_NAME = "CaloriQUserProfile"

    fun saveUserProfile(context: Context, userProfile: UserProfile) {
        val sharedPreferences = context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE)

        sharedPreferences.edit()
            .putInt("age", userProfile.age)
            .putString("gender", userProfile.gender)
            .putFloat("heightCm", userProfile.heightCm.toFloat())
            .putFloat("currentWeightKg", userProfile.currentWeightKg.toFloat())
            .putFloat("targetWeightKg", userProfile.targetWeightKg.toFloat())
            .putString("goal", userProfile.goal)
            .putString("workType", userProfile.workType)
            .putString("workoutStatus", userProfile.workoutStatus)
            .putInt("mealsPerDay", userProfile.mealsPerDay)
            .putString("allergies", userProfile.allergies)
            .putString("dislikedFoods", userProfile.dislikedFoods)
            .putString("favoriteFoods", userProfile.favoriteFoods)
            .putString("budget", userProfile.budget)
            .putBoolean("hasProfile", true)
            .apply()
    }

    fun getUserProfile(context: Context): UserProfile? {
        val sharedPreferences = context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE)
        val hasProfile = sharedPreferences.getBoolean("hasProfile", false)

        if (!hasProfile) {
            return null
        }

        return UserProfile(
            age = sharedPreferences.getInt("age", 0),
            gender = sharedPreferences.getString("gender", "") ?: "",
            heightCm = sharedPreferences.getFloat("heightCm", 0f).toDouble(),
            currentWeightKg = sharedPreferences.getFloat("currentWeightKg", 0f).toDouble(),
            targetWeightKg = sharedPreferences.getFloat("targetWeightKg", 0f).toDouble(),
            goal = sharedPreferences.getString("goal", "") ?: "",
            workType = sharedPreferences.getString("workType", "") ?: "",
            workoutStatus = sharedPreferences.getString("workoutStatus", "") ?: "",
            mealsPerDay = sharedPreferences.getInt("mealsPerDay", 3),
            allergies = sharedPreferences.getString("allergies", "") ?: "",
            dislikedFoods = sharedPreferences.getString("dislikedFoods", "") ?: "",
            favoriteFoods = sharedPreferences.getString("favoriteFoods", "") ?: "",
            budget = sharedPreferences.getString("budget", "") ?: ""
        )
    }

    fun clearUserProfile(context: Context) {
        val sharedPreferences = context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE)
        sharedPreferences.edit().clear().apply()
    }
}