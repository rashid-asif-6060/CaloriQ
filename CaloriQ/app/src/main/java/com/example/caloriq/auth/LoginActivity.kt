package com.example.caloriq.auth

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.caloriq.R
import com.example.caloriq.commondashboard.HomeActivity
import com.example.caloriq.onboarding.OnboardingActivity
import com.example.caloriq.repository.UserProfileRepository
import com.example.caloriq.utils.UserSession
import com.google.firebase.auth.FirebaseAuth

class LoginActivity : AppCompatActivity() {

    private val auth: FirebaseAuth by lazy { FirebaseAuth.getInstance() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_login)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val etLoginEmail = findViewById<EditText>(R.id.etLoginEmail)
        val etLoginPassword = findViewById<EditText>(R.id.etLoginPassword)
        val btnLogin = findViewById<Button>(R.id.btnLogin)
        val btnGoToRegister = findViewById<Button>(R.id.btnGoToRegister)

        btnLogin.setOnClickListener {
            val email = etLoginEmail.text.toString().trim()
            val password = etLoginPassword.text.toString().trim()

            if (email.isBlank()) {
                Toast.makeText(this, "Please enter your email", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (password.isBlank()) {
                Toast.makeText(this, "Please enter your password", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            btnLogin.isEnabled = false

            auth.signInWithEmailAndPassword(email, password)
                .addOnSuccessListener {
                    UserProfileRepository.getCurrentUserProfile(
                        onResult = { savedProfile ->
                            btnLogin.isEnabled = true
                            Toast.makeText(this, "Login successful", Toast.LENGTH_SHORT).show()

                            val intent = if (savedProfile != null) {
                                UserSession.userProfile = savedProfile
                                Intent(this, HomeActivity::class.java)
                            } else {
                                Intent(this, OnboardingActivity::class.java)
                            }

                            startActivity(intent)
                            finish()
                        },
                        onError = { exception ->
                            btnLogin.isEnabled = true
                            Toast.makeText(this, exception.message ?: "Failed to load profile", Toast.LENGTH_SHORT).show()
                        }
                    )
                }
                .addOnFailureListener { exception ->
                    btnLogin.isEnabled = true
                    Toast.makeText(this, exception.message ?: "Invalid email or password", Toast.LENGTH_SHORT).show()
                }
        }

        btnGoToRegister.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }
    }
}
