package com.example.caloriq.auth

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.caloriq.R

class ForgotPasswordActivity : AppCompatActivity() {

    private val preferenceName = "CaloriQAuth"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_forgot_password)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val etRecoveryEmail = findViewById<EditText>(R.id.etRecoveryEmail)
        val btnRecoverPassword = findViewById<Button>(R.id.btnRecoverPassword)
        val tvRecoveredPassword = findViewById<TextView>(R.id.tvRecoveredPassword)

        btnRecoverPassword.setOnClickListener {
            val email = etRecoveryEmail.text.toString().trim()

            if (email.isBlank()) {
                Toast.makeText(this, "Please enter your email", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val sharedPreferences = getSharedPreferences(preferenceName, MODE_PRIVATE)
            val savedEmail = sharedPreferences.getString("email", "")
            val savedPassword = sharedPreferences.getString("password", "")

            if (email == savedEmail) {
                tvRecoveredPassword.text = "Your password is: $savedPassword"
            } else {
                tvRecoveredPassword.text = "No account found with this email."
            }
        }
    }
}