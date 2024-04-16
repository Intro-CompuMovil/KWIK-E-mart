package co.kwik_e_mart.User

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

class GpS : AppCompatActivity() {

    private companion object {
        private const val LOCATION_PERMISSION_REQUEST_CODE = 1
    }

    private var map: MapView? = null
    private var myLocationOverlay: MyLocationNewOverlay? = null
    private var currentMarker: Marker? = null
    private var roadManager: RoadManager? = null
    private lateinit var addressInput: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Configuration.getInstance().load(applicationContext, androidx.preference.PreferenceManager.getDefaultSharedPreferences(applicationContext))
        setContentView(R.layout.activity_gp_s)

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

        requestLocationPermission()
        setupMap()
        roadManager = OSRMRoadManager(this)
    }

    private fun searchAddress(address: String) {
        if (address.isNotEmpty()) {
            val geocoder = Geocoder(this, Locale.getDefault())
            try {
                val addresses = geocoder.getFromLocationName(address, 1)
                if (addresses != null && addresses.isNotEmpty()) {

                    map!!.overlays.forEach {
                        if (it is Marker) {
                            map!!.overlays.remove(it)
                        }
                    }

                    val location = addresses[0]
                    val latitude = location.latitude
                    val longitude = location.longitude
                    val geoPoint = GeoPoint(latitude, longitude)

                    // Center the map on the address
                    map?.controller?.setCenter(geoPoint)

                    // Make a marker on the address
                    val marker = Marker(map)
                    marker.position = geoPoint
                    marker.title = address
                    map?.overlays?.add(marker)

                    // Remove previous road if exists
                    map!!.overlays.forEach {
                        if (it is Polyline) {
                            map!!.overlays.remove(it)
                        }
                    }

                    // Execute the network operation in a background thread
                    GetRoadTask().execute(myLocationOverlay!!.myLocation, geoPoint)

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
    }

    private fun setupMap() {
        map = findViewById(R.id.map)
        map!!.setTileSource(TileSourceFactory.MAPNIK)
        map!!.setBuiltInZoomControls(true)
        map!!.setMultiTouchControls(true)
        map!!.controller.setZoom(15.0)
        map!!.invalidate()
        addMyLocationOverlay()
        addMapLongClickListener()
    }
    private fun addMyLocationOverlay() {
        myLocationOverlay = MyLocationNewOverlay(map)
        map!!.overlays.add(myLocationOverlay)
        myLocationOverlay!!.enableMyLocation()

        myLocationOverlay!!.runOnFirstFix {
            val location = myLocationOverlay!!.myLocation
            if (location != null) {
                runOnUiThread {
                    map!!.controller.setCenter(location)
                }
            } else {
                runOnUiThread {
                    Toast.makeText(this@GpS, "Unable to get current location", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
    private fun addMapLongClickListener() {
        val mapEventsOverlay = MapEventsOverlay(object : MapEventsReceiver {
            override fun singleTapConfirmedHelper(p: GeoPoint): Boolean {
                return false
            }
            override fun longPressHelper(p: GeoPoint): Boolean {
                if (currentMarker != null) {
                    map!!.overlays.remove(currentMarker)
                }

                currentMarker = Marker(map)
                currentMarker!!.position = p
                currentMarker!!.title = getAddressFromLatLng(this@GpS, p)
                currentMarker!!.icon = ContextCompat.getDrawable(this@GpS, android.R.drawable.star_big_on)
                map!!.overlays.add(currentMarker)

                // Remove previous road if exists
                map!!.overlays.forEach {
                    if (it is Polyline) {
                        map!!.overlays.remove(it)
                    }
                }

                // Execute the network operation in a background thread
                GetRoadTask().execute(myLocationOverlay!!.myLocation, p)

                return true
            }
        })

        map!!.overlays.add(0, mapEventsOverlay)
    }

    // AsyncTask to perform network operation in background
    private inner class GetRoadTask : AsyncTask<GeoPoint, Void, Road>() {
        override fun doInBackground(vararg params: GeoPoint): Road? {
            val startPoint = params[0]
            val endPoint = params[1]
            val roadManager = OSRMRoadManager(this@GpS)
            return roadManager.getRoad(
                ArrayList(listOf(startPoint, endPoint))
            )
        }

        override fun onPostExecute(result: Road?) {
            if (result != null) {
                if (result.mStatus == Road.STATUS_OK) {
                    val roadOverlay = Polyline()
                    roadOverlay.setPoints(result.mRouteHigh)
                    roadOverlay.color = Color.BLUE
                    roadOverlay.width = 5.0f
                    map!!.overlayManager.add(roadOverlay)
                    map!!.invalidate()
                } else {
                    Toast.makeText(this@GpS, "Error calculating road", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(this@GpS, "Failed to retrieve road data", Toast.LENGTH_SHORT).show()
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
