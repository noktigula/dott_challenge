package com.noktigula.dottchallenge.model

import com.google.android.gms.maps.model.LatLng

data class MapMarker (
    val position:LatLng,
    val name:String,
    val address:String
)