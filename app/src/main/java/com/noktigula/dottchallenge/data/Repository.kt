package com.noktigula.dottchallenge.data

import androidx.lifecycle.MutableLiveData
import com.google.android.gms.maps.model.LatLngBounds
import com.noktigula.dottchallenge.default
import com.noktigula.dottchallenge.loge
import com.noktigula.dottchallenge.model.MapMarker
import com.noktigula.dottchallenge.model.Snippet
import com.noktigula.dottchallenge.network.DataLoader
import java.util.concurrent.Executors

class Repository (
    private val cache: Cache<List<Snippet>>,
    private val loader: DataLoader<LatLngBounds, List<Snippet>>,
    private val callback: (List<MapMarker>) -> Unit
) {
    private val lock = Any()
    private val threadPoolExecutor = Executors.newCachedThreadPool()

    fun updateMarkersAsync(bounds:LatLngBounds) {
        loge("updateMarkersAsync")
        threadPoolExecutor.submit {
            val data = cachedSnippets(bounds)
            callback(data)
            newSearch(bounds)
        }
    }

    private fun cachedSnippets(bounds:LatLngBounds): List<MapMarker> {
        loge("cachedSnippets")
        return synchronized(lock) {
            cache.get()
                .filter { bounds.contains(it.location) }
                .map { it.toMapMarker() }
        }
    }

    private fun newSearch(bounds: LatLngBounds) {
        loge("newSearch")
        loader.load(bounds) { newSnippets ->
            synchronized(lock) {
                cache.update(newSnippets)
            }

            callback(cachedSnippets(bounds))
        }
    }
}

private fun Snippet.toMapMarker() : MapMarker {
    return MapMarker(this.location, this.name, this.address)
}