package com.example.destinos

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity
import org.json.JSONObject
import java.io.IOException
import java.io.InputStream

class ProductoList : AppCompatActivity(){

    var arreglo = mutableListOf<String>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_destinoexplorelist)

        val adapter = ArrayAdapter(this,android.R.layout.simple_list_item_1,arreglo)
        val listView: ListView = findViewById(R.id.list_view1)
        val spinnerSelected = intent.getStringExtra("llave")
        Log.d("Actividad2", "Valor recibido: $spinnerSelected")

        listView.adapter = adapter;

        val json = JSONObject(loadJSONAsset())
        val productoJson = json.getJSONArray("productos")
        for (i in 0 until productoJson.length()){

            val jsonObject = productoJson.getJSONObject(i)
            if (jsonObject.getString("categoria").equals(spinnerSelected)) {
                arreglo.add(jsonObject.getString("nombre"))
            }
            else if (spinnerSelected.equals("Todos")){
                arreglo.add(jsonObject.getString("nombre"))
            }
        }

        listView.setOnItemClickListener { _, _, position, _ ->
            val productoSeleccionado = arreglo[position]
            val intent = Intent(this, DetallesProducto::class.java).apply {
                putExtra("prodcutollave", productoSeleccionado)
            }
            Log.d("ProductoSeleccionado", "Valor recibido: $productoSeleccionado")
            startActivity(intent)
        }
    }

    private fun loadJSONAsset():String?{
        var json:String? = null
        try{
            var isStream: InputStream = assets.open("productos.json")
            val size:Int = isStream.available()
            val buffer = ByteArray(size)
            isStream.read(buffer)
            isStream.close()
            json = String(buffer,Charsets.UTF_8)
        }catch (ex: IOException){
            ex.printStackTrace()
            return null
        }
        return json
    }
}