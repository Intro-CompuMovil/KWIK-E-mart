package co.kwik_e_mart

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import android.widget.EditText
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

class GpS : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_gp_s)
        createFragment();
    }

    private fun createFragment() {
        val mapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    override fun onMapReady(googleMap: GoogleMap) {
       mMap = googleMap;
       createMarker();
    }

    private fun createMarker() {
        val coordinates = LatLng(4.628075, -74.064728)
        val coordinates2 = LatLng(4.628256, -74.065366)
        val coordinates3 = LatLng(4.627319, -74.065322)
        val mains = LatLng(4.6287662, -74.0636298647595)

        val marker = MarkerOptions().position(coordinates).title("Domiciliario: 1")
        val marker2 = MarkerOptions().position(coordinates2).title("Domiciliario: 2")
        val marker3 = MarkerOptions().position(coordinates3).title("Domiciliario: 3")

        mMap.addMarker(marker)
        mMap.addMarker(marker2)
        mMap.addMarker(marker3)

        mMap.animateCamera(
            CameraUpdateFactory.newLatLngZoom(mains, 18f), 4000, null
        )
    }



}



