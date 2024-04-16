package co.kwik_e_mart

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import co.kwik_e_mart.Domiciliario.DomiciliarioInicio
import co.kwik_e_mart.User.UserInicio

class MainActivity : AppCompatActivity(){

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val usernameEditText = findViewById<EditText>(R.id.editTextTextUsername)
        val loginButton = findViewById<Button>(R.id.buttonLogin)



        loginButton.setOnClickListener {
            val usernameContent = usernameEditText.text.toString()
            Log.d("Informacion", "String en Username: $usernameContent")
            if ( usernameContent == "Admin") {
                val intent = Intent(this, co.kwik_e_mart.Gerente.GerenteInicio::class.java)
                startActivity(intent)
            }

            if ( usernameContent == "User") {
                val intent = Intent(this, UserInicio::class.java)
                startActivity(intent)
            }

            if ( usernameContent == "Domiciliario") {
                val intent = Intent(this, DomiciliarioInicio::class.java)
                startActivity(intent)
            }
        }
    }
}