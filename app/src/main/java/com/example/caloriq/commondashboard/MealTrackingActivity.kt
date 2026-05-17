package com.example.caloriq.commondashboard

import android.os.Bundle
import android.view.View
import android.widget.CheckBox
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.caloriq.R
import com.example.caloriq.utils.BmrCalculator
import com.example.caloriq.utils.CalorieCalculator
import com.example.caloriq.utils.UserSession

class MealTrackingActivity : AppCompatActivity() {

    private var caloriesPerMeal = 0
    private lateinit var tvTrackedCalories: TextView

    private lateinit var cbMeal1: CheckBox
    private lateinit var cbMeal2: CheckBox
    private lateinit var cbMeal3: CheckBox
    private lateinit var cbMeal4: CheckBox
    private lateinit var cbMeal5: CheckBox

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_meal_tracking)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        tvTrackedCalories = findViewById(R.id.tvTrackedCalories)

        cbMeal1 = findViewById(R.id.cbMeal1)
        cbMeal2 = findViewById(R.id.cbMeal2)
        cbMeal3 = findViewById(R.id.cbMeal3)
        cbMeal4 = findViewById(R.id.cbMeal4)
        cbMeal5 = findViewById(R.id.cbMeal5)

        setupMealTracking()
    }

    private fun setupMealTracking() {
        val userProfile = UserSession.userProfile

        if (userProfile == null) {
            tvTrackedCalories.text = "Consumed today: 0 kcal"
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

        caloriesPerMeal = dailyCalories / userProfile.mealsPerDay

        when (userProfile.mealsPerDay) {
            2 -> {
                cbMeal1.text = "Meal 1"
                cbMeal2.text = "Meal 2"
                cbMeal3.visibility = View.GONE
                cbMeal4.visibility = View.GONE
                cbMeal5.visibility = View.GONE
            }

            5 -> {
                cbMeal1.text = "Breakfast"
                cbMeal2.text = "Snack 1"
                cbMeal3.text = "Lunch"
                cbMeal4.text = "Snack 2"
                cbMeal5.text = "Dinner"
            }

            else -> {
                cbMeal1.text = "Breakfast"
                cbMeal2.text = "Lunch"
                cbMeal3.text = "Dinner"
                cbMeal4.visibility = View.GONE
                cbMeal5.visibility = View.GONE
            }
        }

        val checkBoxes = listOf(cbMeal1, cbMeal2, cbMeal3, cbMeal4, cbMeal5)

        checkBoxes.forEach { checkBox ->
            checkBox.setOnCheckedChangeListener { _, _ ->
                updateConsumedCalories()
            }
        }

        updateConsumedCalories()
    }

    private fun updateConsumedCalories() {
        val checkedCount = listOf(cbMeal1, cbMeal2, cbMeal3, cbMeal4, cbMeal5)
            .count { it.visibility == View.VISIBLE && it.isChecked }

        val consumedCalories = checkedCount * caloriesPerMeal
        tvTrackedCalories.text = "Consumed today: $consumedCalories kcal"
    }
}