package co.kwik_e_mart.Domiciliario

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import co.kwik_e_mart.Gerente.GerenteInicio
import co.kwik_e_mart.R
import co.kwik_e_mart.databinding.ActivityUserinicioBinding

class DomiciliarioInicio: AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_domiciliarioinicio)

        val buttListaEntregas = findViewById<ImageButton>(R.id.botonListaEntregas)

        buttListaEntregas.setOnClickListener {
            val intent = Intent(this, ListaEntregas::class.java)
            startActivity(intent)
        }
    }
}