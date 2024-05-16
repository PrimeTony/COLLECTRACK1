package com.example.collectrack

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore


class Register : AppCompatActivity() {

    private lateinit var username: EditText
    private lateinit var email: EditText
    private lateinit var password: EditText
    private lateinit var btnRegister: Button
    private lateinit var btnLogin: Button
    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseFirestore


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        // Initialize the views
        username = findViewById(R.id.editTextUsername)
        email = findViewById(R.id.editTextEmail)
        password = findViewById(R.id.editTextPassword)
        btnRegister = findViewById(R.id.button_register)
        btnLogin = findViewById(R.id.button_login)

        // Initialize Firebase Auth and Firestore
        auth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()

        // Set up the register button
        btnRegister.setOnClickListener {
            val usernameText = username.text.toString()
            val emailText = email.text.toString()
            val passwordText = password.text.toString()

            if (usernameText.isEmpty() || emailText.isEmpty() || passwordText.isEmpty()) {
                Toast.makeText(this, "Please fill out all fields", Toast.LENGTH_SHORT).show()
            } else {
                auth.createUserWithEmailAndPassword(emailText, passwordText)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            val user = auth.currentUser

                            user?.let {
                                val userData = hashMapOf(
                                    "username" to usernameText,
                                    "email" to emailText,
                                    "password" to passwordText
                                )

                                db.collection("users")
                                    .document(it.uid)
                                    .set(userData)
                                    .addOnSuccessListener {
                                        Toast.makeText(this, "Registration successful", Toast.LENGTH_SHORT).show()
                                        startActivity(Intent(this, Login::class.java))
                                    }
                                    .addOnFailureListener {
                                        Toast.makeText(this, "Registration failed", Toast.LENGTH_SHORT).show()
                                    }
                            }
                        } else {
                            Toast.makeText(this, "Registration failed", Toast.LENGTH_SHORT).show()
                        }
                    }
            }
        }

        // Set up the login button
        btnLogin.setOnClickListener {
            startActivity(Intent(this, Login::class.java))
        }
    }


}