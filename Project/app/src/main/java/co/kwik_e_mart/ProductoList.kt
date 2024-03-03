package co.kwik_e_mart

import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity

class ProductoList : AppCompatActivity(){

    private val dataManager = DataManager(this)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_destinoexplorelist)

        val listView: ListView = findViewById(R.id.listView)
        val categorias = intent.getStringExtra("categoria") ?: ""

        // Verificacion
        val productos = if (categorias == "Todos"){
            dataManager.cargarProductos()
        } else {
            dataManager.cargarProductosSeleccionado(categorias)
        }

        val adapter =
            ArrayAdapter(this, android.R.layout.simple_list_item_1, productos.map { it.nombre })
        listView.adapter = adapter

        listView.setOnItemClickListener { _, _, position, _ ->
            val productosSeleccionado = productos[position]
            val intent = Intent(this, DetallesProducto::class.java).apply {
                putExtra("producto", productosSeleccionado)
            }
            startActivity(intent)
        }
    }

}