package co.kwik_e_mart

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.Button
import android.widget.Spinner


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val BUTTDestiny = findViewById<Button>(R.id.button)
        val BUTTlistaCompra = findViewById<Button>(R.id.button2)
        val Spinner = findViewById<Spinner>(R.id.spinner)

        Spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                val selectedItem = parent.getItemAtPosition(position).toString()

                Log.d("Spinner", "Item seleccionado: $selectedItem")
                Log.d("Actividad1", "Valor seleccionado: $selectedItem")
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                // Se llama cuando no se ha seleccionado ningún elemento.
            }
        }

        BUTTDestiny.setOnClickListener{
            val selectedItem = Spinner.selectedItem.toString()
            Log.d("Mandado", selectedItem)
            val intentDestiny = Intent(this,ProductoList::class.java)
            intentDestiny.putExtra("llave", selectedItem)
            startActivity(intentDestiny)
        }

        BUTTlistaCompra.setOnClickListener{
            val intentListaCompras = Intent(this,ListaCompra::class.java)
            startActivity(intentListaCompras)
        }
    }
}