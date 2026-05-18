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
import com.example.caloriq.repository.UserProfileRepository
import com.example.caloriq.utils.UserSession

class SplashScreenActivity : AppCompatActivity() {

    private val splashDelay: Long = 2000

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
            val savedProfile = UserProfileRepository.getUserProfile(this)

            val intent = if (savedProfile != null) {
                UserSession.userProfile = savedProfile
                Intent(this, HomeActivity::class.java)
            } else {
                Intent(this, LoginActivity::class.java)
            }

            startActivity(intent)
            finish()
        }, splashDelay)
    }
}