package com.example.myapp

import android.os.Bundle
import android.view.animation.AnimationUtils
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView

class AboutActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_about)

        // Animasi masuk ke card
        val card = findViewById<CardView>(R.id.about_card)
        val anim = AnimationUtils.loadAnimation(this, android.R.anim.slide_in_left)
        card.startAnimation(anim)
    }
}
