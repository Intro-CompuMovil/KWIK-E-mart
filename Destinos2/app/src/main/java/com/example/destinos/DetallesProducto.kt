package com.example.destinos

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import org.json.JSONObject
import java.io.IOException
import java.io.InputStream



class DetallesProducto : AppCompatActivity(){
    var arregloJsons = mutableListOf<String>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detallesdestino)

        val information = findViewById<TextView>(R.id.textView)
        val adapter = ArrayAdapter(this,android.R.layout.simple_list_item_1,arregloJsons)
        val productoSelected = intent.getStringExtra("prodcutollave")
        val buttonReturn = findViewById<Button>(R.id.button6)
        val buttonListCompra = findViewById<Button>(R.id.button5)
        val viewModel = ViewModelProvider(this).get(SharedListaCompraxDetallesDestino::class.java)

        var nombre = "placeholder"
        var categoria = "placeholder"
        var precio = "placeholder"
        var truefor = 0;


        Log.d("Actividad3", "Valor recibido: $productoSelected")

        val json = JSONObject(loadJSONAsset())
        val productoJson = json.getJSONArray("productos")
        for (i in 0 until productoJson.length()) {
            val jsonObject = productoJson.getJSONObject(i)
            if (jsonObject.getString("nombre").equals(productoSelected)){
                nombre = jsonObject.getString("nombre")
                categoria = jsonObject.getString("categoria")
                precio = jsonObject.getString("precio")
                val todaInfo = "$nombre \n $categoria \n $precio"
                information.text = todaInfo
                truefor = 1;
                break
            }
        }
/*
        if (truefor == 1){
            buttonListCompra.setOnClickListener{
                viewModel.nombreSeleccionado = nombre
                Log.d("Actividad4, Mandar info a Lista de Compras", "Valor recibido: $nombre")
            }
        }
*/

        buttonReturn.setOnClickListener{
            val intent = Intent(this,MainActivity::class.java)
            startActivity(intent)
        }

        buttonListCompra.setOnClickListener {
            Toast.makeText(applicationContext,"Incluido en el carrito $nombre",Toast.LENGTH_SHORT).show()
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