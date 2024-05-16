package com.example.collectrack

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity



class MainActivity : AppCompatActivity() {
    private lateinit var image: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Set up the image view
        image = findViewById(R.id.image)

        // Set up the handler to delay the start of the login activity
        val looper = Looper.getMainLooper()

        val handler = Handler(looper)
        handler.postDelayed({
            startActivity(Intent(this, Login::class.java))
            finish()
        }, 3000)
    }
}