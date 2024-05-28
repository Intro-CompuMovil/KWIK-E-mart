package co.kwik_e_mart.Pedidos

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import co.kwik_e_mart.R

data class Pedido(
    val clienteId: String = "",
    val productos: Map<String, Int> = emptyMap(),
    val total: Double = 0.0,
    val estado: String = "",
    val fecha: String = "",
    val ubicacion: Ubicacion = Ubicacion()
)

data class Ubicacion(
    val latitud: Double = 0.0,
    val longitud: Double = 0.0
)

class PedidoAdapter(
    private var pedidos: MutableList<Pedido>,
    private val onAssign: (Pedido) -> Unit
) : RecyclerView.Adapter<PedidoAdapter.PedidoViewHolder>() {

    class PedidoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val pedidoInfo: TextView = itemView.findViewById(R.id.pedidoInfo)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PedidoViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_pedido, parent, false)
        return PedidoViewHolder(view)
    }

    override fun onBindViewHolder(holder: PedidoViewHolder, position: Int) {
        val pedido = pedidos[position]
        holder.pedidoInfo.text = "Pedido de ${pedido.clienteId}: $${pedido.total} - ${pedido.estado}"
        holder.itemView.setOnClickListener { onAssign(pedido) }
    }

    override fun getItemCount(): Int = pedidos.size

    fun updateData(newPedidos: List<Pedido>) {
        pedidos.clear()
        pedidos.addAll(newPedidos)
        notifyDataSetChanged()
    }
}
