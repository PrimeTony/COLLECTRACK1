package com.example.collectrack

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import java.io.ByteArrayOutputStream

class AddCollection : AppCompatActivity() {

    private lateinit var backarrow: ImageButton
    private lateinit var Name: EditText
    private lateinit var Description: EditText
    private lateinit var CategoryList: EditText
    private lateinit var CollectionImage: ImageView
    private lateinit var UploadButton: Button
    private lateinit var SaveButton: Button
    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseFirestore
    private lateinit var storage: FirebaseStorage
    private lateinit var userId: String // Assuming you have the user ID

    private val PICK_IMAGE_REQUEST = 1
    private val CAMERA_REQUEST_CODE = 2

    private var imageUri: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_collection)

        //Initialize views
        backarrow = findViewById(R.id.back)
        Name = findViewById(R.id.collection_name)
        Description = findViewById(R.id.description)
        CategoryList = findViewById(R.id.categories)
        CollectionImage = findViewById(R.id.imageview)
        UploadButton = findViewById(R.id.btn_uploadImage)
        SaveButton = findViewById(R.id.btn_save)

        // Initialize Firebase
        auth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()
        storage = FirebaseStorage.getInstance()

        // Get current user ID
        userId = auth.currentUser?.uid ?: ""

        // Set the onClickListener for the back arrow
        backarrow.setOnClickListener {
            onBackPressed()
        }

        // Set the onClickListener for UploadButton
        UploadButton.setOnClickListener {
            showUploadOptions()
        }

        // Set the onClickListener for SaveButton
        SaveButton.setOnClickListener {
            saveCollection()
        }
    }

    // Function to show options for uploading an image
    private fun showUploadOptions() {
        val options = arrayOf("Take Picture", "Choose from Gallery")
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Upload Image")
        builder.setItems(options) { _, which ->
            when (which) {
                0 -> takePicture()
                1 -> chooseFromGallery()
            }
        }
        builder.show()
    }

    // Function to take a picture
    private fun takePicture() {
        val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        startActivityForResult(cameraIntent, CAMERA_REQUEST_CODE)
    }

    // Function to choose from gallery
    private fun chooseFromGallery() {
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST)
    }

    // Function to handle the result of image selection/taking
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK && data != null && data.data != null) {
            // Handling gallery image selection
            imageUri = data.data
            CollectionImage.setImageURI(imageUri)
        } else if (requestCode == CAMERA_REQUEST_CODE && resultCode == Activity.RESULT_OK && data != null) {
            // Handling camera image capture
            val imageBitmap = data.extras?.get("data") as Bitmap
            imageUri = getImageUri(imageBitmap)
            CollectionImage.setImageURI(imageUri)
        }
    }

    // Function to convert Bitmap to Uri
    private fun getImageUri(inImage: Bitmap): Uri {
        val bytes = ByteArrayOutputStream()
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes)
        val path = MediaStore.Images.Media.insertImage(contentResolver, inImage, "Title", null)
        return Uri.parse(path)
    }

    // Function to save the collection data to Firestore
    private fun saveCollection() {
        val name = Name.text.toString().trim()
        val description = Description.text.toString().trim()
        val category = CategoryList.text.toString().trim()
        val imageUrl = imageUri.toString()
        val userId = auth.currentUser?.uid

        if (name.isEmpty() || description.isEmpty() || category.isEmpty() || imageUrl.isEmpty()) {
            Toast.makeText(this, "Please fill out all fields", Toast.LENGTH_SHORT).show()
            return
        }

        userId?.let { uid ->
            // Save data to Firestore
            val collectionData = hashMapOf(
                "name" to name,
                "description" to description,
                "category" to category,
                "imageUrl" to imageUrl
            )

            db.collection("users").document(userId).collection("collections")
                .add(collectionData)
                .addOnSuccessListener {
                    Toast.makeText(this, "Collection added successfully", Toast.LENGTH_SHORT).show()
                    finish() // Finish the activity after adding collection
                }
                .addOnFailureListener { e ->
                    Toast.makeText(this, "Failed to add collection: ${e.message}", Toast.LENGTH_SHORT).show()
                }
        } ?: run {
            Toast.makeText(this, "User not logged in", Toast.LENGTH_SHORT).show()
        }
    }
}
