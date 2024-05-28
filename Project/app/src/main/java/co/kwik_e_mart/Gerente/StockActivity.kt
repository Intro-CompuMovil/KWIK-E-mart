package co.kwik_e_mart.Gerente

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import co.kwik_e_mart.Productos.Product
import co.kwik_e_mart.Productos.ProductAdapter
import co.kwik_e_mart.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class StockActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var rtdb: FirebaseDatabase
    private lateinit var recyclerView: RecyclerView
    private lateinit var productAdapter: ProductAdapter
    private val products = mutableListOf<Product>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_stock)

        auth = FirebaseAuth.getInstance()
        rtdb = FirebaseDatabase.getInstance()

        recyclerView = findViewById(R.id.recyclerViewStock)
        recyclerView.layoutManager = LinearLayoutManager(this)
        productAdapter = ProductAdapter(products) { product ->
            // Handle product item click if needed
        }
        recyclerView.adapter = productAdapter

        loadStock()
    }

    private fun loadStock() {
        val userId = auth.currentUser?.uid ?: return
        rtdb.getReference("productos").orderByChild("gerenteId").equalTo(userId)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    products.clear()
                    for (productSnapshot in snapshot.children) {
                        val product = productSnapshot.getValue(Product::class.java)
                        product?.let { products.add(it) }
                    }
                    productAdapter.notifyDataSetChanged()
                }

                override fun onCancelled(error: DatabaseError) {
                    Toast.makeText(this@StockActivity, "Error loading stock", Toast.LENGTH_SHORT).show()
                }
            })
    }
}
