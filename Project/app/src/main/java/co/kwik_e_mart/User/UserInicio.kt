package co.kwik_e_mart.User

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import co.kwik_e_mart.DataManager.DataManager
import co.kwik_e_mart.Productos.ProductAdapter
import co.kwik_e_mart.R
import co.kwik_e_mart.databinding.ActivityUserinicioBinding


class UserInicio : AppCompatActivity() {

    private lateinit var binding: ActivityUserinicioBinding
    private lateinit var dataManager: DataManager
    private lateinit var categorySpinner: Spinner


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUserinicioBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val producto1 = intent.getStringExtra("producto1")
        val producto2 = intent.getStringExtra("producto2")
        val producto3 = intent.getStringExtra("producto3")
        val producto4 = intent.getStringExtra("producto4")
        val producto5 = intent.getStringExtra("producto5")

        var paqueteProductosTextView = findViewById<TextView>(R.id.carrito)


        if (producto1 == null){
            paqueteProductosTextView.text = "Paquete sin Productos"
        }

        else if (producto2 == null){
            paqueteProductosTextView.text = "$producto1"
        }

        else if (producto3 == null){
            paqueteProductosTextView.text = "$producto1 - $producto2"
        }

        else if (producto4 == null){
            paqueteProductosTextView.text = "$producto1 - $producto2 - $producto3"
        }

        else
            paqueteProductosTextView.text = "$producto1 - $producto2 - $producto3 - $producto4 - $producto5"

        dataManager = DataManager(this)
        categorySpinner = findViewById(R.id.categorySpinner)

        // Cargar el arreglo de categoría desde los recursos
        val categories = resources.getStringArray(R.array.Categorias)

        // Configurar el adaptador con el arreglo de categorías
        val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, categories)
        adapter.setDropDownViewResource(android.R.layout.simple_list_item_1)
        categorySpinner.adapter = adapter

        // Establecer el escuchador de selección del spinner
        categorySpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                // Aquí puedes actualizar tu RecyclerView basado en la selección categories[position]
                Toast.makeText(this@UserInicio, "Seleccionado: ${categories[position]}", Toast.LENGTH_SHORT).show()
                // Llama a una función para actualizar el RecyclerView
                updateRecyclerViewBasedOnCategory(categories[position])
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                // Manejar el caso en el que no se selecciona nada en el spinner
            }
        }

        // Configurar SearchView
        //setupProfileButton()
        setupGpsButton()
        setupCartButton()

    }

    /*private fun setupProfileButton() {
        binding.btnPerfil.setOnClickListener {
            // Implementa la lógica para el botón de perfil aquí
            Toast.makeText(this, "Perfil", Toast.LENGTH_SHORT).show()
        }
    }*/

    private fun setupGpsButton() {
        binding.btnPerfil.setOnClickListener {
            startActivity(Intent(this, GpS::class.java))
        }
    }

    private fun setupCartButton() {
        binding.btnCarrito.setOnClickListener {
            // Implementación de la lógica del carrito
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

        // Inicializar la lista de productos del carrito
        val carritoList = dataManager.cargarListaCompra()

        val recyclerView = findViewById<RecyclerView>(R.id.productRecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)

        // Crear el adaptador y pasar las listas filtradas y la lista del carrito
        val adapter = ProductAdapter(filteredProducts.toMutableList(), dataManager, carritoList)
        recyclerView.adapter = adapter
    }
}

