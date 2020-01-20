package com.noktigula.dottchallenge.data

import com.google.android.gms.maps.model.LatLng
import com.noktigula.dottchallenge.model.Snippet

interface Cache<T> {
    fun update(data:T)
    fun get():T
}

class CacheImpl : Cache<List<Snippet>> {
    private val cache = HashMap<LatLng, Snippet>()

    override fun update(data:List<Snippet>) {
        data.forEach { cache[it.location] = it }
    }

    override fun get(): List<Snippet> {
       return cache.values.toList()
    }
}