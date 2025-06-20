package com.example.myapp

import android.os.Bundle
import android.view.animation.AnimationUtils
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.myapp.R

class AboutActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_about)

        // Animasi slide-in untuk semua komponen teks
        val slideAnim = AnimationUtils.loadAnimation(this, android.R.anim.slide_in_left)

        val title = findViewById<TextView>(R.id.about_title)
        val sections = listOf(
            R.id.about_title,
            R.id.about_title + 1,
            R.id.about_title + 2,
            R.id.about_title + 3,
            R.id.about_title + 4
        )

        title.startAnimation(slideAnim)

        // Tambahkan animasi ke setiap TextView konten
        for (id in sections) {
            findViewById<TextView?>(id)?.startAnimation(slideAnim)
        }
    }
}
