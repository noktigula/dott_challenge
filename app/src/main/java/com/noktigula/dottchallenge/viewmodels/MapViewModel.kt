package com.noktigula.dottchallenge.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.android.gms.maps.model.LatLng
import com.noktigula.dottchallenge.data.Repository
import com.noktigula.dottchallenge.default
import com.noktigula.dottchallenge.loge
import com.noktigula.dottchallenge.model.MapMarker

private val AMSTERDAM = LatLng(52.3667, 4.8945)
private val SYDNEY = LatLng(-34.0, 151.0)

class MapViewModel(repository: Repository) : ViewModel() {
    init {
        loge("Created new MapViewModel #${hashCode()}")
    }
    val location = MutableLiveData<LatLng>().default(SYDNEY)
    val markers = repository.markers
}