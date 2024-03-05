package co.kwik_e_mart.Gerente

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ListView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import co.kwik_e_mart.DataManager.DataManager
import co.kwik_e_mart.R

class ListaCompra: AppCompatActivity() {

    private lateinit var dataManager: DataManager
    private lateinit var listView: ListView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_listacompra)

        dataManager = DataManager(this)
        listView = findViewById(R.id.listViewCarrito)
        //Obtener la lista de productosCarrito
        val carritoCompra = dataManager.cargarListaCompra().toMutableList()
        // Generar el adaptador
        val adapter = CarritoAdapter(this, carritoCompra, dataManager)
        listView.adapter = adapter
    }
}

class CarritoAdapter(context: Context, private var carritos: MutableList<Productos>, private val dataManager: DataManager) : ArrayAdapter<Productos>(context, 0, carritos) {
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var listaCompraView = convertView
        if (listaCompraView == null) {
            listaCompraView = LayoutInflater.from(context).inflate(R.layout.item_carrito, parent, false)
        }

        val carrito = carritos[position]

        // Obtener referencias a las vistas de item_carrito.xml
        val nombreTextView = listaCompraView!!.findViewById<TextView>(R.id.nombreTextView)
        val precioTextView = listaCompraView.findViewById<TextView>(R.id.precioTextView)
        val categoriaTextView = listaCompraView.findViewById<TextView>(R.id.categoriaTextView)

        //Establecer los datos del carrito en las vistas
        nombreTextView.text = carrito.nombre
        precioTextView.text = "Precio: ${carrito.precio} USD"
        categoriaTextView.text = "Categoria: ${carrito.categoria}"

        val btnEliminar = listaCompraView.findViewById<Button>(R.id.btnEliminar)
        val btnFotografia = listaCompraView.findViewById<Button>(R.id.btnFotografia)

        // Configurar el clic del boton eliminar
        btnEliminar.setOnClickListener {
            // Remover el elemento del carrito
            carritos.removeAt(position)
            // Guardar el cambio para el producto específico en lugar de toda la lista
            dataManager.guardarProductoCarrito(carritos)
            // Notificar al adaptador sobre el cambio en los datos
            notifyDataSetChanged()
        }

        btnFotografia.setOnClickListener {
            val intent = Intent(context, CamaraPermisos::class.java)
            context.startActivity(intent)
        }
        return listaCompraView
    }
}
