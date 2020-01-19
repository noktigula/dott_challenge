package com.noktigula.dottchallenge.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.android.gms.maps.model.LatLng
import com.noktigula.dottchallenge.default

private val AMSTERDAM = LatLng(52.3667, 4.8945)
private val SYDNEY = LatLng(-34.0, 151.0)

class LocationViewModel : ViewModel() {
    val location = MutableLiveData<LatLng>().default(SYDNEY)
}