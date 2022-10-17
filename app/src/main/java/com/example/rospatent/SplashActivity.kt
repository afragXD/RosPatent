package com.example.rospatent

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val  i = Intent(this, MainActivity::class.java)
        startActivity(i)
        finish()
    }
}