package com.noktigula.dottchallenge.data

import com.noktigula.dottchallenge.assertListEquals
import com.noktigula.dottchallenge.createSnippets
import org.junit.Test

import org.junit.Assert.*

class CacheImplTest {

    val snippets = createSnippets("cached", 3)

    @Test
    fun update() {
        val cache = CacheImpl()
        assertEquals(0, cache.get().size)

        for (i in snippets.indices) {
            cache.update(listOf(snippets[i]))
            assertEquals(i+1, cache.get().size)
            assertEquals(snippets[i], cache.get()[i])
        }
    }

    @Test
    fun get() {
        val cache = CacheImpl()

        assertEquals(0, cache.get().size)
        cache.update(snippets)

        assertEquals(snippets.size, cache.get().size)
        assertListEquals("", snippets, cache.get())
    }
}