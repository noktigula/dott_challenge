package com.noktigula.dottchallenge

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.noktigula.dottchallenge.model.MapMarker

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
        visibleMarkersStorage.add(
            this.addMarker(
                MarkerOptions()
                    .position(it.position)
                    .title(it.name)
            )
        )
    }
}

fun GoogleMap.removeInvisibleMarkers(visibleMarkersStorage: MutableList<Marker>) {
    visibleMarkersStorage.forEach { marker ->
        if (!marker.within(bounds())) {
            marker.remove()
        }
    }
}

fun GoogleMap.bounds() = projection.visibleRegion.latLngBounds

fun Marker.within(bounds: LatLngBounds) = bounds.contains(this.position)