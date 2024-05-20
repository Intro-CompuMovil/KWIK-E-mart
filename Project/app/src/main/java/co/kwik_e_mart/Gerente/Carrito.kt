package co.kwik_e_mart.Gerente

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import co.kwik_e_mart.Productos.Productos
import co.kwik_e_mart.R

class Carrito : AppCompatActivity() {

    private lateinit var carritoAdapter: CarritoAdapter
    private val productosCargador = mutableListOf<Productos>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_listacompra)

        // Obtener los datos del intent
        val id = intent.getIntExtra("id", -1)
        val nombre = intent.getStringExtra("nombre")
        val precio = intent.getIntExtra("precio", 0)
        val categoria = intent.getStringExtra("categoria")

        // Si hay datos del producto, agregarlo a la lista
        if (id != -1 && nombre != null && categoria != null) {
            val nuevoProducto = Productos(id, nombre, precio, categoria)
            productosCargador.add(nuevoProducto)
        }

        val recyclerView = findViewById<RecyclerView>(R.id.listViewCarrito)
        recyclerView.layoutManager = LinearLayoutManager(this)

        carritoAdapter = CarritoAdapter(productosCargador)
        recyclerView.adapter = carritoAdapter
    }
}
