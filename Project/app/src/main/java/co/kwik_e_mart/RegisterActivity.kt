package co.kwik_e_mart

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import co.kwik_e_mart.Gerente.GerenteInicio
import co.kwik_e_mart.Domiciliario.DomiciliarioInicio
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.FirebaseFirestore

class RegisterActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseFirestore
    private lateinit var rtdb: FirebaseDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        // Initialize Firebase
        auth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()
        rtdb = FirebaseDatabase.getInstance()

        val usernameEditText = findViewById<EditText>(R.id.editTextRegisterUsername)
        val emailEditText = findViewById<EditText>(R.id.editTextRegisterEmail)
        val passwordEditText = findViewById<EditText>(R.id.editTextRegisterPassword)
        val confirmPasswordEditText = findViewById<EditText>(R.id.editTextConfirmPassword)
        val userTypeSpinner = findViewById<Spinner>(R.id.spinnerUserType)
        val registerButton = findViewById<Button>(R.id.buttonRegisterUser)

        registerButton.setOnClickListener {
            val username = usernameEditText.text.toString()
            val email = emailEditText.text.toString()
            val password = passwordEditText.text.toString()
            val confirmPassword = confirmPasswordEditText.text.toString()
            val userType = userTypeSpinner.selectedItem.toString()

            if (password == confirmPassword) {
                registerUser(username, email, password, userType)
            } else {
                Toast.makeText(this, "Passwords do not match", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun registerUser(username: String, email: String, password: String, userType: String) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    val user = auth.currentUser
                    val userId = user?.uid

                    // Guardar información adicional en Firestore
                    val userMap = hashMapOf(
                        "username" to username,
                        "email" to email,
                        "userType" to userType
                    )

                    if (userId != null) {
                        // Guardar información adicional en Firestore
                        db.collection("users").document(userId).set(userMap)
                            .addOnSuccessListener {
                                Log.d("Register", "DocumentSnapshot successfully written in Firestore!")
                            }
                            .addOnFailureListener { e ->
                                Log.w("Register", "Error writing document in Firestore", e)
                            }

                        // Guardar información adicional en Realtime Database
                        rtdb.getReference("users").child(userId).setValue(userMap)
                            .addOnSuccessListener {
                                Log.d("Register", "DocumentSnapshot successfully written in Realtime Database!")
                                Toast.makeText(this, "Registration successful!", Toast.LENGTH_SHORT).show()
                                navigateToHome(userType)
                            }
                            .addOnFailureListener { e ->
                                Log.w("Register", "Error writing document in Realtime Database", e)
                                Toast.makeText(this, "Registration failed.", Toast.LENGTH_SHORT).show()
                            }
                    }
                } else {
                    Log.w("Register", "createUserWithEmail:failure", task.exception)
                    Toast.makeText(baseContext, "Registration failed.", Toast.LENGTH_SHORT).show()
                }
            }
    }

    private fun navigateToHome(userType: String) {
        when (userType) {
            "Gerente" -> {
                val intent = Intent(this, GerenteInicio::class.java)
                startActivity(intent)
            }
            "Domiciliario" -> {
                val intent = Intent(this, DomiciliarioInicio::class.java)
                startActivity(intent)
            }
            else -> {
                Toast.makeText(this, "Unknown user type", Toast.LENGTH_SHORT).show()
            }
        }
        // Navigate back to the main activity
        val mainIntent = Intent(this, MainActivity::class.java)
        mainIntent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(mainIntent)
        finish()
    }
}
