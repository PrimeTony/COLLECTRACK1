package com.example.collectrack

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class Profile : AppCompatActivity() {
    private lateinit var bottomNavigation: BottomNavigationView
    private lateinit var Categorybtn: Button
    private lateinit var Goalsbtn: Button
    private lateinit var logoutbtn: Button
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        // Initialize
        bottomNavigation = findViewById(R.id.bottomNavigation)
        Categorybtn = findViewById(R.id.categoryButton)
        Goalsbtn = findViewById(R.id.goalButton)
        logoutbtn = findViewById(R.id.logoutButton)

        // Initialize Firebase
        auth = FirebaseAuth.getInstance()

        //Set bottom navigation to selected
        bottomNavigation.selectedItemId = R.id.profile

        // force the bottom navigation bar to always show icon and title
        bottomNavigation.labelVisibilityMode = BottomNavigationView.LABEL_VISIBILITY_LABELED

        // Set the bottom navigation view to be selected
        bottomNavigation.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.Home -> {
                    startActivity(Intent(this, Homepage::class.java))
                    true
                }
                R.id.collections -> {
                    startActivity(Intent(this, Collections::class.java))
                    true
                }

                R.id.profile -> {
                    true
                }
            }
            false
        }

        // Set the onClickListener for the category button
        Categorybtn.setOnClickListener {
            startActivity(Intent(this, Categories::class.java))
        }

        // Set the onClickListener for the goals button
        Goalsbtn.setOnClickListener {
            startActivity(Intent(this, Goals::class.java))
        }

        // Set the onClickListener for the logout button
        logoutbtn.setOnClickListener {
            // Initialize Firebase
            auth.signOut()

            // Notify the user and start the login activity
            Toast.makeText(this, "Logged out successfully", Toast.LENGTH_LONG).show()
            startActivity(Intent(this, Login::class.java))
        }



    }
}