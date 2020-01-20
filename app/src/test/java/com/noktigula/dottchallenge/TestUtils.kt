package com.noktigula.dottchallenge

import com.google.android.gms.maps.model.LatLng
import com.noktigula.dottchallenge.model.Snippet
import junit.framework.Assert.assertEquals

fun <T> assertListEquals(message:String, expected:List<T>, actual:List<T>) {
    assertEquals(message, expected.size, actual.size)
    for(i in expected.indices) {
        assertEquals(message, expected[i], actual[i])
    }
}

fun createSnippets(prefix:String, count:Int) : List<Snippet> {
    val list = mutableListOf<Snippet>()
    for (i in 0 until count) {
        list.add(
            Snippet(
                id = "${prefix}_${i}_id",
                name = "${prefix}_${i}_name",
                location = LatLng(i.toDouble(), i.toDouble()),
                address = "${prefix}_${i}_address"
            )
        )
    }
    return list
}