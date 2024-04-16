package co.kwik_e_mart.Gerente

import android.os.Bundle
import android.util.Log
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import co.kwik_e_mart.Entrega.EntregaAdapter
import co.kwik_e_mart.Entrega.Entregas
import co.kwik_e_mart.Mensajero.MensajeroAdapter
import co.kwik_e_mart.Mensajero.Mensajeros
import co.kwik_e_mart.R
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class DomiciliarioElegido : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_domiciliario_elegido)

        val nombreMensajero = intent.getStringExtra("nombreMensajero")
        var nombreCourierTextView = findViewById<TextView>(R.id.textViewNameCourier)

        nombreCourierTextView.text = nombreMensajero

        val json = """
[
    {"id": 1, "productos": "A-B-C-D", "direccion": "Javeriana"},
    {"id": 2, "productos": "E-F-G-H", "direccion": "Externado"},
    {"id": 3, "productos": "I-J-K-L", "direccion": "Universidad Nacional"},
    {"id": 4, "productos": "M-N-O-P", "direccion": "Salitre Magico"},
    {"id": 5, "productos": "Q-R-S-T", "direccion": "Plaza Simon Bolivar"}
]

"""

        val entregasCargador: List<Entregas> = Gson().fromJson(json, object : TypeToken<List<Entregas>>() {}.type)

        Log.d("DomiciliarioElegido", "Tamaño de la lista de Entregas cargados: ${entregasCargador.size}")

        val recyclerView = findViewById<RecyclerView>(R.id.entregasRecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)

        val adapter = EntregaAdapter(entregasCargador.toMutableList())
        recyclerView.adapter = adapter

    }
}