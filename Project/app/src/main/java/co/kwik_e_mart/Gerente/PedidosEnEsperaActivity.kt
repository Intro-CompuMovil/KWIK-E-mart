package co.kwik_e_mart

import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import co.kwik_e_mart.Pedidos.Pedido
import co.kwik_e_mart.Pedidos.PedidoAdapter
import co.kwik_e_mart.Pedidos.Ubicacion
import co.kwik_e_mart.utils.generateRandomLocation
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import kotlin.random.Random

class PedidosEnEsperaActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var rtdb: FirebaseDatabase
    private lateinit var recyclerView: RecyclerView
    private lateinit var pedidoAdapter: PedidoAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pedidos_en_espera)

        // Initialize Firebase
        auth = FirebaseAuth.getInstance()
        rtdb = FirebaseDatabase.getInstance()

        recyclerView = findViewById(R.id.recyclerViewPedidosEnEspera)
        recyclerView.layoutManager = LinearLayoutManager(this)

        pedidoAdapter = PedidoAdapter(mutableListOf()) { pedido ->
            // Handle the assign action
        }
        recyclerView.adapter = pedidoAdapter

        loadPedidosEnEspera()

        val generateButton = findViewById<Button>(R.id.buttonGeneratePedidosEnEspera)
        generateButton.setOnClickListener {
            generatePedidosEnEspera()
        }
    }

    private fun loadPedidosEnEspera() {
        val pedidosRef = rtdb.getReference("pedidosEnEspera")
        pedidosRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val pedidos = mutableListOf<Pedido>()
                for (pedidoSnapshot in snapshot.children) {
                    val pedido = pedidoSnapshot.getValue(Pedido::class.java)
                    if (pedido != null) {
                        pedidos.add(pedido)
                    }
                }
                pedidoAdapter.updateData(pedidos)
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@PedidosEnEsperaActivity, "Error al cargar pedidos en espera", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun loadProductos(gerenteId: String, onComplete: (Map<String, Producto>) -> Unit) {
        val productosRef = rtdb.getReference("productos")
        productosRef.orderByChild("gerenteId").equalTo(gerenteId).addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val productos = mutableMapOf<String, Producto>()
                for (productoSnapshot in snapshot.children) {
                    val producto = productoSnapshot.getValue(Producto::class.java)
                    if (producto != null) {
                        productos[productoSnapshot.key!!] = producto
                    }
                }
                onComplete(productos)
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@PedidosEnEsperaActivity, "Error al cargar productos", Toast.LENGTH_SHORT).show()
            }
        })
    }

    data class Producto(
        val id: String = "",
        val nombre: String = "",
        val precio: Double = 0.0,
        val shippingCost: Double = 0.0,
        val stock: Int = 0,
        val gerenteId: String = ""
    )


    private fun generatePedidosEnEspera() {
        val userId = auth.currentUser?.uid ?: return
        val gerenteLat = 37.7749 // Latitud del gerente
        val gerenteLon = -122.4194 // Longitud del gerente

        loadProductos(userId) { productos ->
            if (productos.isNotEmpty()) {
                val productosPedido = mutableMapOf<String, Int>()
                var total = 0.0

                for ((id, producto) in productos) {
                    val cantidadPedido = Random.nextInt(1, producto.stock + 1)
                    productosPedido[id] = cantidadPedido
                    total += cantidadPedido * producto.precio
                }

                Log.d("PedidosEnEspera", "Productos en el pedido: $productosPedido")
                Log.d("PedidosEnEspera", "Total del pedido: $total")

                val (lat, lon) = generateRandomLocation(gerenteLat, gerenteLon, 5.0)
                val pedidoId = rtdb.getReference("pedidosEnEspera").push().key ?: return@loadProductos
                val nuevoPedido = Pedido(
                    clienteId = userId,
                    productos = productosPedido,
                    total = total,
                    estado = "en espera",
                    fecha = "2024-05-27", // Aquí puedes usar la fecha actual
                    ubicacion = Ubicacion(lat, lon)
                )

                rtdb.getReference("pedidosEnEspera").child(pedidoId).setValue(nuevoPedido)
                    .addOnSuccessListener {
                        Toast.makeText(this@PedidosEnEsperaActivity, "Pedido generado", Toast.LENGTH_SHORT).show()
                        loadPedidosEnEspera()
                    }
                    .addOnFailureListener {
                        Toast.makeText(this@PedidosEnEsperaActivity, "Error al generar el pedido", Toast.LENGTH_SHORT).show()
                    }
            } else {
                Toast.makeText(this@PedidosEnEsperaActivity, "No hay productos en stock", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
