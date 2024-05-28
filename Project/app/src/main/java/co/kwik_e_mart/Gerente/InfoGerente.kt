package co.kwik_e_mart.Gerente

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import co.kwik_e_mart.R
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage

class InfoGerente : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var rtdb: FirebaseDatabase
    private lateinit var storage: FirebaseStorage

    private lateinit var profileImageView: ImageView
    private lateinit var nameTextView: TextView
    private lateinit var statusTextView: TextView
    private lateinit var editProfileButton: Button

    private var selectedImageUri: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_info_gerente)

        // Initialize Firebase
        auth = FirebaseAuth.getInstance()
        rtdb = FirebaseDatabase.getInstance()
        storage = FirebaseStorage.getInstance()

        profileImageView = findViewById(R.id.imageViewPfp)
        nameTextView = findViewById(R.id.textViewNameCourier)
        statusTextView = findViewById(R.id.textViewStatus)
        editProfileButton = findViewById(R.id.buttonEditProfile)

        // Load gerente information
        loadGerenteInfo()

        // Set up button listener
        editProfileButton.setOnClickListener {
            selectImage()
        }
    }

    private fun loadGerenteInfo() {
        val userId = auth.currentUser?.uid ?: return
        rtdb.getReference("users").child(userId).get().addOnSuccessListener {
            val username = it.child("username").value as? String
            val profileImageUrl = it.child("profileImageUrl").value as? String

            nameTextView.text = username ?: "Admin"
            statusTextView.text = "Online"

            if (profileImageUrl != null) {
                Glide.with(this).load(profileImageUrl).into(profileImageView)
            }
        }.addOnFailureListener {
            Toast.makeText(this, "Failed to load user info", Toast.LENGTH_SHORT).show()
        }
    }

    private fun selectImage() {
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.type = "image/*"
        startActivityForResult(intent, 1)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 1 && resultCode == Activity.RESULT_OK) {
            selectedImageUri = data?.data
            profileImageView.setImageURI(selectedImageUri)

            // Upload the image to Firebase Storage
            val userId = auth.currentUser?.uid ?: return
            val imageRef = storage.reference.child("profileImages").child(userId)
            imageRef.putFile(selectedImageUri!!).addOnSuccessListener {
                imageRef.downloadUrl.addOnSuccessListener { uri ->
                    val profileImageUrl = uri.toString()
                    updateProfileImageUrl(userId, profileImageUrl)
                }
            }.addOnFailureListener {
                Toast.makeText(this, "Failed to upload image", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun updateProfileImageUrl(userId: String, profileImageUrl: String) {
        rtdb.getReference("users").child(userId).child("profileImageUrl").setValue(profileImageUrl)
            .addOnSuccessListener {
                Toast.makeText(this, "Profile image updated", Toast.LENGTH_SHORT).show()
            }.addOnFailureListener {
                Toast.makeText(this, "Failed to update profile image", Toast.LENGTH_SHORT).show()
            }
    }
}
