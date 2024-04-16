package co.kwik_e_mart.Mensajero

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import co.kwik_e_mart.DataManager.DataManager
import co.kwik_e_mart.R

class MensajeroList: AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var dataManager: DataManager
    private lateinit var mensajeroAdapter: MensajeroAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_gerenteinicio)

        recyclerView = findViewById(R.id.mensajeroRecyclerView)
        dataManager = DataManager(this)
        loadMensajeros(dataManager.cargarListaMensajeros())

    }

    private fun loadMensajeros(couriers: List<Mensajeros>) {
        mensajeroAdapter = MensajeroAdapter(couriers, dataManager, courierList = dataManager.cargarListaMensajeros())
        recyclerView.apply {
            layoutManager = LinearLayoutManager(this@MensajeroList)
            adapter = mensajeroAdapter
        }
    }

}

