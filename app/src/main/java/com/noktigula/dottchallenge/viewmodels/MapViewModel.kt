package com.noktigula.dottchallenge.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.android.gms.maps.model.LatLng
import com.noktigula.dottchallenge.default
import com.noktigula.dottchallenge.model.MapMarker

private val SYDNEY = LatLng(-34.0, 151.0)

class MapViewModel : ViewModel() {
    val location = MutableLiveData<LatLng>().default(SYDNEY)
    val markers = MutableLiveData<List<MapMarker>>().default(emptyList())
}