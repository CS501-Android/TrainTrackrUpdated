package com.example.trainfinal

data class SNCFResponse(
    val journeys: List<Journey>,
)

data class Journey(
    val duration: Int,
    val nb_transfers: Int,
    val departure_date_time: String,
    val arrival_date_time: String,
    val sections: List<Section>,
)

data class Section(
    val id: String,
    val duration: Int,
    val departure_date_time: String,
    val arrival_date_time: String,
    val from: Location,
    val to: Location,
    val mode: String,
    val type: String,
)

data class Location(
    val id: String,
    val name: String,
    val quality: Int,
    val administrative_region: AdministrativeRegion?,
    val stop_point: StopPoint?,
    val embedded_type: String
)

data class AdministrativeRegion(
    val id: String,
    val name: String,
    val level: Int,
    val zip_code: String,
    val label: String,
    val insee: String,
    val coord: Coordinate
)

data class StopPoint(
    val id: String,
    val name: String,
    val label: String,
    val coord: Coordinate,
)

data class Coordinates(
    val lon: Double,
    val lat: Double
)