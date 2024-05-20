package co.kwik_e_mart.Gerente

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import co.kwik_e_mart.Productos.Productos
import co.kwik_e_mart.R

class CarritoAdapter(private val productList: MutableList<Productos>) :

    RecyclerView.Adapter<CarritoAdapter.NewCarritoViewHolder>() {

    // ViewHolder para contener las vistas de cada elemento de la lista
    class NewCarritoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val nombreProducto: TextView = itemView.findViewById(R.id.productoNombreCarrito)
        val precioProducto: TextView = itemView.findViewById(R.id.productoPrecioCarrito)
        val categoriaProducto: TextView = itemView.findViewById(R.id.productoCategoriaCarrito)
        val btnEliminar: Button = itemView.findViewById(R.id.btnEliminar)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewCarritoViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_carrito, parent, false)
        return NewCarritoViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: NewCarritoViewHolder, position: Int) {
        val currentItem = productList[position]

        // Asignar los valores del producto a las vistas correspondientes
        holder.nombreProducto.text = currentItem.nombre
        holder.precioProducto.text = currentItem.precio.toString()
        holder.categoriaProducto.text = currentItem.categoria

        // Configurar el clic del botón "Eliminar"
        holder.btnEliminar.setOnClickListener {
            // Eliminar el producto de la lista
            productList.removeAt(holder.adapterPosition)
            // Notificar al adaptador que el elemento ha sido eliminado
            notifyItemRemoved(holder.adapterPosition)
        }
    }

    override fun getItemCount() = productList.size
}
