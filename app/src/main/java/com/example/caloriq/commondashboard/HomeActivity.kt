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
import com.example.caloriq.utils.BmiCalculator
import com.example.caloriq.utils.BmrCalculator
import com.example.caloriq.utils.CalorieCalculator
import com.example.caloriq.utils.UserSession
import java.util.Locale

class HomeActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_home)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val tvDailyCalories = findViewById<TextView>(R.id.tvDailyCalories)
        val tvBmi = findViewById<TextView>(R.id.tvBmi)
        val tvBmiStatus = findViewById<TextView>(R.id.tvBmiStatus)
        val tvGoal = findViewById<TextView>(R.id.tvGoal)

        val btnViewDietPlan = findViewById<Button>(R.id.btnViewDietPlan)
        val btnTrackMeal = findViewById<Button>(R.id.btnTrackMeal)
        val btnViewProgress = findViewById<Button>(R.id.btnViewProgress)
        val btnViewProfile = findViewById<Button>(R.id.btnViewProfile)

        val userProfile = UserSession.userProfile

        if (userProfile != null) {
            val bmi = BmiCalculator.calculateBmi(
                weightKg = userProfile.currentWeightKg,
                heightCm = userProfile.heightCm
            )

            val bmiStatus = BmiCalculator.getBmiStatus(bmi)

            val bmr = BmrCalculator.calculateBmr(
                gender = userProfile.gender,
                weightKg = userProfile.currentWeightKg,
                heightCm = userProfile.heightCm,
                age = userProfile.age
            )

            val dailyCalories = CalorieCalculator.calculateDailyCalories(
                bmr = bmr,
                workType = userProfile.workType,
                goal = userProfile.goal
            )

            tvDailyCalories.text = "$dailyCalories kcal"
            tvBmi.text = String.format(Locale.US, "%.1f", bmi)
            tvBmiStatus.text = bmiStatus
            tvGoal.text = userProfile.goal
        } else {
            tvDailyCalories.text = "0 kcal"
            tvBmi.text = "0.0"
            tvBmiStatus.text = "Not available"
            tvGoal.text = "Not set"
        }

        btnViewDietPlan.setOnClickListener {
            val intent = Intent(this, DietPlanActivity::class.java)
            startActivity(intent)
        }

        btnTrackMeal.setOnClickListener {
            val intent = Intent(this, MealTrackingActivity::class.java)
            startActivity(intent)
        }

        btnViewProgress.setOnClickListener {
            val intent = Intent(this, ProgressActivity::class.java)
            startActivity(intent)
        }

        btnViewProfile.setOnClickListener {
            val intent = Intent(this, ProfileActivity::class.java)
            startActivity(intent)
        }
    }
}