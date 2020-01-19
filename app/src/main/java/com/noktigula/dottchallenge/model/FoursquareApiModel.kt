package com.noktigula.dottchallenge.model

data class RestarauntSnippet (
    val id: String,
    val name: String,
    val location: RestarauntLocation,
    val categories: List<RestarauntCategory>
)

data class RestarauntLocation(
    val address: String?,
    val crossStreet: String?,
    val city: String?,
    val state: String?,
    val postalCode: String?,
    val country: String?,
    val lat: Double?,
    val lon: Double?,
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