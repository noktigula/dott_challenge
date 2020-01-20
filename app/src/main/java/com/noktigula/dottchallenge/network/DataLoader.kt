package com.noktigula.dottchallenge.network

import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.noktigula.dottchallenge.loge
import com.noktigula.dottchallenge.model.SearchResults
import com.noktigula.dottchallenge.model.Snippet
import com.noktigula.dottchallenge.simpleString
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

interface DataLoader<P, T> {
    fun load(params: P, callback:(T)->Unit)
}

class DataLoaderImpl (
    private val api: FoursquareApi
) : DataLoader<LatLngBounds, List<Snippet>> {
    override fun load(params: LatLngBounds, callback: (List<Snippet>) -> Unit) {
        val call =
            api.searchRestaraunts(params.southwest.simpleString(), params.northeast.simpleString())
        loge("Execute call")
        call.enqueue(object : Callback<SearchResults> {
            override fun onFailure(call: Call<SearchResults>, t: Throwable) {
                loge("CALL FAILED BECAUSE ${t.message}")
            }

            override fun onResponse(
                call: Call<SearchResults>,
                response: Response<SearchResults>
            ) {
                if (!response.isSuccessful) {
                    loge("Fail ${response.code()}")
                }

                val venues = response.body()?.response?.venues ?: return

                callback(
                    venues
                        .filter { it.hasValidLocation() }
                        .map {
                            Snippet(
                                id = it.id,
                                name = it.name,
                                location = LatLng(it.location.lat!!, it.location.lng!!),
                                address = it.location.address ?: ""
                            )
                        }
                )
            }
        })
    }
}
