package com.noktigula.dottchallenge

import junit.framework.Assert.assertEquals

fun <T> assertListEquals(message:String, expected:List<T>, actual:List<T>) {
    assertEquals(message, expected.size, actual.size)
    for(i in expected.indices) {
        assertEquals(message, expected[i], actual[i])
    }
}