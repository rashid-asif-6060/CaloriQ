package com.example.caloriq.commondashboard

import android.os.Bundle
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.caloriq.R
import com.example.caloriq.utils.BmrCalculator
import com.example.caloriq.utils.CalorieCalculator
import com.example.caloriq.utils.DietPlanGenerator
import com.example.caloriq.utils.UserSession

class DietPlanActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_diet_plan)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val tvDietCalories = findViewById<TextView>(R.id.tvDietCalories)
        val tvMealPlan = findViewById<TextView>(R.id.tvMealPlan)

        val userProfile = UserSession.userProfile

        if (userProfile == null) {
            tvDietCalories.text = "Daily target: 0 kcal"
            tvMealPlan.text = "No profile data found. Please complete onboarding first."
            return
        }

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

        tvDietCalories.text = "Daily target: $dailyCalories kcal"
        tvMealPlan.text = DietPlanGenerator.generate(userProfile)
    }
}