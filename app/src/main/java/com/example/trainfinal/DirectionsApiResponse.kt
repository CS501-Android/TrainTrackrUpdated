package com.example.trainfinal

data class DirectionsApiResponse(
    val routes: List<Route>,
    val status: String
)

data class GoogleMapsRoute(
    val bounds: Bounds,
    val legs: List<Leg>,
    val overview_polyline: Polyline,
    val summary: String
)

data class Bounds(
    val northeast: Location,
    val southwest: Location
)

data class Leg(
    val distance: Distance,
    val duration: Duration,
    val end_address: String,
    val end_location: Location,
    val start_address: String,
    val start_location: Location,
    val steps: List<Step>,
    val traffic_speed_entry: List<Any>,
    val via_waypoint: List<Any>
)

data class Step(
    val distance: Distance,
    val duration: Duration,
    val end_location: Location,
    val html_instructions: String,
    val polyline: Polyline,
    val start_location: Location,
    val travel_mode: String
)

data class Distance(
    val text: String,
    val value: Int
)

data class Duration(
    val text: String,
    val value: Int
)

data class GoogleMapsLocation(
    val lat: Double,
    val lng: Double
)

data class Polyline(
    val points: String
)
