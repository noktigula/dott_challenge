package com.noktigula.dottchallenge.presenters

import android.location.LocationProvider
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.Marker
import com.noktigula.dottchallenge.*
import com.noktigula.dottchallenge.data.Repository
import com.noktigula.dottchallenge.model.MapMarker
import com.noktigula.dottchallenge.viewmodels.MapViewModel
import com.noktigula.dottchallenge.viewmodels.SelectedVenueViewModel

private const val STREET = 15f
private const val CITY = 10f
private const val DEFAULT_ZOOM = STREET

interface DottMapPresenter {
    fun prepareMap(map:Map)
}

interface Map {
    fun zoomTo(level:Float)
    fun showLocation(location: LatLng)
    fun onMarkersUpdate(markers: List<MapMarker>)
    fun onMapScrollStopped(callback:()->Unit)
    fun onMarkerClicked(callback:(Marker)->Unit)
    fun onMapClicked(callback:()->Unit)
    fun setMinimalZoomLevel(level:Float)
    fun bounds() : LatLngBounds
}

class DottMapPresenterImpl(
    private val lifecycleOwner:LifecycleOwner,
    private val mapViewModel:MapViewModel,
    private val selectedVenueViewModel: SelectedVenueViewModel,
    private val repository:Repository
) : DottMapPresenter {
    override fun prepareMap(map: Map) {
        map.zoomTo(DEFAULT_ZOOM)
        map.setMinimalZoomLevel(CITY)

        onLocationUpdate(mapViewModel) {
            map.showLocation(it)
            loadRestaraunts(map.bounds())
        }

        onMarkersUpdate(mapViewModel) { map.onMarkersUpdate(it) }

        map.onMarkerClicked { selectedVenueViewModel.selectedVenue.value = it.tag as MapMarker }
        map.onMapClicked { selectedVenueViewModel.selectedVenue.value = null }

        map.onMapScrollStopped { loadRestaraunts(map.bounds()) }
    }

    private fun onLocationUpdate(viewModel: MapViewModel, callback:(LatLng)->Unit) {
        observe(viewModel.location) { callback(it) }
    }

    private fun onMarkersUpdate(viewModel: MapViewModel, callback: (List<MapMarker>) -> Unit) {
        observe(viewModel.markers) { callback(it) }
    }

    private fun <T> observe(data: LiveData<T>, callback: (T)->Unit) {
        data.observe(lifecycleOwner, Observer(callback))
    }

    private fun loadRestaraunts(bounds: LatLngBounds) {
        repository.updateMarkersAsync(bounds)
    }
}

class MapImpl (
    private val map: GoogleMap
) : Map {

    private val visibleMarkers = mutableListOf<Marker>()

    override fun zoomTo(level: Float) {
        map.zoomTo(level)
    }

    override fun showLocation(location: LatLng) {
        map.show(location)
    }

    override fun onMarkersUpdate(markers: List<MapMarker>) {
        map.addMarkers(markers, visibleMarkers)
        map.removeInvisibleMarkers(visibleMarkers)
    }

    override fun onMapScrollStopped(callback: () -> Unit) {
        map.setOnCameraIdleListener(callback)
    }

    override fun onMarkerClicked(callback: (Marker) -> Unit) {
        map.setOnMarkerClickListener {
            callback(it)
            true
        }
    }

    override fun onMapClicked(callback: () -> Unit) {
        map.setOnMapClickListener{
            callback()
        }
    }

    override fun setMinimalZoomLevel(level: Float) {
        map.setMinZoomPreference(level)
    }

    override fun bounds(): LatLngBounds = map.bounds()
}