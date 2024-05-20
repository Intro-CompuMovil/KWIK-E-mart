package co.kwik_e_mart.Paquetes

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

class PaqueteAdapter(private val paqueteList: MutableList<Paquetes>) :
    RecyclerView.Adapter<PaqueteAdapter.PackViewHolder>() {

    // ViewHolder para contener las vistas de cada elemento de la lista
    class PackViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val producto1: TextView = itemView.findViewById(R.id.producto1)
        val producto2: TextView = itemView.findViewById(R.id.producto2)
        val producto3: TextView = itemView.findViewById(R.id.producto3)
        val producto4: TextView = itemView.findViewById(R.id.producto4)
        val producto5: TextView = itemView.findViewById(R.id.producto5)
        val btnelegir: Button = itemView.findViewById(R.id.btnElegirPaquete)
        val btnRechazar: Button = itemView.findViewById(R.id.btnRechazarPaquete)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PackViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_paquete, parent, false)
        return PackViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: PackViewHolder, position: Int) {
        val currentItem = paqueteList[position]

        // Asignar los valores del producto a las vistas correspondientes
        holder.producto1.text = currentItem.producto1
        holder.producto2.text = currentItem.producto2
        holder.producto3.text = currentItem.producto3
        holder.producto4.text = currentItem.producto4
        holder.producto5.text = currentItem.producto5

        // Configurar el clic del botón "Elegir paquete"
        holder.btnelegir.setOnClickListener {
            Toast.makeText(it.context, "Paquete Elegido", Toast.LENGTH_SHORT).show()
            val context = it.context
            val intent = Intent(context, DomiciliarioElegido::class.java).apply {
                putExtra("producto1", currentItem.producto1)
                putExtra("producto2", currentItem.producto2)
                putExtra("producto3", currentItem.producto3)
                putExtra("producto4", currentItem.producto4)
                putExtra("producto5", currentItem.producto5)
            }
            context.startActivity(intent)
        }

        holder.btnRechazar.setOnClickListener {
            paqueteList.removeAt(holder.adapterPosition)
            notifyItemRemoved(holder.adapterPosition)
            Toast.makeText(it.context, "Paquete Rechazado", Toast.LENGTH_SHORT).show()
        }
    }

    override fun getItemCount() = paqueteList.size
}
