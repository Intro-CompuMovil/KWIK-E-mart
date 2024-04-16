package co.kwik_e_mart.Entrega

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import co.kwik_e_mart.Gerente.DomiciliarioElegido
import co.kwik_e_mart.R

class EntregaAdapter(private val mensajeroList: List<Entregas>) :

    RecyclerView.Adapter<EntregaAdapter.CourierViewHolder>() {

    // ViewHolder para contener las vistas de cada elemento de la lista
    class CourierViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val productosTextView: TextView = itemView.findViewById(R.id.ProductosTextView)
        val direccionTextView: TextView = itemView.findViewById(R.id.DireccionTextView)
        val btnaceptar: Button = itemView.findViewById(R.id.btnElegirTarea)
        val btnrechzar: Button = itemView.findViewById(R.id.btnRechazarTarea)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CourierViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_entrega, parent, false)
        return CourierViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: CourierViewHolder, position: Int) {
        val currentItem = mensajeroList[position]

        // Asignar los valores del producto a las vistas correspondientes
        holder.productosTextView.text = currentItem.productos
        holder.direccionTextView.text = "Direccion: ${currentItem.direccion}"

        // Configurar el clic del botón "Agregar al carrito"
        holder.btnaceptar.apply {

            setOnClickListener {
                Toast.makeText(it.context, "Productos ${currentItem.productos}", Toast.LENGTH_SHORT).show()
            }
        }
    }
    override fun getItemCount() = mensajeroList.size
}