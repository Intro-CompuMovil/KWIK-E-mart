package co.kwik_e_mart

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import co.kwik_e_mart.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var dataManager: DataManager
    private lateinit var categorySpinner: Spinner

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        dataManager = DataManager(this)
        categorySpinner = findViewById(R.id.categorySpinner)

        // Cargar el arreglo de categoria desde los recursos
        val categories = resources.getStringArray(R.array.Categorias)

        // Configurar el adaptador con el arreglo de categorias
        val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, categories)
        adapter.setDropDownViewResource(android.R.layout.simple_list_item_1)
        categorySpinner.adapter = adapter

        // Establecer el escuchador de seleccion del spinner
        categorySpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                // Aquí puedes actualizar tu RecyclerView basado en la selección categories[position]
                Toast.makeText(this@MainActivity, "Seleccionado: ${categories[position]}", Toast.LENGTH_SHORT).show()
                // Llama a una función para actualizar el RecyclerView
                updateRecyclerViewBasedOnCategory(categories[position])
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                // Manejar el caso en el que no se selecciona nada en el spinner
            }
        }

        // Configurar SearchView
        setupProfileButton()
        setupCartButton()
    }

    private fun setupProfileButton() {
        binding.btnPerfil.setOnClickListener {
            // Implementa la lógica para el botón de perfil aquí
            Toast.makeText(this, "Perfil", Toast.LENGTH_SHORT).show()
        }
    }

    private fun setupCartButton() {
        binding.btnCarrito.setOnClickListener {
            //Implementacion de la logica del carrito
            startActivity(Intent(this, ListaCompra::class.java))
        }
    }

    private fun updateRecyclerViewBasedOnCategory(category: String) {
        val filteredProducts = if (category == "Todos") {
            // Si la categoría es "Todos", mostrar todos los productos
            dataManager.cargarProductos()
        } else {
            // De lo contrario, filtrar los productos por categoría
            dataManager.cargarProductosSeleccionado(category)
        }

        // Suponiendo que tienes un RecyclerView llamado productsRecyclerView y un adaptador
        val recyclerView = findViewById<RecyclerView>(R.id.productRecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)
        // Suponiendo que tienes un adaptador que toma una lista de productos como parámetro
        val adapter = ProductAdapter(filteredProducts.toMutableList())
        recyclerView.adapter = adapter
    }
}

