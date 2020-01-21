package com.noktigula.dottchallenge.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.LatLng
import com.noktigula.dottchallenge.*
import com.noktigula.dottchallenge.model.MapMarker
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
            onLocation = ::onLocationUpdate,
            onMarkers =  ::onMarkersUpdate,
            onVenueSelected = ::onVenueSelected,
            repository = mapsActivity.repository
        )

        presenter.prepareMap(MapImpl(map))
    }

    private fun onVenueSelected(venue:MapMarker?) {
        (activity as MapsActivity).selectedViewModelFactory.selectedVenue.value = venue
    }

    private fun onLocationUpdate(callback:(LatLng)->Unit) {
        val viewModel = (activity as MapsActivity).mapViewModel
        observe(viewModel.location) { callback(it) }
    }

    private fun onMarkersUpdate(callback: (List<MapMarker>) -> Unit) {
        val viewModel = (activity as MapsActivity).mapViewModel
        observe(viewModel.markers) { callback(it) }
    }

    private fun <T> observe(data: LiveData<T>, callback: (T)->Unit) {
        data.observe(this, Observer(callback))
    }
}