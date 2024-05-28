package co.kwik_e_mart.Gerente

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import co.kwik_e_mart.DataManager.DataManager
import co.kwik_e_mart.Productos.Product
import co.kwik_e_mart.Productos.ProductAdapter
import co.kwik_e_mart.databinding.ActivityListaProductosGerenteBinding
import com.google.firebase.auth.FirebaseAuth

class ListaProductosGerente : AppCompatActivity() {

    private lateinit var binding: ActivityListaProductosGerenteBinding
    private lateinit var productAdapter: ProductAdapter
    private lateinit var gerenteId: String
    private val dataManager = DataManager()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityListaProductosGerenteBinding.inflate(layoutInflater)
        setContentView(binding.root)

        gerenteId = FirebaseAuth.getInstance().currentUser?.uid ?: ""

        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        productAdapter = ProductAdapter(mutableListOf()) { product ->
            deleteProduct(product)
        }
        binding.recyclerView.adapter = productAdapter

        loadProducts()

        binding.fabAddProduct.setOnClickListener {
            binding.addProductForm.visibility = View.VISIBLE
        }

        binding.btnCancel.setOnClickListener {
            binding.addProductForm.visibility = View.GONE
        }

        binding.btnAddProduct.setOnClickListener {
            val name = binding.editTextProductName.text.toString()
            val stock = binding.editTextProductStock.text.toString().toIntOrNull() ?: 0
            val price = binding.editTextProductPrice.text.toString().toDoubleOrNull() ?: 0.0
            val shippingCost = binding.editTextProductShippingCost.text.toString().toDoubleOrNull() ?: 0.0

            if (name.isNotEmpty()) {
                addProduct(Product(name = name, stock = stock, price = price, shippingCost = shippingCost, gerenteId = gerenteId))
            } else {
                Toast.makeText(this, "Please enter a product name", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun loadProducts() {
        dataManager.getProductsByGerente(gerenteId, { products ->
            Log.d("DataManager", "Retrieved products: $products")
            productAdapter.updateData(products)
        }, { error ->
            Log.e("ListaProductosGerente", "Error loading products", error)
        })
    }

    private fun addProduct(product: Product) {
        dataManager.addProduct(product, {
            Toast.makeText(this, "Product added successfully", Toast.LENGTH_SHORT).show()
            binding.addProductForm.visibility = View.GONE
            loadProducts() // Recargar productos después de agregar
        }, { error ->
            Toast.makeText(this, "Failed to add product: ${error.message}", Toast.LENGTH_SHORT).show()
        })
    }

    private fun deleteProduct(product: Product) {
        dataManager.deleteProduct(product.id, {
            Toast.makeText(this, "Product deleted successfully", Toast.LENGTH_SHORT).show()
            loadProducts() // Recargar productos después de eliminar
        }, { error ->
            Toast.makeText(this, "Failed to delete product: ${error.message}", Toast.LENGTH_SHORT).show()
        })
    }
}
