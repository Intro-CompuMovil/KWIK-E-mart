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
import co.kwik_e_mart.Productos.ProductAdapter
import co.kwik_e_mart.R
import co.kwik_e_mart.databinding.ActivityGerenteinicioBinding
import co.kwik_e_mart.databinding.ActivityUserinicioBinding

class GerenteInicio : AppCompatActivity() {

    private lateinit var binding: ActivityGerenteinicioBinding
    private lateinit var dataManager: DataManager


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGerenteinicioBinding.inflate(layoutInflater)
        setContentView(binding.root)

        dataManager = DataManager(this)

        val mensajerosCargador = dataManager.cargarMensjaeros()
        val courierList = dataManager.cargarListaMensajeros()

        Log.d("GerenteInicio", "Tamaño de la lista de mensajeros cargados: ${mensajerosCargador.size}")

        // Registrar el tamaño de la lista de mensajeros del carrito
        Log.d("GerenteInicio", "Tamaño de la lista de mensajeros del carrito: ${courierList.size}")

        val recyclerView = findViewById<RecyclerView>(R.id.mensajeroRecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)

        val adapter = MensajeroAdapter(mensajerosCargador.toMutableList(), dataManager, courierList)
        recyclerView.adapter = adapter

    }

}