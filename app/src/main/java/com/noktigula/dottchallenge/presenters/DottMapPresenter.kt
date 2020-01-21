package com.noktigula.dottchallenge.presenters

import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.Marker
import com.noktigula.dottchallenge.*
import com.noktigula.dottchallenge.data.Repository
import com.noktigula.dottchallenge.model.MapMarker

private const val STREET = 15f
private const val CITY = 10f
private const val DEFAULT_ZOOM = STREET

typealias Subscription<T> = ((T)->Unit)->Unit
typealias Consumer<T> = (T)->Unit

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
    private val onLocation: Subscription<LatLng>,
    private val onMarkers: Subscription<List<MapMarker>>,
    private val onVenueSelected: Consumer<MapMarker?>,
    private val repository:Repository
) : DottMapPresenter {
    override fun prepareMap(map: Map) {
        map.zoomTo(DEFAULT_ZOOM)
        map.setMinimalZoomLevel(CITY)

        onLocation {
            map.showLocation(it)
            loadRestaurants(map.bounds())
        }

        onMarkers { map.onMarkersUpdate(it) }

        map.onMarkerClicked { onVenueSelected(it.tag as MapMarker) }
        map.onMapClicked { onVenueSelected(null) }

        map.onMapScrollStopped { loadRestaurants(map.bounds()) }
    }

    private fun loadRestaurants(bounds: LatLngBounds) {
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