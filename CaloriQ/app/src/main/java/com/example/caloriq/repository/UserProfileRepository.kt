package com.example.caloriq.repository

import com.example.caloriq.model.UserProfile
import com.example.caloriq.utils.BmiCalculator
import com.example.caloriq.utils.BmrCalculator
import com.example.caloriq.utils.CalorieCalculator
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import java.util.Locale

object UserProfileRepository {

    private const val USERS_COLLECTION = "users"

    private val auth: FirebaseAuth by lazy { FirebaseAuth.getInstance() }
    private val firestore: FirebaseFirestore by lazy { FirebaseFirestore.getInstance() }

    fun createAuthUserDocument(
        displayName: String,
        onSuccess: () -> Unit,
        onError: (Exception) -> Unit
    ) {
        val user = auth.currentUser

        if (user == null) {
            onError(IllegalStateException("No logged-in user found"))
            return
        }

        val userData = hashMapOf(
            "uid" to user.uid,
            "email" to (user.email ?: ""),
            "displayName" to displayName,
            "photoUrl" to "",
            "createdAt" to Timestamp.now(),
            "updatedAt" to Timestamp.now(),
            "onboardingComplete" to false,
            "role" to "user",
            "notificationsEnabled" to true,
            "fcmToken" to ""
        )

        firestore.collection(USERS_COLLECTION)
            .document(user.uid)
            .set(userData, SetOptions.merge())
            .addOnSuccessListener { onSuccess() }
            .addOnFailureListener { onError(it) }
    }

    fun saveCurrentUserProfile(
        userProfile: UserProfile,
        onSuccess: () -> Unit,
        onError: (Exception) -> Unit
    ) {
        val user = auth.currentUser

        if (user == null) {
            onError(IllegalStateException("No logged-in user found"))
            return
        }

        val bmi = BmiCalculator.calculateBmi(userProfile.currentWeightKg, userProfile.heightCm)
        val bmr = BmrCalculator.calculateBmr(
            gender = userProfile.gender,
            weightKg = userProfile.currentWeightKg,
            heightCm = userProfile.heightCm,
            age = userProfile.age
        )
        val dailyCalorieTarget = CalorieCalculator.calculateDailyCalories(
            bmr = bmr,
            workType = userProfile.workType,
            goal = userProfile.goal
        )

        val userData = hashMapOf(
            "uid" to user.uid,
            "email" to (user.email ?: ""),
            "updatedAt" to Timestamp.now(),
            "onboardingComplete" to true,
            "age" to userProfile.age,
            "gender" to userProfile.gender.lowercase(Locale.US),
            "heightCm" to userProfile.heightCm,
            "currentWeightKg" to userProfile.currentWeightKg,
            "targetWeightKg" to userProfile.targetWeightKg,
            "fitnessGoal" to toFirestoreGoal(userProfile.goal),
            "activityType" to toFirestoreActivityType(userProfile.workType),
            "doesWorkout" to userProfile.workoutStatus.equals("Yes", ignoreCase = true),
            "mealsPerDay" to userProfile.mealsPerDay,
            "foodAllergies" to splitCsv(userProfile.allergies),
            "dislikedFoods" to splitCsv(userProfile.dislikedFoods),
            "favoriteFoods" to splitCsv(userProfile.favoriteFoods),
            "budgetLevel" to userProfile.budget.lowercase(Locale.US),
            "bmr" to bmr,
            "tdee" to dailyCalorieTarget.toDouble(),
            "bmi" to bmi,
            "bmiCategory" to BmiCalculator.getBmiStatus(bmi).lowercase(Locale.US),
            "dailyCalorieTarget" to dailyCalorieTarget,
            "role" to "user"
        )

        firestore.collection(USERS_COLLECTION)
            .document(user.uid)
            .set(userData, SetOptions.merge())
            .addOnSuccessListener { onSuccess() }
            .addOnFailureListener { onError(it) }
    }

    fun getCurrentUserProfile(
        onResult: (UserProfile?) -> Unit,
        onError: (Exception) -> Unit
    ) {
        val user = auth.currentUser

        if (user == null) {
            onResult(null)
            return
        }

        firestore.collection(USERS_COLLECTION)
            .document(user.uid)
            .get()
            .addOnSuccessListener { document ->
                if (!document.exists() || document.getBoolean("onboardingComplete") != true) {
                    onResult(null)
                    return@addOnSuccessListener
                }

                onResult(
                    UserProfile(
                        age = document.getLong("age")?.toInt() ?: 0,
                        gender = document.getString("gender").orEmpty().replaceFirstChar {
                            if (it.isLowerCase()) it.titlecase(Locale.US) else it.toString()
                        },
                        heightCm = document.getDouble("heightCm") ?: 0.0,
                        currentWeightKg = document.getDouble("currentWeightKg") ?: 0.0,
                        targetWeightKg = document.getDouble("targetWeightKg") ?: 0.0,
                        goal = fromFirestoreGoal(document.getString("fitnessGoal").orEmpty()),
                        workType = fromFirestoreActivityType(document.getString("activityType").orEmpty()),
                        workoutStatus = if (document.getBoolean("doesWorkout") == true) "Yes" else "No",
                        mealsPerDay = document.getLong("mealsPerDay")?.toInt() ?: 3,
                        allergies = joinList(document.get("foodAllergies")),
                        dislikedFoods = joinList(document.get("dislikedFoods")),
                        favoriteFoods = joinList(document.get("favoriteFoods")),
                        budget = document.getString("budgetLevel").orEmpty().replaceFirstChar {
                            if (it.isLowerCase()) it.titlecase(Locale.US) else it.toString()
                        }
                    )
                )
            }
            .addOnFailureListener { onError(it) }
    }

    fun signOut() {
        auth.signOut()
    }

    private fun splitCsv(value: String): List<String> {
        return value.split(",")
            .map { it.trim() }
            .filter { it.isNotBlank() }
    }

    private fun joinList(value: Any?): String {
        return (value as? List<*>)
            ?.mapNotNull { it?.toString() }
            ?.joinToString(", ")
            .orEmpty()
    }

    private fun toFirestoreGoal(goal: String): String {
        return when (goal) {
            "Weight Loss" -> "weight_loss"
            "Weight Gain" -> "weight_gain"
            else -> "maintenance"
        }
    }

    private fun fromFirestoreGoal(goal: String): String {
        return when (goal) {
            "weight_loss" -> "Weight Loss"
            "weight_gain" -> "Weight Gain"
            else -> "Maintenance"
        }
    }

    private fun toFirestoreActivityType(workType: String): String {
        return when (workType) {
            "Physical" -> "physical"
            "Mixed" -> "mixed"
            else -> "mental"
        }
    }

    private fun fromFirestoreActivityType(activityType: String): String {
        return when (activityType) {
            "physical" -> "Physical"
            "mixed" -> "Mixed"
            else -> "Desk"
        }
    }
}
