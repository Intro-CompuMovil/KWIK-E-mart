package co.kwik_e_mart.Gerente

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import co.kwik_e_mart.DataManager.DataManager
import co.kwik_e_mart.Mensajero.MensajeroAdapter
import co.kwik_e_mart.Mensajero.Mensajeros
import co.kwik_e_mart.Productos.ProductAdapter
import co.kwik_e_mart.R
import co.kwik_e_mart.databinding.ActivityGerenteinicioBinding
import co.kwik_e_mart.databinding.ActivityUserinicioBinding
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class GerenteInicio : AppCompatActivity() {

    private lateinit var binding: ActivityGerenteinicioBinding
    private lateinit var dataManager: DataManager


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGerenteinicioBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val json = """
[
    {"id": 1, "nombre": "Javier Useche", "calificacion": 5, "preciopromedio": 20000},
    {"id": 2, "nombre": "Juan Useche", "calificacion": 4.8, "preciopromedio": 25000},
    {"id": 3, "nombre": "Jorge Useche", "calificacion": 3.2, "preciopromedio": 22000},
    {"id": 4, "nombre": "Jesus Useche", "calificacion": 4.5, "preciopromedio": 10000},
    {"id": 5, "nombre": "Javier Acosta", "calificacion": 5, "preciopromedio": 30000}
]
"""

        val mensajerosCargador: List<Mensajeros> = Gson().fromJson(json, object : TypeToken<List<Mensajeros>>() {}.type)

        Log.d("GerenteInicio", "Tamaño de la lista de mensajeros cargados: ${mensajerosCargador.size}")

        val recyclerView = findViewById<RecyclerView>(R.id.mensajeroRecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)

        val adapter = MensajeroAdapter(mensajerosCargador.toMutableList())
        recyclerView.adapter = adapter

    }

}