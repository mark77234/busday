package com.example.busforblind

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity

class FavoritesActivity : AppCompatActivity() {

    private val REQUEST_CODE_NEW_ROAD = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_favorites)

        val backToMainButton: ImageButton = findViewById(R.id.backToMain)
        val startInputButton: Button = findViewById(R.id.start_input)

        backToMainButton.setOnClickListener {
            finish()
        }

        startInputButton.setOnClickListener {
            val intent = Intent(this, NewRoadActivity::class.java)
            startActivityForResult(intent, REQUEST_CODE_NEW_ROAD)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE_NEW_ROAD && resultCode == Activity.RESULT_OK) {
            val newRoadName = data?.getStringExtra("NEW_ROAD_NAME")
            if (newRoadName != null) {
                updateButtonWithNewRoad(newRoadName)
            }
        }
    }

    private fun updateButtonWithNewRoad(roadName: String) {
        val fav1: Button = findViewById(R.id.fav_1)
        val fav2: Button = findViewById(R.id.fav_2)
        val fav3: Button = findViewById(R.id.fav_3)
        val fav4: Button = findViewById(R.id.fav_4)

        if (fav1.visibility == Button.INVISIBLE) {
            fav1.text = roadName
            fav1.visibility = Button.VISIBLE
        } else if (fav2.visibility == Button.INVISIBLE) {
            fav2.text = roadName
            fav2.visibility = Button.VISIBLE
        } else if (fav3.visibility == Button.INVISIBLE) {
            fav3.text = roadName
            fav3.visibility = Button.VISIBLE
        } else if (fav4.visibility == Button.INVISIBLE) {
            fav4.text = roadName
            fav4.visibility = Button.VISIBLE
        } else {

        }
    }
}
