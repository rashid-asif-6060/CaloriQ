package com.example.caloriq.commondashboard

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.caloriq.R
import com.example.caloriq.auth.LoginActivity
import com.example.caloriq.repository.UserProfileRepository
import com.example.caloriq.utils.UserSession
import java.util.Locale

class ProfileActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_profile)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val tvProfileBody = findViewById<TextView>(R.id.tvProfileBody)
        val btnLogout = findViewById<Button>(R.id.btnLogout)
        val userProfile = UserSession.userProfile

        if (userProfile == null) {
            tvProfileBody.text = "No profile data found. Please complete onboarding first."
        } else {
            tvProfileBody.text = """
                Age: ${userProfile.age}
                Gender: ${userProfile.gender}
                Height: ${formatNumber(userProfile.heightCm)} cm
                Current weight: ${formatNumber(userProfile.currentWeightKg)} kg
                Target weight: ${formatNumber(userProfile.targetWeightKg)} kg
                Goal: ${userProfile.goal}

                Work type: ${userProfile.workType}
                Workout: ${userProfile.workoutStatus}
                Meals per day: ${userProfile.mealsPerDay}
                Budget: ${userProfile.budget}

                Allergies: ${formatEmpty(userProfile.allergies)}
                Disliked foods: ${formatEmpty(userProfile.dislikedFoods)}
                Favorite foods: ${formatEmpty(userProfile.favoriteFoods)}
            """.trimIndent()
        }

        btnLogout.setOnClickListener {
            UserSession.userProfile = null
            UserProfileRepository.signOut()

            val intent = Intent(this, LoginActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
        }
    }

    private fun formatEmpty(value: String): String {
        return if (value.isBlank()) "None" else value
    }

    private fun formatNumber(value: Double): String {
        return if (value % 1.0 == 0.0) {
            value.toInt().toString()
        } else {
            String.format(Locale.US, "%.1f", value)
        }
    }
}
