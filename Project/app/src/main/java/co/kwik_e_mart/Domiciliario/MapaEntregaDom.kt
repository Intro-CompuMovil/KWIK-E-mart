package co.kwik_e_mart.Domiciliario

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Color
import android.location.Address
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import org.osmdroid.config.Configuration
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Marker
import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay
import android.location.Geocoder
import android.os.AsyncTask
import android.view.KeyEvent
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import android.widget.Toast
import co.kwik_e_mart.R
import org.osmdroid.bonuspack.routing.OSRMRoadManager
import org.osmdroid.events.MapEventsReceiver
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.views.overlay.MapEventsOverlay
import java.util.*
import org.osmdroid.bonuspack.routing.RoadManager
import org.osmdroid.bonuspack.routing.Road
import org.osmdroid.views.overlay.Polyline
import kotlin.collections.ArrayList

class MapaEntregaDom : AppCompatActivity() {

    private companion object {
        private const val LOCATION_PERMISSION_REQUEST_CODE = 1
    }

    private var map: MapView? = null
    private var myLocationOverlay: MyLocationNewOverlay? = null
    private var currentMarker: Marker? = null
    private var roadManager: RoadManager? = null
    private lateinit var addressInput: EditText
    private var currentLocation: GeoPoint? = null
    private var addressToSearch: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Configuration.getInstance().load(applicationContext, androidx.preference.PreferenceManager.getDefaultSharedPreferences(applicationContext))
        setContentView(R.layout.activity_gp_s)

        // Get address from intent
        val address = intent.getStringExtra("direccion") ?: ""

        addressInput = findViewById(R.id.addressInput)

        // Listen when the user clicks the "Enter" button on the keyboard
        addressInput.setOnEditorActionListener { _, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_DONE ||
                (event.action == KeyEvent.ACTION_DOWN && event.keyCode == KeyEvent.KEYCODE_ENTER)
            ) {
                // Make the search
                searchAddress(addressInput.text.toString())
                return@setOnEditorActionListener true
            }
            return@setOnEditorActionListener false
        }
        addressInput.isEnabled = false
        requestLocationPermission()
        setupMap(address)
        roadManager = OSRMRoadManager(this)
    }

    private fun setupMap(address: String ) {
        map = findViewById(R.id.map)
        map?.apply {
            setTileSource(TileSourceFactory.MAPNIK)
            setBuiltInZoomControls(true)
            setMultiTouchControls(true)
            controller.setZoom(15.0)
            invalidate()
        }
        addMyLocationOverlay()
        addMapLongClickListener()

        if (address.isNotEmpty()) {
            addressToSearch = address
        }
    }

    private fun addMyLocationOverlay() {
        myLocationOverlay = MyLocationNewOverlay(map)
        map?.overlays?.add(myLocationOverlay)
        myLocationOverlay?.enableMyLocation()

        myLocationOverlay?.runOnFirstFix {
            val location = myLocationOverlay?.myLocation
            if (location != null) {
                currentLocation = location
                runOnUiThread {
                    map?.controller?.setCenter(location)
                    addressInput.isEnabled = true
                    addressToSearch?.let { searchAddress(it) }
                }
            } else {
                runOnUiThread {
                    Toast.makeText(this@MapaEntregaDom, "Unable to get current location", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun searchAddress(address: String) {
        if (currentLocation != null) {
            if (address.isNotEmpty()) {
                val geocoder = Geocoder(this, Locale.getDefault())
                try {
                    val addresses = geocoder.getFromLocationName(address, 1)
                    if (addresses != null && addresses.isNotEmpty()) {
                        map?.let { map ->
                            map.overlays.forEach {
                                if (it is Marker) {
                                    map.overlays.remove(it)
                                }
                            }

                            val location = addresses[0]
                            val latitude = location.latitude
                            val longitude = location.longitude
                            val geoPoint = GeoPoint(latitude, longitude)

                            // Center the map on the address
                            map.controller.setCenter(geoPoint)

                            // Make a marker on the address
                            val marker = Marker(map)
                            marker.position = geoPoint
                            marker.title = address
                            map.overlays.add(marker)

                            // Remove previous road if exists
                            map.overlays.filterIsInstance<Polyline>().forEach {
                                map.overlays.remove(it)
                            }

                            // Execute the network operation in a background thread
                            GetRoadTask().execute(currentLocation, geoPoint)

                        }

                    } else {
                        Toast.makeText(this, "Address not found", Toast.LENGTH_SHORT).show()
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                    Toast.makeText(this, "Error searching address", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(this, "Please enter an address", Toast.LENGTH_SHORT).show()
            }
        } else {
            // Si la ubicación actual no está disponible, muestra un mensaje al usuario para que espere
            Toast.makeText(this, "Waiting to get current location", Toast.LENGTH_SHORT).show()
        }
    }

    private fun addMapLongClickListener() {
        val mapEventsOverlay = MapEventsOverlay(object : MapEventsReceiver {
            override fun singleTapConfirmedHelper(p: GeoPoint): Boolean {
                return false
            }

            override fun longPressHelper(p: GeoPoint): Boolean {
                currentMarker?.let {
                    map?.overlays?.remove(it)
                }

                currentMarker = Marker(map)
                currentMarker?.apply {
                    position = p
                    title = getAddressFromLatLng(this@MapaEntregaDom, p)
                    icon = ContextCompat.getDrawable(this@MapaEntregaDom, android.R.drawable.star_big_on)
                }

                map?.overlays?.add(currentMarker)

                map?.overlays?.filterIsInstance<Polyline>()?.forEach {
                    map?.overlays?.remove(it)
                }

                myLocationOverlay?.myLocation?.let { myLocation ->
                    // Execute the network operation in a background thread
                    GetRoadTask().execute(myLocation, p)
                } ?: run {
                    Toast.makeText(this@MapaEntregaDom, "Unable to get current location", Toast.LENGTH_SHORT).show()
                }

                return true
            }
        })

        map?.overlays?.add(0, mapEventsOverlay)
    }

    // AsyncTask to perform network operation in background
    private inner class GetRoadTask : AsyncTask<GeoPoint?, Void, Road>() {
        override fun doInBackground(vararg params: GeoPoint?): Road? {
            val startPoint = params[0]
            val endPoint = params[1]
            if (startPoint != null) {
                val roadManager = OSRMRoadManager(this@MapaEntregaDom)
                return roadManager.getRoad(
                    ArrayList(listOf(startPoint, endPoint))
                )
            } else {
                return null
            }
        }

        override fun onPostExecute(result: Road?) {
            if (result != null) {
                if (result.mStatus == Road.STATUS_OK) {
                    val roadOverlay = Polyline()
                    roadOverlay.setPoints(result.mRouteHigh)
                    roadOverlay.color = Color.BLUE
                    roadOverlay.width = 5.0f
                    map?.overlayManager?.add(roadOverlay)
                    map?.invalidate()
                } else {
                    Toast.makeText(this@MapaEntregaDom, "Error calculating road", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(this@MapaEntregaDom, "Failed to retrieve road data", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun requestLocationPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
            != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                LOCATION_PERMISSION_REQUEST_CODE
            )
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                addMyLocationOverlay()
            } else {
                Toast.makeText(
                    this,
                    "Permission denied. App may not work properly.",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    private fun getAddressFromLatLng(context: Context, latLng: GeoPoint): String {
        val geocoder = Geocoder(context, Locale.getDefault())
        try {
            val addresses = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1)
            if (addresses != null && addresses.size > 0) {
                val address = addresses[0]
                return address.getAddressLine(0)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return ""
    }
}