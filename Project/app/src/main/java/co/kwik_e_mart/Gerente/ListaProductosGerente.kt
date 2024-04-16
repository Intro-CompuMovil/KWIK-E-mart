package co.kwik_e_mart.Gerente

import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity
import co.kwik_e_mart.Productos.Productos
import co.kwik_e_mart.R
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken


class ListaProductosGerente: AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_listaproductosgerente)


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
        val listView = findViewById<ListView>(R.id.listViewProd)
        val nombresProductos = productosCargador.map { it.nombre }
        val adapter = ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, nombresProductos)
        listView.adapter = adapter

    }


}