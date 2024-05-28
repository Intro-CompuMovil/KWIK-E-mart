package co.kwik_e_mart.Entrega

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import co.kwik_e_mart.Productos.Product
import co.kwik_e_mart.R

class NewProductAdapter(private val productList: List<Product>, private val p1TextView: TextView) :

    RecyclerView.Adapter<NewProductAdapter.NewProductViewHolder>() {

    // ViewHolder para contener las vistas de cada elemento de la lista
    class NewProductViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val nombreProducto: TextView = itemView.findViewById(R.id.ProductoNameTextView)
        val precioProducto: TextView = itemView.findViewById(R.id.ProductoPrecioTextView)
        val categoriaProducto: TextView = itemView.findViewById(R.id.ProductoCategoriaTextView)
        val btnAdd: Button = itemView.findViewById(R.id.btnAdd)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewProductViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_addproduct, parent, false)
        return NewProductViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: NewProductViewHolder, position: Int) {
        val currentItem = productList[position]

        // Asignar los valores del producto a las vistas correspondientes
        holder.nombreProducto.text = currentItem.name
        holder.precioProducto.text = currentItem.price.toString()


    }
    override fun getItemCount() = productList.size
}