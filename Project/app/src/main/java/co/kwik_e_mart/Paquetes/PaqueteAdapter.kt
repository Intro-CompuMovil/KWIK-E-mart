package co.kwik_e_mart.Paquetes

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import co.kwik_e_mart.R

class PaqueteAdapter(private val paquetes: MutableList<Paquetes>) : RecyclerView.Adapter<PaqueteAdapter.PaqueteViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PaqueteViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_paquete, parent, false)
        return PaqueteViewHolder(view)
    }

    override fun onBindViewHolder(holder: PaqueteViewHolder, position: Int) {
        val paquete = paquetes[position]
        holder.bind(paquete)
    }

    override fun getItemCount(): Int = paquetes.size

    fun updateData(newPaquetes: List<Paquetes>) {
        paquetes.clear()
        paquetes.addAll(newPaquetes)
        notifyDataSetChanged()
    }

    class PaqueteViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val textViewPaquete: TextView = itemView.findViewById(R.id.textViewPaquete)

        fun bind(paquete: Paquetes) {
            textViewPaquete.text = "${paquete.producto1}, ${paquete.producto2}, ${paquete.producto3}"
        }
    }
}
