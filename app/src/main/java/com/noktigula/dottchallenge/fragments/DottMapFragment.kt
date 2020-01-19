package com.noktigula.dottchallenge.fragments

import android.location.Location
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.noktigula.dottchallenge.R
import com.noktigula.dottchallenge.api.FoursquareApi
import com.noktigula.dottchallenge.loge
import com.noktigula.dottchallenge.model.RestarauntSnippet
import com.noktigula.dottchallenge.simpleString
import com.noktigula.dottchallenge.viewmodels.LocationViewModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import com.facebook.stetho.okhttp3.StethoInterceptor
import okhttp3.Interceptor
import okhttp3.OkHttpClient

private const val DEFAULT_ZOOM = 15f

class DottMapFragment : Fragment(), OnMapReadyCallback {
    private val mapView: MapView by lazy { view!!.findViewById<MapView>(R.id.map_view) }
    private val locationViewModel : LocationViewModel by lazy {
        ViewModelProviders.of(activity!!)[LocationViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_dott_map, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        mapView.onCreate(savedInstanceState)
        mapView.getMapAsync(this)
    }

    override fun onResume() {
        super.onResume()
        mapView.onResume()
    }

    override fun onMapReady(inMap: GoogleMap?) {
        val map = inMap ?: return

        map.moveCamera(CameraUpdateFactory.zoomTo(DEFAULT_ZOOM))
        locationViewModel.location.observe(this, Observer<LatLng> { userLocation ->
            map.moveCamera(CameraUpdateFactory.newLatLng(userLocation))
            loadRestaurants(map)
        })

        map.setOnCameraMoveListener { loadRestaurants(map) }
    }

    fun loadRestaurants(map:GoogleMap) {
        loge("Loading restaraunts")

        val okHttpClient = OkHttpClient.Builder()
            .addNetworkInterceptor(StethoInterceptor())
            .addInterceptor { chain ->
                val original = chain.request()
                val originalHttpUrl = original.url()

                val url = originalHttpUrl.newBuilder()
                    .addQueryParameter("client_id", getString(R.string.foursquare_id))
                    .addQueryParameter("client_secret", getString(R.string.foursquare_secret))
                    .addQueryParameter("v", "20200119")
                    .build()

                // Request customization: add request headers
                val requestBuilder = original.newBuilder()
                    .url(url)

                val request = requestBuilder.build()
                chain.proceed(request)

            }
            .build()

        val retrofit = Retrofit.Builder()
            .baseUrl("https://api.foursquare.com/v2/")
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val api = retrofit.create(FoursquareApi::class.java)
        val bounds = map.projection.visibleRegion.latLngBounds
        val call = api.searchRestaraunts(bounds.southwest.simpleString(), bounds.northeast.simpleString())
        loge("Enqueuing call")
        call.enqueue(object:Callback<List<RestarauntSnippet>> {
            override fun onFailure(call: Call<List<RestarauntSnippet>>, t: Throwable) {
                loge("CALL FAILED BECAUSE ${t.message}")
            }

            override fun onResponse(
                call: Call<List<RestarauntSnippet>>,
                response: Response<List<RestarauntSnippet>>
            ) {
                loge("onResponse")
                if (response.isSuccessful) {
                    loge("Success ${response.code()}")
                } else {
                    loge("Fail ${response.code()}")
                }
            }
        })
    }
}