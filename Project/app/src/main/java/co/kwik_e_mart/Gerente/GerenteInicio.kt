package co.kwik_e_mart.Gerente

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import co.kwik_e_mart.R
import co.kwik_e_mart.Paquetes.PaqueteAdapter
import co.kwik_e_mart.Paquetes.Paquetes
import com.google.android.material.navigation.NavigationView
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class GerenteInicio : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    private lateinit var drawerLayout: DrawerLayout
    private lateinit var paqueteAdapter: PaqueteAdapter
    private lateinit var textViewDatosAdmin: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_gerenteinicio)

        val toolbar: androidx.appcompat.widget.Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        drawerLayout = findViewById(R.id.drawer_layout)
        val navView: NavigationView = findViewById(R.id.nav_view)
        val toggle = ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        navView.setNavigationItemSelectedListener(this)

        val recyclerView = findViewById<RecyclerView>(R.id.mensajeroRecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)

        paqueteAdapter = PaqueteAdapter(mutableListOf())
        recyclerView.adapter = paqueteAdapter

        textViewDatosAdmin = findViewById(R.id.textViewDatosAdmin)
        loadAdminData()

        val bttnListaProducto = findViewById<ImageButton>(R.id.botonListaProductos)
        val bttnadminInfo = findViewById<ImageButton>(R.id.imageButtonAdminData)

        bttnListaProducto.setOnClickListener {
            val intent = Intent(this, ListaProductosGerente::class.java)
            startActivity(intent)
        }

        bttnadminInfo.setOnClickListener {
            val intent = Intent(this, InfoGerente::class.java)
            startActivity(intent)
        }
    }

    private fun loadAdminData() {
        val database = FirebaseDatabase.getInstance().reference
        database.child("adminData").addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val adminData = snapshot.getValue(String::class.java) ?: "Admin info"
                textViewDatosAdmin.text = adminData
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("GerenteInicio", "Error loading admin data", error.toException())
            }
        })

        database.child("paquetes").addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val paquetesList = mutableListOf<Paquetes>()
                for (paqueteSnapshot in snapshot.children) {
                    val paquete = paqueteSnapshot.getValue(Paquetes::class.java)
                    if (paquete != null) {
                        paquetesList.add(paquete)
                    }
                }
                paqueteAdapter.updateData(paquetesList)
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("GerenteInicio", "Error loading paquetes", error.toException())
            }
        })
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.nav_profile -> {
                startActivity(Intent(this, InfoGerente::class.java))
            }
            R.id.nav_stock -> {
                startActivity(Intent(this, StockActivity::class.java))
            }
            R.id.nav_orders_completed -> {
                startActivity(Intent(this, PedidosRealizadosActivity::class.java))
            }
            R.id.nav_orders_pending -> {
                startActivity(Intent(this, PedidosEnEsperaActivity::class.java))
            }
        }
        drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }

    override fun onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }
}
