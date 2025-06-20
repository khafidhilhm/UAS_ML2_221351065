package com.example.myapp

import android.os.Bundle
import android.view.animation.AnimationUtils
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity

class FeatureActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_feature)

        val layout = findViewById<LinearLayout>(R.id.feature_list)
        val animation = AnimationUtils.loadAnimation(this, R.anim.slide_up)
        layout.startAnimation(animation)
    }
}
