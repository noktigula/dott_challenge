package com.noktigula.dottchallenge.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.LatLng
import com.noktigula.dottchallenge.network.FoursquareApi
import com.noktigula.dottchallenge.viewmodels.MapViewModel
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import com.facebook.stetho.okhttp3.StethoInterceptor
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.noktigula.dottchallenge.*
import com.noktigula.dottchallenge.data.Repository
import com.noktigula.dottchallenge.model.MapMarker
import com.noktigula.dottchallenge.network.RetrofitInstance
import com.noktigula.dottchallenge.viewmodels.MapViewModelFactory
import okhttp3.OkHttpClient

private const val DEFAULT_ZOOM = 15f

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
    }

    private fun loadRestaurants(map:GoogleMap) {
        val bounds = map.projection.visibleRegion.latLngBounds
        (activity as MapsActivity).repository.updateMarkersAsync(bounds)
    }
}