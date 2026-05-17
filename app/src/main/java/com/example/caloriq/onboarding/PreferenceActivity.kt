package com.example.caloriq.onboarding

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.RadioGroup
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.caloriq.R
import com.example.caloriq.commondashboard.HomeActivity
import com.example.caloriq.model.UserProfile
import com.example.caloriq.repository.UserProfileRepository
import com.example.caloriq.utils.UserSession

class PreferenceActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_preference)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val btnFinishPreference = findViewById<Button>(R.id.btnFinishPreference)

        btnFinishPreference.setOnClickListener {
            val age = intent.getIntExtra("age", 0)
            val gender = intent.getStringExtra("gender") ?: ""
            val heightCm = intent.getDoubleExtra("heightCm", 0.0)
            val currentWeightKg = intent.getDoubleExtra("currentWeightKg", 0.0)
            val targetWeightKg = intent.getDoubleExtra("targetWeightKg", 0.0)
            val goal = intent.getStringExtra("goal") ?: ""

            val rgWorkType = findViewById<RadioGroup>(R.id.rgWorkType)
            val rgWorkoutStatus = findViewById<RadioGroup>(R.id.rgWorkoutStatus)
            val rgMealsPerDay = findViewById<RadioGroup>(R.id.rgMealsPerDay)
            val rgBudget = findViewById<RadioGroup>(R.id.rgBudget)

            val etAllergies = findViewById<EditText>(R.id.etAllergies)
            val etDislikedFoods = findViewById<EditText>(R.id.etDislikedFoods)
            val etFavoriteFoods = findViewById<EditText>(R.id.etFavoriteFoods)

            val workType = when (rgWorkType.checkedRadioButtonId) {
                R.id.rbDeskWork -> "Desk"
                R.id.rbPhysicalWork -> "Physical"
                R.id.rbMixedWork -> "Mixed"
                else -> {
                    Toast.makeText(this, "Please select your type of work", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }
            }

            val workoutStatus = when (rgWorkoutStatus.checkedRadioButtonId) {
                R.id.rbWorkoutYes -> "Yes"
                R.id.rbWorkoutNo -> "No"
                else -> {
                    Toast.makeText(this, "Please select your workout status", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }
            }

            val mealsPerDay = when (rgMealsPerDay.checkedRadioButtonId) {
                R.id.rbMeals2 -> 2
                R.id.rbMeals3 -> 3
                R.id.rbMeals5 -> 5
                else -> {
                    Toast.makeText(this, "Please select meals per day", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }
            }

            val budget = when (rgBudget.checkedRadioButtonId) {
                R.id.rbBudgetLow -> "Low"
                R.id.rbBudgetMedium -> "Medium"
                R.id.rbBudgetHigh -> "High"
                else -> {
                    Toast.makeText(this, "Please select your budget preference", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }
            }

            val userProfile = UserProfile(
                age = age,
                gender = gender,
                heightCm = heightCm,
                currentWeightKg = currentWeightKg,
                targetWeightKg = targetWeightKg,
                goal = goal,
                workType = workType,
                workoutStatus = workoutStatus,
                mealsPerDay = mealsPerDay,
                allergies = etAllergies.text.toString().trim(),
                dislikedFoods = etDislikedFoods.text.toString().trim(),
                favoriteFoods = etFavoriteFoods.text.toString().trim(),
                budget = budget
            )

            UserSession.userProfile = userProfile
            UserProfileRepository.saveUserProfile(this, userProfile)

            val intent = Intent(this, HomeActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}