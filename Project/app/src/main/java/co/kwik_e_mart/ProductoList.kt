package co.kwik_e_mart

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class ProductosList : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var categorySpinner: Spinner
    private lateinit var searchView: SearchView
    private lateinit var dataManager: DataManager
    private lateinit var productAdapter: ProductAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        recyclerView = findViewById(R.id.productRecyclerView)
        categorySpinner = findViewById(R.id.categorySpinner)
        dataManager = DataManager(this)

        setupCategorySpinner()
        loadProducts(dataManager.cargarProductos())
    }

    private fun setupCategorySpinner() {
        val categories = resources.getStringArray(R.array.Categorias)
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, categories)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        categorySpinner.adapter = adapter

        categorySpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                val category = categories[position]
                val filteredProducts = if (category == "Todos") {
                    dataManager.cargarProductos()
                } else {
                    dataManager.cargarProductosSeleccionado(category)
                }
                loadProducts(filteredProducts)
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }
    }

    private fun loadProducts(products: List<Productos>) {
        productAdapter = ProductAdapter(products, dataManager, carritoList = dataManager.cargarListaCompra())
        recyclerView.apply {
            layoutManager = LinearLayoutManager(this@ProductosList)
            adapter = productAdapter
        }
    }
}