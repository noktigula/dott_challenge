package com.noktigula.dottchallenge.fragments

import android.location.Location
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.noktigula.dottchallenge.R
import com.noktigula.dottchallenge.loge
import com.noktigula.dottchallenge.viewmodels.LocationViewModel

private const val DEFAULT_ZOOM = 15f

class DottMapFragment : Fragment(), OnMapReadyCallback {
    private val mapView: MapView by lazy { view!!.findViewById<MapView>(R.id.map_view) }
    private val locationViewModel : LocationViewModel by lazy {
        ViewModelProviders.of(activity!!)[LocationViewModel::class.java]
    }

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
        locationViewModel.location.observe(this, Observer<LatLng> { userLocation ->
            map.moveCamera(CameraUpdateFactory.newLatLng(userLocation))
        })
    }
}