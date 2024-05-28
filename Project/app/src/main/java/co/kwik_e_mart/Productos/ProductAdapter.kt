package co.kwik_e_mart.Productos

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import co.kwik_e_mart.R

class ProductAdapter(
    private var products: MutableList<Product>,
    private val onDelete: (Product) -> Unit
) : RecyclerView.Adapter<ProductAdapter.ProductViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_product, parent, false)
        return ProductViewHolder(view)
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        val product = products[position]
        holder.bind(product)
        holder.btnDelete.setOnClickListener {
            onDelete(product)
            products.removeAt(position)
            notifyItemRemoved(position)
        }
    }

    override fun getItemCount(): Int = products.size

    fun updateData(newProducts: List<Product>) {
        products.clear()
        products.addAll(newProducts)
        notifyDataSetChanged()
    }

    class ProductViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val productName: TextView = itemView.findViewById(R.id.productName)
        private val productStock: TextView = itemView.findViewById(R.id.productStock)
        private val productPrice: TextView = itemView.findViewById(R.id.productPrice)
        private val productShippingCost: TextView = itemView.findViewById(R.id.productShippingCost)
        val btnDelete: Button = itemView.findViewById(R.id.btnDelete)

        fun bind(product: Product) {
            productName.text = product.name
            productStock.text = "Stock: ${product.stock}"
            productPrice.text = "Price: $${product.price}"
            productShippingCost.text = "Shipping: $${product.shippingCost}"
        }
    }
}
