package com.example.myapp

import android.os.Bundle
import android.view.animation.AnimationUtils
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity

class ArchitectureActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_architecture)

        // Animasi slide masuk
        val layout = findViewById<LinearLayout>(R.id.layout_architecture)
        val anim = AnimationUtils.loadAnimation(this, R.anim.slide_up)
        layout.startAnimation(anim)
    }
}
