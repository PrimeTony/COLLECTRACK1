package com.example.collectrack

import android.content.Intent
import android.os.Bundle
import android.widget.ListView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.collectrack.models.Collection
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class Homepage : AppCompatActivity() {
    private lateinit var bottomNavigation: BottomNavigationView
    private lateinit var usernameTextView: TextView
    private lateinit var collectionsList: ListView
    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseFirestore
    private lateinit var userId: String

    private val collectionsListData = mutableListOf<Collection>()
    private lateinit var adapter: CollectionAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_homepage)

        // Initialize Firebase components
        auth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()

        // Get current user ID
        userId = auth.currentUser?.uid ?: ""

        // Initialize UI components
        usernameTextView = findViewById(R.id.Username)
        collectionsList = findViewById(R.id.collectionList)
        bottomNavigation = findViewById(R.id.bottomNavigation)

        // Initialize the adapter and set it to the ListView
        adapter = CollectionAdapter(this, R.layout.collection_item, collectionsListData)
        collectionsList.adapter = adapter

        // Set the username to the current user's username
        db.collection("users").document(userId).get().addOnSuccessListener { document ->
            usernameTextView.text = document.data?.get("username").toString()
        }

        // Get the collections from the Firestore database
        db.collection("users").document(userId).collection("collections").get()
            .addOnSuccessListener { result ->
                for (document in result) {
                    val collection = document.toObject(Collection::class.java)
                    collectionsListData.add(collection)
                }
                adapter.notifyDataSetChanged()
            }
            .addOnFailureListener { exception ->
                // Handle any errors that occur when fetching data
            }

        // Set the bottom navigation view to be selected
        bottomNavigation.selectedItemId = R.id.Home

        // Force the bottom navigation bar to always show icon and title
        bottomNavigation.labelVisibilityMode = BottomNavigationView.LABEL_VISIBILITY_LABELED

        // Set the bottom navigation item selected listener
        bottomNavigation.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.Home -> true
                R.id.collections -> {
                    startActivity(Intent(this, Collections::class.java))
                    true
                }
                R.id.profile -> {
                    startActivity(Intent(this, Profile::class.java))
                    true
                }
                else -> false
            }
        }
    }
}
