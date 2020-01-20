package com.noktigula.dottchallenge

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.LinearLayout
import android.widget.TextView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders

import com.google.android.gms.maps.model.LatLng
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.noktigula.dottchallenge.data.CacheImpl
import com.noktigula.dottchallenge.data.Repository
import com.noktigula.dottchallenge.listeners.UserLocationListener
import com.noktigula.dottchallenge.network.DataLoaderImpl
import com.noktigula.dottchallenge.network.RetrofitInstance
import com.noktigula.dottchallenge.viewmodels.MapViewModel
import com.noktigula.dottchallenge.viewmodels.SelectedVenueViewModel

class MapsActivity : AppCompatActivity() {
    val repository by lazy {
        Repository(
            cache = CacheImpl,
            loader = DataLoaderImpl(RetrofitInstance.foursquareApi)
        ) { snippets ->
            mapViewModel.markers.postValue(snippets.map { it.toMapMarker() })
        }
    }

    val mapViewModel : MapViewModel by lazy {
        ViewModelProviders.of(this).get(MapViewModel::class.java)
    }

    val selectedViewModelFactory : SelectedVenueViewModel by lazy {
        ViewModelProviders.of(this).get(SelectedVenueViewModel::class.java)
    }

    private val locationListener : UserLocationListener by lazy {
        UserLocationListener(this){ userLocation ->
            mapViewModel.location.value = LatLng(userLocation.latitude, userLocation.longitude)
        }
    }

    // TODO extract to custom view
    private val bottomSheet by lazy { findViewById<LinearLayout>(R.id.bottom_sheet) }
    private val title by lazy { bottomSheet.findViewById<TextView>(R.id.title) }
    private val address by lazy { bottomSheet.findViewById<TextView>(R.id.address) }
    private val behavior by lazy { BottomSheetBehavior.from(bottomSheet) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)

        RetrofitInstance.initApi(getString(R.string.foursquare_id), getString(R.string.foursquare_secret))

        lifecycle.addObserver(locationListener)

        behavior.state = BottomSheetBehavior.STATE_COLLAPSED

        selectedViewModelFactory.selectedVenue.observe(this, Observer { marker ->
            if (marker != null) {
                if (behavior.state != BottomSheetBehavior.STATE_EXPANDED) {
                    behavior.state = BottomSheetBehavior.STATE_EXPANDED
                }

                title.text = marker.name
                address.text = marker.address
            } else {
                behavior.state = BottomSheetBehavior.STATE_COLLAPSED
            }
        })
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
