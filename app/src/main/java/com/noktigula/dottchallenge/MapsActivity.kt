package com.noktigula.dottchallenge

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.noktigula.dottchallenge.listeners.UserLocationListener
import com.noktigula.dottchallenge.viewmodels.LocationViewModel

class MapsActivity : AppCompatActivity() {
    private val locationViewModel : LocationViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)
        val lifecycleObserver = UserLocationListener(this){ userLocation ->
            locationViewModel.location.value = LatLng(userLocation.latitude, userLocation.longitude)
        }
        lifecycle.addObserver(lifecycleObserver)
    }
}
