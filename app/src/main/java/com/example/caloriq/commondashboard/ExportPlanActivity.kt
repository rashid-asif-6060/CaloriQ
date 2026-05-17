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
import com.example.caloriq.utils.BmrCalculator
import com.example.caloriq.utils.CalorieCalculator
import com.example.caloriq.utils.DietPlanGenerator
import com.example.caloriq.utils.UserSession

class ExportPlanActivity : AppCompatActivity() {

    private var exportText: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_export_plan)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val tvExportPlan = findViewById<TextView>(R.id.tvExportPlan)
        val btnSharePlan = findViewById<Button>(R.id.btnSharePlan)

        val userProfile = UserSession.userProfile

        if (userProfile == null) {
            exportText = "No profile data found. Please complete onboarding first."
            tvExportPlan.text = exportText
        } else {
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

            val mealPlan = DietPlanGenerator.generate(userProfile)

            exportText = """
                CaloriQ Diet Plan

                Goal: ${userProfile.goal}
                Daily target: $dailyCalories kcal
                Meals per day: ${userProfile.mealsPerDay}
                Budget: ${userProfile.budget}

                $mealPlan
            """.trimIndent()

            tvExportPlan.text = exportText
        }

        btnSharePlan.setOnClickListener {
            val shareIntent = Intent(Intent.ACTION_SEND)
            shareIntent.type = "text/plain"
            shareIntent.putExtra(Intent.EXTRA_SUBJECT, "CaloriQ Diet Plan")
            shareIntent.putExtra(Intent.EXTRA_TEXT, exportText)

            startActivity(Intent.createChooser(shareIntent, "Share diet plan"))
        }
    }
}