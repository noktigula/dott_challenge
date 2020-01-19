package com.noktigula.dottchallenge.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.noktigula.dottchallenge.default
import com.noktigula.dottchallenge.model.MapMarker

class SelectedVenueViewModel : ViewModel() {
    val selectedVenue = MutableLiveData<MapMarker?>().default(null)
}