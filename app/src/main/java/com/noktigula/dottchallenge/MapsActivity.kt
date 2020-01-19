package com.noktigula.dottchallenge

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.lifecycle.ViewModelProviders

import com.google.android.gms.maps.model.LatLng
import com.noktigula.dottchallenge.data.Repository
import com.noktigula.dottchallenge.listeners.UserLocationListener
import com.noktigula.dottchallenge.network.RetrofitInstance
import com.noktigula.dottchallenge.viewmodels.MapViewModel
import com.noktigula.dottchallenge.viewmodels.MapViewModelFactory

class MapsActivity : AppCompatActivity() {
    val repository by lazy {
        Repository(RetrofitInstance.foursquareApi)
    }

    val mapViewModel : MapViewModel by lazy {
        val factory = MapViewModelFactory(repository)
        ViewModelProviders.of(this, factory).get(MapViewModel::class.java)
    }

    private val locationListener : UserLocationListener by lazy {
        UserLocationListener(this){ userLocation ->
            mapViewModel.location.value = LatLng(userLocation.latitude, userLocation.longitude)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)

        RetrofitInstance.initApi(getString(R.string.foursquare_id), getString(R.string.foursquare_secret))

        lifecycle.addObserver(locationListener)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        locationListener.onRequestPermissionsResult(requestCode, permissions, grantResults)
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }
}
