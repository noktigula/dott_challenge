package com.noktigula.dottchallenge.presenters

import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.doAnswer
import com.nhaarman.mockitokotlin2.mock
import com.noktigula.dottchallenge.data.Repository
import org.junit.Before
import org.junit.Test

import org.junit.Assert.*

// Used to simplify looking for a broken method if test fails
const val ZOOM = "zoom"
const val LOCATION = "location"
const val MARKERS_UPDATE = "markersUpdate"
const val ON_MAP_SCROLL_STOPPED = "onMapScrollStopped"
const val ON_MARKER_CLICKED = "onMarkerClicked"
const val ON_MAP_CLICKED = "onMapClicked"
const val SET_MINIMAL_ZOOM = "setMinimalZoom"

class DottMapPresenterImplTest {

    lateinit var mockMap: Map
    var methodsCalled = mutableMapOf(
        ZOOM to 0,
        LOCATION to 0,
        MARKERS_UPDATE to 0,
        ON_MAP_SCROLL_STOPPED to 0,
        ON_MARKER_CLICKED to 0,
        ON_MAP_CLICKED to 0,
        SET_MINIMAL_ZOOM to 0
    )

    @Before
    fun setUp() {
        mockMap = mock {
            on { zoomTo(any()) } doAnswer {
                methodsCalled[ZOOM] = 1
                Unit
            }

            on { onMapScrollStopped(any()) } doAnswer {
                methodsCalled[ON_MAP_SCROLL_STOPPED] = 1
                Unit
            }

            on { onMarkerClicked(any()) } doAnswer {
                methodsCalled[ON_MARKER_CLICKED] = 1
                Unit
            }

            on { onMapClicked(any()) } doAnswer {
                methodsCalled[ON_MAP_CLICKED] = 1
                Unit
            }

            on { setMinimalZoomLevel(any()) } doAnswer {
                methodsCalled[SET_MINIMAL_ZOOM] = 1
                Unit
            }
        }
    }

    @Test
    fun prepareMap_ensure_init_steps() {
        val presenter = DottMapPresenterImpl(
            onLocation = {},
            onMarkers = {},
            onVenueSelected = {},
            repository = mock()
        )

        presenter.prepareMap(mockMap)

        methodsCalled.forEach {
            if (it.key != LOCATION && it.key != MARKERS_UPDATE) {
                assertNotEquals("${it.key} was not called", 0, it.value)
            }
        }
    }
}