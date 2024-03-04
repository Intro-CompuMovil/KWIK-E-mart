package co.kwik_e_mart

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity


class DetallesProducto : AppCompatActivity(){

    private val dataManager = DataManager(this)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detallesdestino)
        //Obtener el producto Seleccionado del intent
        val producto: Productos = intent.getParcelableExtra("producto") ?: return
        //Obtener los detalles del producto del DataManager
        val detallesProductos = dataManager.obtenerDetalles(producto.id) ?: return
        //Actualizar la intefaz de usuario
        actualizarInterfazUsuario(detallesProductos)
        //Configurar el boton para agregar
        val btnAgregar: Button = findViewById(R.id.Carrito)
        btnAgregar.setOnClickListener {
            //Guardar el producto en el carrito
            gurdarProuctoFavorito(detallesProductos)
            //Mostrar un toast
            Toast.makeText(this, "Producto agregado", Toast.LENGTH_SHORT).show()
            btnAgregar.setEnabled(false)
        }
        //verificar si el producto ya esta en la lista de carrito
        if (dataManager.cargarListaCompra().contains(producto))
            btnAgregar.isEnabled = false

        //Configurar el boton para regresar al main
        val btnReturn: Button = findViewById(R.id.Return)
        btnReturn.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
    }

    private fun actualizarInterfazUsuario(detallesProducto: Productos){
        findViewById<TextView>(R.id.nombreTextView).text = "Nomrbre: ${detallesProducto.nombre}"
        findViewById<TextView>(R.id.PrecioTextView).text = "Precio: ${detallesProducto.precio} $"
        findViewById<TextView>(R.id.CategoriasTextView).text = "Categoria: ${detallesProducto.categoria}"
    }

    private fun gurdarProuctoFavorito(producto: Productos) {
        dataManager.guardarProductoCar(producto)
    }


}