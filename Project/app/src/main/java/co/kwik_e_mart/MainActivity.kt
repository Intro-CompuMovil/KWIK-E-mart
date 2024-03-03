package co.kwik_e_mart

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.Button
import android.widget.Spinner


class MainActivity : AppCompatActivity() {
    private lateinit var dataManager: DataManager
    private lateinit var spinner: Spinner
    private val listaCompra = mutableListOf<Productos>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        dataManager = DataManager(this)
        spinner = findViewById(R.id.spinner)
        // Cargar productos agregados
        listaCompra.addAll(dataManager.cargarListaCompra())

        // Exlorar productos
        val btnExplorar = findViewById<Button>(R.id.buttonExplorar)
        btnExplorar.setOnClickListener {
            //Obtener el valor del spinner
            val catergoria = spinner.selectedItem.toString()
            //Crear el intent
            val intent = Intent(this, ProductoList::class.java)
            intent.putExtra("categoria", catergoria)
            // Iniciar el intent
            startActivity(intent)
        }

        //Boton para ver la lista de compra
        val btnListaCompra = findViewById<Button>(R.id.buttonListaCompra)
        btnListaCompra.setOnClickListener {
            //Abrir la lista de favoritos
            val intent = Intent(this, ListaCompra::class.java)
            startActivity(intent)
        }
    }
}

