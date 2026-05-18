package com.example.caloriq

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.caloriq.auth.LoginActivity
import com.example.caloriq.commondashboard.HomeActivity
import com.example.caloriq.onboarding.OnboardingActivity
import com.example.caloriq.repository.UserProfileRepository
import com.example.caloriq.utils.UserSession
import com.google.firebase.auth.FirebaseAuth

class SplashScreenActivity : AppCompatActivity() {

    private val splashDelay: Long = 2000
    private val auth: FirebaseAuth by lazy { FirebaseAuth.getInstance() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_splash_screen)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        Handler(Looper.getMainLooper()).postDelayed({
            if (auth.currentUser == null) {
                startActivity(Intent(this, LoginActivity::class.java))
                finish()
                return@postDelayed
            }

            UserProfileRepository.getCurrentUserProfile(
                onResult = { savedProfile ->
                    val intent = if (savedProfile != null) {
                        UserSession.userProfile = savedProfile
                        Intent(this, HomeActivity::class.java)
                    } else {
                        Intent(this, OnboardingActivity::class.java)
                    }

                    startActivity(intent)
                    finish()
                },
                onError = {
                    startActivity(Intent(this, LoginActivity::class.java))
                    finish()
                }
            )
        }, splashDelay)
    }
}
