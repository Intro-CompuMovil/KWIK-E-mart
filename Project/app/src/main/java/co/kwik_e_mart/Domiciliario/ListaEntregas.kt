package co.kwik_e_mart.Domiciliario

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ListView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import co.kwik_e_mart.DataManager.DataManager
import co.kwik_e_mart.Entrega.Entregas
import co.kwik_e_mart.R
import co.kwik_e_mart.User.CamaraPermisos

class ListaEntregas: AppCompatActivity() {

    private lateinit var dataManager: DataManager
    private lateinit var listView: ListView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_listaentregas)

        dataManager = DataManager(this)
        listView = findViewById(R.id.listViewEntregas)
        val deliveryList = dataManager.cargarListaEntregas().toMutableList()
        val adapter = DeliveryAdapter(this, deliveryList, dataManager)
        listView.adapter = adapter

    }

    class DeliveryAdapter(
        context: Context,
        private var entregas: MutableList<Entregas>,
        private val dataManager: DataManager
    ) : ArrayAdapter<Entregas>(context, 0, entregas) {
        override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
            var listaEntregaView = convertView
            if (listaEntregaView == null) {
                listaEntregaView =
                    LayoutInflater.from(context).inflate(R.layout.item_entrega, parent, false)
            }

            val carrito = entregas[position]

            // Obtener referencias a las vistas de item_carrito.xml
            val productoTextView = listaEntregaView!!.findViewById<TextView>(R.id.ProductosTextView)
            val direccionTextView = listaEntregaView.findViewById<TextView>(R.id.DireccionTextView)

            //Establecer los datos del carrito en las vistas
            productoTextView.text = carrito.productos
            direccionTextView.text = carrito.direccion

            val btnAceptar = listaEntregaView.findViewById<Button>(R.id.btnElegirTarea)
            val btnEliminar = listaEntregaView.findViewById<Button>(R.id.btnRechazarTarea)

            // Configurar el clic del boton eliminar
            btnAceptar.setOnClickListener {

            }

            btnEliminar.setOnClickListener {

            }

            return listaEntregaView
        }

    }
}