package com.example.caloriq.utils

import com.example.caloriq.model.UserProfile

object DietPlanGenerator {

    fun generate(userProfile: UserProfile): String {
        val avoidList = buildAvoidList(userProfile.allergies, userProfile.dislikedFoods)

        return when (userProfile.mealsPerDay) {
            2 -> generateTwoMealPlan(userProfile.goal, userProfile.budget, userProfile.favoriteFoods, avoidList)
            5 -> generateFiveMealPlan(userProfile.goal, userProfile.budget, userProfile.favoriteFoods, avoidList)
            else -> generateThreeMealPlan(userProfile.goal, userProfile.budget, userProfile.favoriteFoods, avoidList)
        }
    }

    private fun buildAvoidList(allergies: String, dislikedFoods: String): List<String> {
        return "$allergies,$dislikedFoods"
            .split(",")
            .map { it.trim().lowercase() }
            .filter { it.isNotBlank() }
    }

    private fun isAvoided(food: String, avoidList: List<String>): Boolean {
        val normalizedFood = food.lowercase()
        return avoidList.any { avoidedFood ->
            normalizedFood.contains(avoidedFood) || avoidedFood.contains(normalizedFood)
        }
    }

    private fun chooseFrom(options: List<String>, avoidList: List<String>, fallback: String): String {
        return options.firstOrNull { !isAvoided(it, avoidList) } ?: fallback
    }

    private fun chooseProtein(budget: String, favoriteFoods: String, avoidList: List<String>): String {
        val favorites = favoriteFoods.lowercase()

        val options = when (budget) {
            "Low" -> listOf("dal", "soybean", "small fish", "chicken")
            "High" -> listOf("chicken", "fish", "beef", "dal", "yogurt")
            else -> listOf("chicken", "fish", "dal", "soybean")
        }

        val preferredOption = options.firstOrNull {
            favorites.contains(it) && !isAvoided(it, avoidList)
        }

        return preferredOption ?: chooseFrom(options, avoidList, "dal")
    }

    private fun chooseBreakfastProtein(avoidList: List<String>): String {
        return chooseFrom(
            options = listOf("boiled egg", "chicken slice", "dal", "soybean"),
            avoidList = avoidList,
            fallback = "dal"
        )
    }

    private fun chooseCarb(goal: String, avoidList: List<String>): String {
        val options = if (goal == "Weight Loss") {
            listOf("ruti", "small rice portion", "oats")
        } else {
            listOf("rice", "ruti", "oats")
        }

        return chooseFrom(options, avoidList, "rice")
    }

    private fun chooseVegetable(avoidList: List<String>): String {
        return chooseFrom(
            options = listOf("mixed vegetables", "spinach", "cucumber salad", "lau", "beans"),
            avoidList = avoidList,
            fallback = "mixed vegetables"
        )
    }

    private fun chooseSnack(avoidList: List<String>): String {
        return chooseFrom(
            options = listOf("banana", "apple", "guava", "yogurt", "milk", "nuts"),
            avoidList = avoidList,
            fallback = "fruit"
        )
    }

    private fun chooseLightDinnerProtein(protein: String, avoidList: List<String>): String {
        return chooseFrom(
            options = listOf(protein, "chicken", "dal", "soybean"),
            avoidList = avoidList,
            fallback = "dal"
        )
    }

    private fun generateTwoMealPlan(
        goal: String,
        budget: String,
        favoriteFoods: String,
        avoidList: List<String>
    ): String {
        val protein = chooseProtein(budget, favoriteFoods, avoidList)
        val breakfastProtein = chooseBreakfastProtein(avoidList)
        val carb = chooseCarb(goal, avoidList)
        val vegetable = chooseVegetable(avoidList)
        val snack = chooseSnack(avoidList)

        return when (goal) {
            "Weight Gain" -> """
                Meal 1
                $carb, $breakfastProtein, dal, $vegetable, $snack

                Meal 2
                Rice, $protein curry, $vegetable, $snack
            """.trimIndent()

            "Weight Loss" -> """
                Meal 1
                $carb, $breakfastProtein, cucumber salad

                Meal 2
                Small rice portion, $protein, dal, $vegetable
            """.trimIndent()

            else -> """
                Meal 1
                $carb, $breakfastProtein, dal, $vegetable

                Meal 2
                Rice, $protein curry, $vegetable, $snack
            """.trimIndent()
        }
    }

    private fun generateThreeMealPlan(
        goal: String,
        budget: String,
        favoriteFoods: String,
        avoidList: List<String>
    ): String {
        val protein = chooseProtein(budget, favoriteFoods, avoidList)
        val breakfastProtein = chooseBreakfastProtein(avoidList)
        val carb = chooseCarb(goal, avoidList)
        val vegetable = chooseVegetable(avoidList)
        val snack = chooseSnack(avoidList)
        val dinnerProtein = chooseLightDinnerProtein(protein, avoidList)

        return when (goal) {
            "Weight Gain" -> """
                Breakfast
                $carb, $breakfastProtein, $snack

                Lunch
                Rice, $protein curry, dal, $vegetable

                Dinner
                Rice, $dinnerProtein, potato bhorta, $vegetable
            """.trimIndent()

            "Weight Loss" -> """
                Breakfast
                $carb, $breakfastProtein, cucumber salad

                Lunch
                Small rice portion, $protein, dal, $vegetable

                Dinner
                Vegetable soup, $dinnerProtein, salad
            """.trimIndent()

            else -> """
                Breakfast
                $carb, $breakfastProtein, $snack

                Lunch
                Rice, $protein curry, dal, $vegetable

                Dinner
                Rice, $dinnerProtein, $vegetable
            """.trimIndent()
        }
    }

    private fun generateFiveMealPlan(
        goal: String,
        budget: String,
        favoriteFoods: String,
        avoidList: List<String>
    ): String {
        val protein = chooseProtein(budget, favoriteFoods, avoidList)
        val breakfastProtein = chooseBreakfastProtein(avoidList)
        val carb = chooseCarb(goal, avoidList)
        val vegetable = chooseVegetable(avoidList)
        val snackOne = chooseSnack(avoidList)
        val snackTwo = chooseSnack(avoidList)
        val dinnerProtein = chooseLightDinnerProtein(protein, avoidList)

        return when (goal) {
            "Weight Gain" -> """
                Breakfast
                $carb, $breakfastProtein, $snackOne

                Snack 1
                $snackOne

                Lunch
                Rice, $protein curry, dal, $vegetable

                Snack 2
                $snackTwo

                Dinner
                Rice, $dinnerProtein, $vegetable
            """.trimIndent()

            "Weight Loss" -> """
                Breakfast
                $carb, $breakfastProtein, cucumber salad

                Snack 1
                $snackOne

                Lunch
                Small rice portion, $protein, dal, $vegetable

                Snack 2
                $snackTwo

                Dinner
                Soup, $dinnerProtein, salad
            """.trimIndent()

            else -> """
                Breakfast
                $carb, $breakfastProtein, $snackOne

                Snack 1
                $snackOne

                Lunch
                Rice, $protein curry, dal, $vegetable

                Snack 2
                $snackTwo

                Dinner
                Rice, $dinnerProtein, $vegetable
            """.trimIndent()
        }
    }
}