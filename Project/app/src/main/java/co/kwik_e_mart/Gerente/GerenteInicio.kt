package co.kwik_e_mart.Gerente

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import co.kwik_e_mart.DataManager.DataManager
import co.kwik_e_mart.Mensajero.MensajeroAdapter
import co.kwik_e_mart.Mensajero.Mensajeros
import co.kwik_e_mart.Paquetes.PaqueteAdapter
import co.kwik_e_mart.Paquetes.Paquetes
import co.kwik_e_mart.Productos.ProductAdapter
import co.kwik_e_mart.R
import co.kwik_e_mart.User.UserInicio
import co.kwik_e_mart.databinding.ActivityGerenteinicioBinding
import co.kwik_e_mart.databinding.ActivityUserinicioBinding
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class GerenteInicio : AppCompatActivity() {

    private lateinit var binding: ActivityGerenteinicioBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGerenteinicioBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val bttnListaProducto = findViewById<ImageButton>(R.id.botonListaProductos)
        val bttnadminInfo = findViewById<ImageButton>(R.id.imageButtonAdminData)

        val json = """
[
    {"id": 1, "producto1": "Agua Oxigenada", "producto2": "Jabon Liquido", "producto3": "Desengrasante"},
    {"id": 2, "producto1": "Amoniaco", "producto2": "Valvula", "producto3": "Flauta", "producto4": "Mezcladora", "producto5": "Jabon Liquido"},
    {"id": 3, "producto1": "Desengrasante", "producto2": "Limpiador", "producto3": "Hipoclorito", "producto4": "Amoniaco", "producto5": "Valvula"},
    {"id": 4, "producto1": "Flauta", "producto2": "Mezcladora", "producto3": "Agua Oxigenada", "producto4": "Jabon Liquido", "producto5": "Desengrasante"},
    {"id": 5, "producto1": "Limpiador", "producto2": "Hipoclorito", "producto3": "Amoniaco", "producto4": "Valvula", "producto5": "Flauta"},
    {"id": 6, "producto1": "Mezcladora", "producto2": "Agua Oxigenada", "producto3": "Jabon Liquido", "producto4": "Desengrasante", "producto5": "Limpiador"},
    {"id": 7, "producto1": "Hipoclorito", "producto2": "Amoniaco", "producto3": "Valvula", "producto4": "Flauta", "producto5": "Mezcladora"},
    {"id": 8, "producto1": "Desengrasante", "producto2": "Limpiador", "producto3": "Hipoclorito", "producto4": "Amoniaco", "producto5": "Mezcladora"},
    {"id": 9, "producto1": "Agua Oxigenada", "producto2": "Jabon Liquido", "producto3": "Flauta", "producto4": "Mezcladora", "producto5": "Limpiador"},
    {"id": 10, "producto1": "Amoniaco", "producto2": "Desengrasante", "producto3": "Hipoclorito", "producto4": "Valvula", "producto5": "Flauta"},
]
"""

        val paquetesCargador: List<Paquetes> = Gson().fromJson(json, object : TypeToken<List<Paquetes>>() {}.type)

        Log.d("GerenteInicio", "Tamaño de la lista de Paquetes cargados: ${paquetesCargador.size}")

        val recyclerView = findViewById<RecyclerView>(R.id.mensajeroRecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)

        val adapter = PaqueteAdapter(paquetesCargador.toMutableList())
        recyclerView.adapter = adapter

        bttnListaProducto.setOnClickListener{
            val intent = Intent(this, ListaProductosGerente::class.java)
            startActivity(intent)
        }

        bttnadminInfo.setOnClickListener{
            val intent = Intent(this, InfoGerente::class.java)
            startActivity(intent)
        }

    }

}