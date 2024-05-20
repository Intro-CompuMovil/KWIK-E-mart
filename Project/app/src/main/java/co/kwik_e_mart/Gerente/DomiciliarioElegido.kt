package co.kwik_e_mart.Gerente

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import co.kwik_e_mart.Entrega.NewProductAdapter
import co.kwik_e_mart.Productos.Productos
import co.kwik_e_mart.R
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class DomiciliarioElegido : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_domiciliario_elegido)

        val p1TextView: TextView = findViewById(R.id.p1)
        val p2TextView: TextView = findViewById(R.id.p2)
        val p3TextView: TextView = findViewById(R.id.p3)
        val p4TextView: TextView = findViewById(R.id.p4)
        val p5TextView: TextView = findViewById(R.id.p5)

        val btnCarrito: ImageButton = findViewById(R.id.btnCarrito)

        val producto1 = intent.getStringExtra("producto1")
        val producto2 = intent.getStringExtra("producto2")
        val producto3 = intent.getStringExtra("producto3")
        val producto4 = intent.getStringExtra("producto4")
        val producto5 = intent.getStringExtra("producto5")
        var paqueteProductosTextView = findViewById<TextView>(R.id.textViewpaqueteProductos)



        if (producto1 == null){
            paqueteProductosTextView.text = "Paquete sin Productos"
        }

            else if (producto2 == null){
                paqueteProductosTextView.text = "$producto1"
            }

                else if (producto3 == null){
                    paqueteProductosTextView.text = "$producto1 - $producto2"
                }

                    else if (producto4 == null){
                    paqueteProductosTextView.text = "$producto1 - $producto2 - $producto3"
                    }

                        else
                        paqueteProductosTextView.text = "$producto1 - $producto2 - $producto3 - $producto4 - $producto5"


        val json = """
[
    {"id": 1, "nombre": "Agua Oxigenada", "precio": "4000", "categoria": "Quimicos"},
    {"id": 2, "nombre": "Jabon Liquido", "precio": "8000", "categoria": "Limpieza"},
    {"id": 3, "nombre": "Desengrasante", "precio": "15000", "categoria": "Limpieza"},
    {"id": 4, "nombre": "Limpiador", "precio": "6000", "categoria": "Limpieza"},
    {"id": 5, "nombre": "Hipoclorito", "precio": "7000", "categoria": "Quimicos"},
    {"id": 6, "nombre": "Amoniaco", "precio": "3000", "categoria": "Quimicos"},
    {"id": 7, "nombre": "Valvula", "precio": "30000", "categoria": "Productos"},
    {"id": 8, "nombre": "Flauta", "precio": "35000", "categoria": "Productos"},
    {"id": 9, "nombre": "Mezcladora", "precio": "40000", "categoria": "Productos"}
  ]

"""

        val productosCargador: List<Productos> = Gson().fromJson(json, object : TypeToken<List<Productos>>() {}.type)

        Log.d("DomiciliarioElegido", "Tamaño de la lista de Entregas cargados: ${productosCargador.size}")

        val recyclerView = findViewById<RecyclerView>(R.id.entregasRecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)

        val adapter = NewProductAdapter(productosCargador.toMutableList(),p1TextView)
        recyclerView.adapter = adapter

        btnCarrito.setOnClickListener{
            val intent = Intent(this, Carrito::class.java)
            startActivity(intent)
        }

    }

}