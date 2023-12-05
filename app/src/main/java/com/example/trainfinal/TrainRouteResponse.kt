package com.example.trainfinal

import com.google.gson.annotations.SerializedName

data class TrainRouteResponse(
    @SerializedName("plan") val plan: Plan?
)

data class Plan(
    @SerializedName("itineraries") val itineraries: List<Itinerary>?
)

data class Itinerary(
    @SerializedName("duration") val duration: Long?,
    @SerializedName("startTime") val startTime: Long?,
    @SerializedName("endTime") val endTime: Long?,
    @SerializedName("legs") val legs: List<Leg>?
)

data class Leg(
    @SerializedName("startTime") val startTime: Long?,
    @SerializedName("endTime") val endTime: Long?,
    @SerializedName("mode") val mode: String?,
    @SerializedName("route") val route: String?,
    @SerializedName("distance") val distance: Double?,
    @SerializedName("from") val from: Place?,
    @SerializedName("to") val to: Place?,
    @SerializedName("legGeometry") val legGeometry: LegGeometry?
)

data class Place(
    @SerializedName("name") val name: String?,
    @SerializedName("lat") val lat: Double?,
    @SerializedName("lon") val lon: Double?
)

data class LegGeometry(
    @SerializedName("points") val points: String?
)
