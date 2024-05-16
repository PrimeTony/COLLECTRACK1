package com.example.collectrack

import android.content.Intent
import android.os.Bundle
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.example.collectrack.models.Collection

class Collections : AppCompatActivity() {
    private lateinit var bottomNavigation: BottomNavigationView
    private lateinit var btnAddCollection: FloatingActionButton
    private lateinit var collectionsList: ListView
    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseFirestore
    private lateinit var userId: String // Assuming you have the user ID

    private val CollectionsList = mutableListOf<Collection>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_collections)

        // Initialize views
        bottomNavigation = findViewById(R.id.bottomNavigation)
        btnAddCollection = findViewById(R.id.fab)
        collectionsList = findViewById(R.id.collectionList)

        // Initialize Firebase components
        auth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()

        // Get current user ID
        userId = auth.currentUser?.uid ?: ""

        // Set bottom navigation menu item selected
        bottomNavigation.selectedItemId = R.id.collections

        // Force the bottom navigation bar to always show icon and title
        bottomNavigation.labelVisibilityMode = BottomNavigationView.LABEL_VISIBILITY_LABELED

        // Set the bottom navigation view to be selected
        bottomNavigation.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.Home -> {
                    startActivity(Intent(this, Homepage::class.java))
                    true
                }
                R.id.collections -> {
                    true
                }
                R.id.profile -> {
                    startActivity(Intent(this, Profile::class.java))
                    true
                }
                else -> false
            }
        }

        // Set the onClickListener for the add collection button
        btnAddCollection.setOnClickListener {
            startActivity(Intent(this, AddCollection::class.java))
        }

        // List view adapter to display the collections list items from the Firestore database
        val adapter = CollectionAdapter(this, R.layout.collection_item, CollectionsList)
        collectionsList.adapter = adapter

        // Get the categories from the Firestore database
        db.collection("users").document(userId).collection("collections").get()
            .addOnSuccessListener { result ->
                for (document in result) {
                    val collection = document.toObject(Collection::class.java)
                    CollectionsList.add(collection)
                }
                adapter.notifyDataSetChanged()
            }
            .addOnFailureListener { exception ->
                // Handle any errors that occur when fetching data
            }


    }
}
