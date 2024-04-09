package co.kwik_e_mart.Gerente

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import co.kwik_e_mart.DataManager.DataManager
import co.kwik_e_mart.R

class ProductAdapter(private val productList: List<Productos>,
                     private var dataManager: DataManager,
                     private val carritoList: List<Productos>) :

    RecyclerView.Adapter<ProductAdapter.ProductViewHolder>() {

    // ViewHolder para contener las vistas de cada elemento de la lista
    class ProductViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val nombreTextView: TextView = itemView.findViewById(R.id.nombreTextView)
        val precioTextView: TextView = itemView.findViewById(R.id.precioTextView)
        val categoriaTextView: TextView = itemView.findViewById(R.id.categoriaTextView)
        val btnAgregarCarrito: Button = itemView.findViewById(R.id.btnAgregarCarrito)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_product, parent, false)
        return ProductViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        val currentItem = productList[position]

        // Asignar los valores del producto a las vistas correspondientes
        holder.nombreTextView.text = currentItem.nombre
        holder.precioTextView.text = "Precio: ${currentItem.precio} USD"
        holder.categoriaTextView.text = "Categoría: ${currentItem.categoria}"

        // Verificar si el producto actual está en el carrito
        val isInCart = carritoList.contains(currentItem)

        // Configurar el clic del botón "Agregar al carrito"
        holder.btnAgregarCarrito.apply {
            // Desactivar el botón si el producto está en el carrito
            isEnabled = !isInCart

            setOnClickListener {
                // Agregar el producto al carrito usando dataManager
                dataManager.guardarProductoCar(currentItem)

                // Desactivar el botón después de agregar al carrito
                isEnabled = false

                Toast.makeText(it.context, "Producto ${currentItem.nombre} agregado al carrito", Toast.LENGTH_SHORT).show()
            }
        }
    }
    override fun getItemCount() = productList.size
}