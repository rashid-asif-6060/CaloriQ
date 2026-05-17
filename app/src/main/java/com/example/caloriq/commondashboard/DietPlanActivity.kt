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
        tvMealPlan.text = generateMealPlan(userProfile.mealsPerDay, userProfile.goal)
    }

    private fun generateMealPlan(mealsPerDay: Int, goal: String): String {
        return when (mealsPerDay) {
            2 -> generateTwoMealPlan(goal)
            5 -> generateFiveMealPlan(goal)
            else -> generateThreeMealPlan(goal)
        }
    }

    private fun generateTwoMealPlan(goal: String): String {
        return when (goal) {
            "Weight Gain" -> """
                Meal 1
                Rice, chicken curry, dal, mixed vegetables, banana

                Meal 2
                Rice, fish curry, egg, vegetables, milk
            """.trimIndent()

            "Weight Loss" -> """
                Meal 1
                Oats or ruti, boiled egg, cucumber, apple

                Meal 2
                Small rice portion, grilled fish, dal, mixed vegetables
            """.trimIndent()

            else -> """
                Meal 1
                Rice, egg, dal, vegetables

                Meal 2
                Rice, chicken or fish curry, vegetables, yogurt
            """.trimIndent()
        }
    }

    private fun generateThreeMealPlan(goal: String): String {
        return when (goal) {
            "Weight Gain" -> """
                Breakfast
                Ruti, egg omelette, banana, milk

                Lunch
                Rice, chicken curry, dal, mixed vegetables

                Dinner
                Rice, fish curry, potato bhorta, vegetables
            """.trimIndent()

            "Weight Loss" -> """
                Breakfast
                Boiled egg, ruti, cucumber, green tea

                Lunch
                Small rice portion, grilled fish, dal, vegetables

                Dinner
                Vegetable soup, egg or chicken, salad
            """.trimIndent()

            else -> """
                Breakfast
                Ruti, egg, banana

                Lunch
                Rice, fish curry, dal, vegetables

                Dinner
                Rice, chicken curry, vegetables, yogurt
            """.trimIndent()
        }
    }

    private fun generateFiveMealPlan(goal: String): String {
        return when (goal) {
            "Weight Gain" -> """
                Breakfast
                Ruti, egg omelette, banana

                Snack 1
                Milk and nuts

                Lunch
                Rice, chicken curry, dal, vegetables

                Snack 2
                Yogurt and fruit

                Dinner
                Rice, fish curry, egg, vegetables
            """.trimIndent()

            "Weight Loss" -> """
                Breakfast
                Boiled egg, ruti, cucumber

                Snack 1
                Apple or guava

                Lunch
                Small rice portion, fish, dal, vegetables

                Snack 2
                Yogurt or green tea

                Dinner
                Soup, chicken or egg, salad
            """.trimIndent()

            else -> """
                Breakfast
                Ruti, egg, banana

                Snack 1
                Fruit

                Lunch
                Rice, fish curry, dal, vegetables

                Snack 2
                Yogurt

                Dinner
                Rice, chicken curry, vegetables
            """.trimIndent()
        }
    }
}