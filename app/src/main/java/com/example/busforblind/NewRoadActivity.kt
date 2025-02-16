package com.example.busforblind

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton

class NewRoadActivity : Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.favorites_new_road)

        val backToMainButton: ImageButton = findViewById(R.id.backToMain)
        val roadNameInput: EditText = findViewById(R.id.roadNameInput)
        val saveRoadButton: Button = findViewById(R.id.saveRoadButton)

        backToMainButton.setOnClickListener {
            finish()
        }

        saveRoadButton.setOnClickListener {
            val roadName = roadNameInput.text.toString()
            val resultIntent = Intent().apply {
                putExtra("NEW_ROAD_NAME", roadName)
            }
            setResult(RESULT_OK, resultIntent)
            finish()
        }
    }
}
