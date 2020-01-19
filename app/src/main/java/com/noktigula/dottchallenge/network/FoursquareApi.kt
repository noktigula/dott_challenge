package com.noktigula.dottchallenge.network

import com.noktigula.dottchallenge.model.SearchResults
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

private const val CATEGORY_FOOD = "4d4b7105d754a06374d81259"
private const val DEFAULT_INTENT = "browse"

interface FoursquareApi {
    @GET("venues/search")
    fun searchRestaraunts(
        @Query("sw") sw:String,
        @Query("ne") ne:String,
        @Query("intent") intent:String= DEFAULT_INTENT,
        @Query("categoryId") categoryId:String= CATEGORY_FOOD): Call<SearchResults>
}