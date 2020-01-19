package com.noktigula.dottchallenge.data

import android.os.Handler
import android.os.Looper
import androidx.lifecycle.MutableLiveData
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.noktigula.dottchallenge.network.FoursquareApi
import com.noktigula.dottchallenge.default
import com.noktigula.dottchallenge.loge
import com.noktigula.dottchallenge.model.MapMarker
import com.noktigula.dottchallenge.model.RestarauntSnippet
import com.noktigula.dottchallenge.model.SearchResults
import com.noktigula.dottchallenge.simpleString
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.Executors

class Repository(val api: FoursquareApi) {
    private val lock = Any()
    private val cache = HashMap<LatLng, RestarauntSnippet>()

    val markers = MutableLiveData<List<MapMarker>>().default(emptyList())
    private val threadPoolExecutor = Executors.newCachedThreadPool()

    fun updateMarkersAsync(bounds:LatLngBounds) {
        threadPoolExecutor.submit {
            updateLiveData(cachedSnippets(bounds))
            newSearch(bounds)
        }
    }

    private fun cachedSnippets(bounds:LatLngBounds): List<MapMarker> {
        return synchronized(lock) {
            cache.filter { bounds.contains(it.key) }
                .values
                .toList()
                .map { it.toMapMarker() }
        }
    }

    private fun newSearch(bounds: LatLngBounds) {
        val call =
            api.searchRestaraunts(bounds.southwest.simpleString(), bounds.northeast.simpleString())

        call.enqueue(object : Callback<SearchResults> {
            override fun onFailure(call: Call<SearchResults>, t: Throwable) {
                loge("CALL FAILED BECAUSE ${t.message}")
            }

            override fun onResponse(
                call: Call<SearchResults>,
                response: Response<SearchResults>
            ) {
                if (!response.isSuccessful) {
                    loge("Fail ${response.code()}")
                }

                val venues = response.body()?.response?.venues ?: return

                val validVenues = venues.filter { it.hasValidLocation() }
                synchronized(lock) {
                    validVenues.forEach {
                        cache[LatLng(it.location.lat!!, it.location.lng!!)] = it
                    }
                }
                updateLiveData(cachedSnippets(bounds))
            }
        })
    }

    private fun updateLiveData(data:List<MapMarker>) {
        markers.postValue(data)
    }
}

private fun RestarauntSnippet.toMapMarker() : MapMarker {
    return MapMarker(LatLng(this.location.lat!!, this.location.lng!!), this.name, this.location.address ?: "")
}