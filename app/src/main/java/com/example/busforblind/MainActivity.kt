package com.example.busforblind

import android.animation.ObjectAnimator
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        overridePendingTransition(R.anim.slide_in, R.anim.slide_out)

        val select1Button: Button = findViewById(R.id.select1)
        val select2Button: Button = findViewById(R.id.select2)
        val select3Button: Button = findViewById(R.id.select3)
        val select4Button: Button = findViewById(R.id.select4)

        val textView = findViewById<TextView>(R.id.textView)

        textView.translationX = -1000f

        val animator = ObjectAnimator.ofFloat(textView, "translationX", 0f)
        animator.duration = 1000  // 애니메이션 지속 시간 설정 (1초)
        animator.start()

        select1Button.setOnClickListener {
            val intent = Intent(this, FavoritesActivity::class.java)
            startActivity(intent)
        }

        select2Button.setOnClickListener {
            val intent = Intent(this, RouteActivity::class.java)
            startActivity(intent)
        }

        select3Button.setOnClickListener {
            val intent = Intent(this, BusStopActivity::class.java)
            startActivity(intent)
        }

        select4Button.setOnClickListener {
            val intent = Intent(this, SettingActivity::class.java)
            startActivity(intent)
        }

    }
}