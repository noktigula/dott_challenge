package com.noktigula.dottchallenge.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.OnMapReadyCallback
import com.noktigula.dottchallenge.*
import com.noktigula.dottchallenge.presenters.DottMapPresenterImpl
import com.noktigula.dottchallenge.presenters.MapImpl

class DottMapFragment : Fragment(), OnMapReadyCallback {
    private val mapView: MapView by lazy { view!!.findViewById<MapView>(R.id.map_view) }

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
        val mapsActivity = activity as MapsActivity

        val presenter = DottMapPresenterImpl(
            mapsActivity,
            mapsActivity.mapViewModel,
            mapsActivity.selectedViewModelFactory,
            mapsActivity.repository
        )

        presenter.prepareMap(MapImpl(map))
    }
}