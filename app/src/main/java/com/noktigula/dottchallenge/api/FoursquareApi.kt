package com.noktigula.dottchallenge.api

import com.noktigula.dottchallenge.model.RestarauntSnippet
import retrofit2.Call
import retrofit2.http.GET

private const val CATEGORY_FOOD = "4d4b7105d754a06374d81259"
private const val DEFAULT_INTENT = "browse"

interface FoursquareApi {
    @GET("https://api.foursquare.com/v2/venues/search")
    fun searchRestaraunts(
        sw:Double,
        ne: Double,
        intent:String= DEFAULT_INTENT,
        categoryId:String= CATEGORY_FOOD): Call<List<RestarauntSnippet>>
}