package co.kwik_e_mart

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class ProductAdapter(private val products: List<Productos>) : RecyclerView.Adapter<ProductAdapter.ProductViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_product, parent, false)
        return ProductViewHolder(view)
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        val product = products[position]
        holder.bind(product)
    }

    override fun getItemCount(): Int {
        return products.size
    }

    inner class ProductViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val nameTextView: TextView = itemView.findViewById(R.id.nombreTextView)
        private val priceTextView: TextView = itemView.findViewById(R.id.precioTextView)
        private val categoryTextView: TextView = itemView.findViewById(R.id.categoriaTextView)

        fun bind(product: Productos) {
            nameTextView.text = product.nombre
            priceTextView.text = "Precio: ${product.precio} USD"
            categoryTextView.text = "Categoría: ${product.categoria}"
        }
    }
}