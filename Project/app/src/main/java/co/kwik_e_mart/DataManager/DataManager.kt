package co.kwik_e_mart.DataManager

import android.util.Log
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import co.kwik_e_mart.Productos.Product
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener

class DataManager {

    private val database: DatabaseReference = FirebaseDatabase.getInstance().reference.child("productos")

    fun addProduct(product: Product, onSuccess: () -> Unit, onFailure: (Exception) -> Unit) {
        val productId = database.push().key
        if (productId != null) {
            product.id = productId
            database.child(productId).setValue(product).addOnCompleteListener {
                if (it.isSuccessful) {
                    onSuccess()
                } else {
                    it.exception?.let { e -> onFailure(e) }
                }
            }
        } else {
            onFailure(Exception("Error generating product ID"))
        }
    }

    fun updateProduct(product: Product, onSuccess: () -> Unit, onFailure: (Exception) -> Unit) {
        database.child(product.id).setValue(product).addOnCompleteListener {
            if (it.isSuccessful) {
                onSuccess()
            } else {
                it.exception?.let { e -> onFailure(e) }
            }
        }
    }

    fun deleteProduct(productId: String, onSuccess: () -> Unit, onFailure: (Exception) -> Unit) {
        database.child(productId).removeValue().addOnCompleteListener {
            if (it.isSuccessful) {
                onSuccess()
            } else {
                it.exception?.let { e -> onFailure(e) }
            }
        }
    }

    fun getProductsByGerente(gerenteId: String, onSuccess: (List<Product>) -> Unit, onFailure: (Exception) -> Unit) {
        database.orderByChild("gerenteId").equalTo(gerenteId).addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val products = mutableListOf<Product>()
                for (productSnapshot in snapshot.children) {
                    val product = productSnapshot.getValue(Product::class.java)
                    if (product != null) {
                        products.add(product)
                    }
                }
                Log.d("DataManager", "Retrieved products: $products")
                onSuccess(products)
            }

            override fun onCancelled(error: DatabaseError) {
                onFailure(error.toException())
            }
        })
    }
}
