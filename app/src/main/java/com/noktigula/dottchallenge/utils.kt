package com.noktigula.dottchallenge

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.noktigula.dottchallenge.model.MapMarker
import com.noktigula.dottchallenge.model.Snippet

private const val TAG = "Dott"

fun <T> MutableLiveData<T>.default(initialValue:T) : MutableLiveData<T> {
    this.value = initialValue
    return this
}

fun loge(message:String) {
    if (BuildConfig.DEBUG) {
        Log.e(TAG, message)
    }
}

fun LatLng.simpleString() : String = "${latitude},${longitude}"

fun GoogleMap.addMarkers(markers:List<MapMarker>, visibleMarkersStorage:MutableList<Marker>) {
    markers.forEach {
        val marker = this.addMarker(
            MarkerOptions()
                .position(it.position)
                .title(it.name)
        )
        marker.tag = it
        visibleMarkersStorage.add(marker)
    }
}

fun GoogleMap.removeInvisibleMarkers(visibleMarkersStorage: MutableList<Marker>) {
    val iterator = visibleMarkersStorage.iterator()
    while(iterator.hasNext()) {
        val marker = iterator.next()
        if (!marker.within(bounds())) {
            marker.remove()
            iterator.remove()
        }
    }
}

fun GoogleMap.bounds() = projection.visibleRegion.latLngBounds

fun GoogleMap.zoomTo(level:Float) {
    moveCamera(CameraUpdateFactory.zoomTo(level))
}

fun GoogleMap.show(location:LatLng) {
    moveCamera(CameraUpdateFactory.newLatLng(location))
}

fun Marker.within(bounds: LatLngBounds) = bounds.contains(this.position)

fun Snippet.toMapMarker() : MapMarker {
    return MapMarker(this.location, this.name, this.address)
}