package com.noktigula.dottchallenge

import androidx.lifecycle.MutableLiveData

fun <T> MutableLiveData<T>.default(initialValue:T) : MutableLiveData<T> {
    this.value = initialValue
    return this
}