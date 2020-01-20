package com.noktigula.dottchallenge.data

import com.google.android.gms.maps.model.LatLngBounds
import com.noktigula.dottchallenge.model.MapMarker
import com.noktigula.dottchallenge.model.Snippet
import com.noktigula.dottchallenge.network.DataLoader
import java.util.concurrent.Executor
import java.util.concurrent.Executors

class Repository (
    private val cache: Cache<List<Snippet>>,
    private val loader: DataLoader<LatLngBounds, List<Snippet>>,
    private val threadPoolExecutor: Executor = Executors.newCachedThreadPool(),
    private val callback: (List<MapMarker>) -> Unit
) {
    private val lock = Any()

    fun updateMarkersAsync(bounds:LatLngBounds) {
        threadPoolExecutor.execute {
            callback(cachedSnippets(bounds))
            newSearch(bounds)
        }
    }

    private fun cachedSnippets(bounds:LatLngBounds): List<MapMarker> {
        return synchronized(lock) {
            cache.get()
                .filter { bounds.contains(it.location) }
                .map { it.toMapMarker() }
        }
    }

    private fun newSearch(bounds: LatLngBounds) {
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