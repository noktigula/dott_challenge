package com.noktigula.dottchallenge.data

import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.nhaarman.mockitokotlin2.*
import com.noktigula.dottchallenge.assertListEquals
import com.noktigula.dottchallenge.createSnippets
import com.noktigula.dottchallenge.model.Snippet
import com.noktigula.dottchallenge.network.DataLoader
import junit.framework.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import java.util.concurrent.Executor

class RepositoryTest {

    private val cachedSnippets = createSnippets("cached", 5)
    private val networkSnippets = createSnippets("network", 5)
    private val dummyBounds = LatLngBounds(LatLng(0.0, 0.0), LatLng(5.0, 5.0))

    lateinit var mockCache: Cache<List<Snippet>>
    lateinit var mockCallback: (List<Snippet>)->Unit
    lateinit var mockLoader: DataLoader<LatLngBounds, List<Snippet>>

    @Before
    fun setUp() {
        mockCache = mock {
            on { get() } doReturn cachedSnippets
        }

        mockCallback = fun (input: List<Snippet>) {
            mockCache.update(input)
        }

        mockLoader = mock {
            on {load(any(), any())} doAnswer {
                mockCallback(networkSnippets)
            }
        }
    }

    @Test
    fun updateMarkersAsync() {
        var updates = 0
        val repository = Repository(
            cache = mockCache,
            loader = mockLoader,
            threadPoolExecutor = Executor { p0 -> p0.run() }
        ) {
            when (updates) {
                0 -> {
                    assertListEquals("First update should be from cache", cachedSnippets, it)
                    updates++
                }
                1 -> {
                    assertListEquals(
                        message = "Second update should be both cached and network",
                        expected = listOf(*cachedSnippets.toTypedArray(), *networkSnippets.toTypedArray()),
                        actual = it
                    )
                    updates++
                }
                else -> assertTrue(false)
            }
        }

        repository.updateMarkersAsync(dummyBounds)

        verify(mockCache).update( check {
            assertListEquals("Cache update fail: updated not with network data", networkSnippets, it)
        })
    }
}