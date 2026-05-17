package com.example.caloriq.commondashboard

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.CheckBox
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.caloriq.R
import com.example.caloriq.utils.NotificationHelper

class ReminderActivity : AppCompatActivity() {

    private lateinit var cbMealReminder: CheckBox
    private lateinit var cbWorkoutReminder: CheckBox
    private lateinit var cbProgressReminder: CheckBox
    private lateinit var tvReminderStatus: TextView

    private val notificationPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            Toast.makeText(this, "Notifications enabled", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(this, "Notification permission denied", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_reminder)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        NotificationHelper.createNotificationChannel(this)
        requestNotificationPermissionIfNeeded()

        cbMealReminder = findViewById(R.id.cbMealReminder)
        cbWorkoutReminder = findViewById(R.id.cbWorkoutReminder)
        cbProgressReminder = findViewById(R.id.cbProgressReminder)
        tvReminderStatus = findViewById(R.id.tvReminderStatus)

        cbMealReminder.setOnCheckedChangeListener { _, isChecked ->
            updateReminderStatus()

            if (isChecked) {
                NotificationHelper.showReminderNotification(
                    context = this,
                    title = "Meal reminder enabled",
                    message = "CaloriQ will remind you to stay consistent with your meals."
                )
            }
        }

        cbWorkoutReminder.setOnCheckedChangeListener { _, isChecked ->
            updateReminderStatus()

            if (isChecked) {
                NotificationHelper.showReminderNotification(
                    context = this,
                    title = "Workout reminder enabled",
                    message = "CaloriQ will remind you about your routine."
                )
            }
        }

        cbProgressReminder.setOnCheckedChangeListener { _, isChecked ->
            updateReminderStatus()

            if (isChecked) {
                NotificationHelper.showReminderNotification(
                    context = this,
                    title = "Progress reminder enabled",
                    message = "CaloriQ will remind you to check your progress."
                )
            }
        }

        updateReminderStatus()
    }

    private fun requestNotificationPermissionIfNeeded() {
        val hasPermission = ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.POST_NOTIFICATIONS
        ) == PackageManager.PERMISSION_GRANTED

        if (!hasPermission) {
            notificationPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
        }
    }

    private fun updateReminderStatus() {
        val selectedReminders = mutableListOf<String>()

        if (cbMealReminder.isChecked) {
            selectedReminders.add("Meal reminders enabled")
        }

        if (cbWorkoutReminder.isChecked) {
            selectedReminders.add("Workout reminders enabled")
        }

        if (cbProgressReminder.isChecked) {
            selectedReminders.add("Progress check reminders enabled")
        }

        tvReminderStatus.text = if (selectedReminders.isEmpty()) {
            "No reminders selected"
        } else {
            selectedReminders.joinToString(separator = "\n")
        }
    }
}