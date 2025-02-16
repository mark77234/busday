package com.example.busforblind

import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity

class SettingActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_setting)

        val backToMainButton: ImageButton = findViewById(R.id.backToMain)
        backToMainButton.setOnClickListener {
            finish()
        }
    }
}