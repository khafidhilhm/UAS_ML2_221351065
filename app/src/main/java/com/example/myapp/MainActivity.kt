package com.example.myapp

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        findViewById<Button>(R.id.btn_about).setOnClickListener {
            startActivity(Intent(this, AboutActivity::class.java))
        }

        findViewById<Button>(R.id.btn_feature).setOnClickListener {
            startActivity(Intent(this, FeatureActivity::class.java))
        }

        findViewById<Button>(R.id.btn_architecture).setOnClickListener {
            startActivity(Intent(this, ArchitectureActivity::class.java))
        }

        findViewById<Button>(R.id.btn_simulation).setOnClickListener {
            startActivity(Intent(this, SimulationActivity::class.java))
        }
    }
}
