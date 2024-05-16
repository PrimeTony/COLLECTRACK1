package com.example.collectrack

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.collectrack.models.Category
import com.example.collectrack.models.Goal
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import android.widget.ArrayAdapter


class Goals : AppCompatActivity() {
    private lateinit var backArrow: ImageButton
    private lateinit var setGoal: EditText
    private lateinit var btnAddCategory: Button
    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseFirestore
    private lateinit var userId: String // Assuming you have the user ID

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_goals)

        // Initialize the views
        backArrow = findViewById(R.id.back)
        setGoal = findViewById(R.id.goal)
        btnAddCategory = findViewById(R.id.addbtn)

        // Initialize Firebase components
        auth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()

        // Get current user ID
        userId = auth.currentUser?.uid ?: ""

        // Set the onClickListener for the back arrow
        backArrow.setOnClickListener {
            onBackPressed()
        }


        // Set the onClickListener for adding goals the Firestore database using the Goal model
        btnAddCategory.setOnClickListener {

            val goal = setGoal.text.toString().trim()
            if (goal.isNotEmpty()) {
                val category = Goal(goal.toInt())

                // Reference the user document and create the "goals" subcollection under it
                db.collection("users").document(userId).collection("goals").add(category)
                    .addOnSuccessListener {
                        setGoal.text.clear()
                    }
                    .addOnFailureListener { e ->
                        // Handle any errors that occur during the write operation
                        // For now, toast the error
                        Toast.makeText(this, "Failed to add goal: ${e.message}", Toast.LENGTH_SHORT).show()
                    }
            } else {
                Toast.makeText(this, "Please fill out the field", Toast.LENGTH_SHORT).show()
            }

        }
    }
}
