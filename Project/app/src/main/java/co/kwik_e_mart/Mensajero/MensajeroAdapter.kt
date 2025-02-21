package co.kwik_e_mart.Mensajero

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import co.kwik_e_mart.R

class MensajeroAdapter(private val mensajeroList: List<Mensajeros>) :

    RecyclerView.Adapter<MensajeroAdapter.CourierViewHolder>() {

    // ViewHolder para contener las vistas de cada elemento de la lista
    class CourierViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val mensajeroNombreTextView: TextView = itemView.findViewById(R.id.courierNameTextView)
        val calificacionTextView: TextView = itemView.findViewById(R.id.calificacionTextView)
        val precioPromedioTextView: TextView = itemView.findViewById(R.id.preciopromedioTextView)
        val btnelegir: Button = itemView.findViewById(R.id.btnElegirMensajero)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CourierViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_mensajero, parent, false)
        return CourierViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: CourierViewHolder, position: Int) {
        val currentItem = mensajeroList[position]

        // Asignar los valores del producto a las vistas correspondientes
        holder.mensajeroNombreTextView.text = currentItem.nombre
        holder.calificacionTextView.text = "Calificacion: ${currentItem.calificacion} "
        holder.precioPromedioTextView.text = "Precio Promedio: ${currentItem.preciopromedio}"


    }
    override fun getItemCount() = mensajeroList.size
}