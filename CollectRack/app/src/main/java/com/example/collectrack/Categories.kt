package com.example.collectrack

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ListView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.example.collectrack.models.Category

class Categories : AppCompatActivity() {
    private lateinit var backArrow: ImageButton
    private lateinit var addCategory: EditText
    private lateinit var btnAddCategory: Button
    private lateinit var categoryList: ListView
    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_categories)

        // Initialize the views
        backArrow = findViewById(R.id.back)
        addCategory = findViewById(R.id.category)
        btnAddCategory = findViewById(R.id.addbtn)
        categoryList = findViewById(R.id.categoriesList)

        // Initialize Firebase components
        auth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()

        // Set the onClickListener for the back arrow
        backArrow.setOnClickListener {
            onBackPressed()
        }

        // Set the onClickListener for the add category button and add the category to the Firestore database using the Category model
        btnAddCategory.setOnClickListener {
            val categoryName = addCategory.text.toString().trim()
            if (categoryName.isNotEmpty()) {
                val category = Category(categoryName)
                db.collection("categories").add(category)
                    .addOnSuccessListener {
                        addCategory.text.clear()
                    }
                    .addOnFailureListener { e ->
                        // Handle any errors that occur during the write operation
                        // For now, toast the error
                        Toast.makeText(this, "Please fill out the field", Toast.LENGTH_SHORT).show()

                    }
            }
        }

        // ListView will display the categories from the Firestore database
        val categories = mutableListOf<Category>()
        val adapter = CategoryAdapter(this, R.layout.category_item, categories)
        categoryList.adapter = adapter

        // Get the categories from the Firestore database
        db.collection("categories").get()
            .addOnSuccessListener { result ->
                for (document in result) {
                    val category = document.toObject(Category::class.java)
                    categories.add(category)
                }
                adapter.notifyDataSetChanged()
            }
            .addOnFailureListener { e ->
                // Handle any errors that occur during the read operation
                // For now, just log the error
                Toast.makeText(this, "Failed to fetch categories", Toast.LENGTH_SHORT).show()
            }
    }

    companion object {
        private const val TAG = "CategoriesActivity"
    }
}
