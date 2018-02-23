package me.rozkmin.googlemapsdraw

import android.support.v7.app.AppCompatActivity
import android.os.Bundle

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.model.Polyline
import me.rozkmin.gmapspolyline.AVOID
import me.rozkmin.gmapspolyline.DirectionsApiClient
import me.rozkmin.gmapspolyline.MODE
import me.rozkmin.gmapspolyline.TransitOptions

class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var googleMap: GoogleMap
    private lateinit var directionsApiClient: DirectionsApiClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)

        val mapFragment = supportFragmentManager
                .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        directionsApiClient = DirectionsApiClient(
                apiKey = getString(R.string.google_directions_key),
                logHttp = true)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        this.googleMap = googleMap

        val krk = LatLng(50.052, 19.944)
        this.googleMap.addMarker(
                MarkerOptions()
                        .position(krk)
                        .draggable(false)
                        .title("Marker in Kraków"))

        this.googleMap.addMarker(
                MarkerOptions()
                        .position(krk)
                        .draggable(true)
                        .title("Drag me")
        )

        this.googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(krk, 13f))

        this.googleMap.setOnMarkerDragListener(object : GoogleMap.OnMarkerDragListener {
            override fun onMarkerDragEnd(marker: Marker) {
                getPolylineToRoute(marker.position, krk)
            }

            override fun onMarkerDragStart(p0: Marker?) {}

            override fun onMarkerDrag(p0: Marker?) {}
        })
    }

    private val mutableList: MutableList<Polyline> = mutableListOf()

    fun getPolylineToRoute(from: LatLng, to: LatLng) {
        mutableList.forEach { it.remove() }
        mutableList.clear()

        val transitOptions = TransitOptions(mode = MODE.WALKING, whatToAvoidArray = arrayOf(AVOID.FERRIES, AVOID.HIGHWAYS))

        directionsApiClient.getRoutePolylines(origin = from, dest = to, options = transitOptions) {
            runOnUiThread {
                it.forEach {
                    //you can do additional polyline customization here
                    mutableList.add(googleMap.addPolyline(it))
                }
            }
        }
    }
}
