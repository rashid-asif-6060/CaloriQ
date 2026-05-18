package com.example.caloriq.commondashboard

import android.os.Bundle
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.caloriq.R
import com.example.caloriq.utils.BmiCalculator
import com.example.caloriq.utils.UserSession
import java.util.Locale
import kotlin.math.abs

class ProgressActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_progress)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val tvProgressSummary = findViewById<TextView>(R.id.tvProgressSummary)
        val tvProgressBmi = findViewById<TextView>(R.id.tvProgressBmi)

        val userProfile = UserSession.userProfile

        if (userProfile == null) {
            tvProgressSummary.text = "No profile data found. Please complete onboarding first."
            tvProgressBmi.text = "BMI: Not available"
            return
        }

        val remainingWeight = abs(userProfile.currentWeightKg - userProfile.targetWeightKg)

        val bmi = BmiCalculator.calculateBmi(
            weightKg = userProfile.currentWeightKg,
            heightCm = userProfile.heightCm
        )

        val bmiStatus = BmiCalculator.getBmiStatus(bmi)

        tvProgressSummary.text = """
            Goal: ${userProfile.goal}
            Current weight: ${formatNumber(userProfile.currentWeightKg)} kg
            Target weight: ${formatNumber(userProfile.targetWeightKg)} kg
            Remaining difference: ${formatNumber(remainingWeight)} kg
        """.trimIndent()

        tvProgressBmi.text = """
            BMI: ${String.format(Locale.US, "%.1f", bmi)}
            Status: $bmiStatus
        """.trimIndent()
    }

    private fun formatNumber(value: Double): String {
        return if (value % 1.0 == 0.0) {
            value.toInt().toString()
        } else {
            String.format(Locale.US, "%.1f", value)
        }
    }
}