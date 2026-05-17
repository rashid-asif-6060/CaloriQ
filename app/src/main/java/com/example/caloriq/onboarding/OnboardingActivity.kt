package com.example.caloriq.onboarding

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.RadioGroup
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.caloriq.R

class OnboardingActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_onboarding)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val btnContinue = findViewById<Button>(R.id.btnContinueOnboarding)

        btnContinue.setOnClickListener {
            val etAge = findViewById<EditText>(R.id.etAge)
            val etHeight = findViewById<EditText>(R.id.etHeight)
            val etCurrentWeight = findViewById<EditText>(R.id.etCurrentWeight)
            val etTargetWeight = findViewById<EditText>(R.id.etTargetWeight)
            val rgGender = findViewById<RadioGroup>(R.id.rgGender)
            val rgGoal = findViewById<RadioGroup>(R.id.rgGoal)

            val age = etAge.text.toString().toIntOrNull() ?: 0
            val heightCm = etHeight.text.toString().toDoubleOrNull() ?: 0.0
            val currentWeightKg = etCurrentWeight.text.toString().toDoubleOrNull() ?: 0.0
            val targetWeightKg = etTargetWeight.text.toString().toDoubleOrNull() ?: 0.0

            val gender = when (rgGender.checkedRadioButtonId) {
                R.id.rbMale -> "Male"
                R.id.rbFemale -> "Female"
                else -> "Male"
            }

            val goal = when (rgGoal.checkedRadioButtonId) {
                R.id.rbWeightLoss -> "Weight Loss"
                R.id.rbWeightGain -> "Weight Gain"
                R.id.rbMaintenance -> "Maintenance"
                else -> "Maintenance"
            }

            val intent = Intent(this, PreferenceActivity::class.java)
            intent.putExtra("age", age)
            intent.putExtra("gender", gender)
            intent.putExtra("heightCm", heightCm)
            intent.putExtra("currentWeightKg", currentWeightKg)
            intent.putExtra("targetWeightKg", targetWeightKg)
            intent.putExtra("goal", goal)
            startActivity(intent)
        }
    }
}