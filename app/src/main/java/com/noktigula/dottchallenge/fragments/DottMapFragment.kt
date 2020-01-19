package com.noktigula.dottchallenge.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.noktigula.dottchallenge.*
import com.noktigula.dottchallenge.model.MapMarker

private const val STREET = 15f
private const val CITY = 10f
private const val DEFAULT_ZOOM = STREET

class DottMapFragment : Fragment(), OnMapReadyCallback {
    private val mapView: MapView by lazy { view!!.findViewById<MapView>(R.id.map_view) }
    private val visibleMarkers = mutableListOf<Marker>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_dott_map, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        mapView.onCreate(savedInstanceState)
        mapView.getMapAsync(this)
    }

    override fun onResume() {
        super.onResume()
        mapView.onResume()
    }

    override fun onMapReady(inMap: GoogleMap?) {
        val map = inMap ?: return

        map.moveCamera(CameraUpdateFactory.zoomTo(DEFAULT_ZOOM))
        val mapsActivity = activity as MapsActivity
        mapsActivity.mapViewModel.location.observe(this, Observer<LatLng> { userLocation ->
            map.moveCamera(CameraUpdateFactory.newLatLng(userLocation))
            loadRestaurants(map)
        })

        mapsActivity.mapViewModel.markers.observe(this, Observer<List<MapMarker>> { markers ->
            map.addMarkers(markers, visibleMarkers)
            map.removeInvisibleMarkers(visibleMarkers)
        })

        map.setOnCameraIdleListener { loadRestaurants(map) }
        map.setOnMarkerClickListener {
            mapsActivity.selectedViewModelFactory.selectedVenue.value = it.tag as MapMarker
            true
        }

        map.setOnMapClickListener {
            mapsActivity.selectedViewModelFactory.selectedVenue.value = null
        }

        map.setMinZoomPreference(CITY)
    }


    private fun loadRestaurants(map:GoogleMap) {
        val bounds = map.projection.visibleRegion.latLngBounds
        (activity as MapsActivity).repository.updateMarkersAsync(bounds)
    }
}