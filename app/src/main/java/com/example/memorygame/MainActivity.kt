package com.example.memorygame

import android.app.ActivityOptions
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.startButton)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Components
        val startButton = findViewById<Button>(R.id.startButton)
        val titleText = findViewById<TextView>(R.id.titleText)

        // Listeners
        startButton.setOnClickListener {
            // Go to GameInterface activity
            val intent = Intent(this, GameInterface::class.java)
            val options = ActivityOptions.makeSceneTransitionAnimation(
                this,
                titleText,
                "title_text_transition"
            )
            startActivity(intent, options.toBundle())
        }
    }
}
