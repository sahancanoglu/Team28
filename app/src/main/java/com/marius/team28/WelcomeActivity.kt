package com.marius.team28

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.marius.team28.databinding.ActivityWelcomeBinding

class WelcomeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityWelcomeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Removes actionbar from screen
        try {
            this.supportActionBar!!.hide()
        } catch (e: NullPointerException) {
        }

        binding = ActivityWelcomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Go to the main page on button click
        binding.getStarted.setOnClickListener{
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
    }
}