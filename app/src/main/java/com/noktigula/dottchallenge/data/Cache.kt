package com.noktigula.dottchallenge.data

import com.noktigula.dottchallenge.model.Snippet

interface Cache<T> {
    fun update(data:T)
    fun get():T
}

class CacheImpl : Cache<List<Snippet>> {
    private val cache = mutableListOf<Snippet>()

    override fun update(data:List<Snippet>) {
        cache.addAll(data)
    }

    override fun get(): List<Snippet> {
       return cache
    }
}