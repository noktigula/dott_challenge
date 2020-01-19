package com.noktigula.dottchallenge.model

typealias SearchResults = FoursquareResponse<SearchResponse>

data class FoursquareResponse<T>(
    val meta:FoursquareMeta,
    val response:T
)

data class SearchResponse(
    val venues:List<RestarauntSnippet>
)

data class FoursquareMeta(
    val code:Int,
    val requestId:String
)

data class RestarauntSnippet (
    val id: String,
    val name: String,
    val location: RestarauntLocation,
    val categories: List<RestarauntCategory>
) {
    fun hasValidLocation() = location.lat != null && location.lng != null
}

data class RestarauntLocation(
    val address: String?,
    val crossStreet: String?,
    val city: String?,
    val state: String?,
    val postalCode: String?,
    val country: String?,
    val lat: Double?,
    val lng: Double?,
    val distance: Int?,
    val isFuzzed: Boolean?
)

data class RestarauntCategory(
    val id: String,
    val name: String,
    val pluralName: String,
    val shortName: String,
    val icon: CategoryIcon,
    val primary: Boolean?
)

data class CategoryIcon(
    val prefix: String,
    val suffix: String
)