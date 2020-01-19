package com.noktigula.dottchallenge.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.noktigula.dottchallenge.data.Repository

class MapViewModelFactory (
    private val repository: Repository
) : ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return MapViewModel(repository = repository) as T
    }
}