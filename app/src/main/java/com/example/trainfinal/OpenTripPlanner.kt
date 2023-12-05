package com.example.trainfinal

import com.example.trainfinal.TrainRouteResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface OpenTripPlannerService {
    @GET("plan")
    suspend fun getTrainRoutes(
        @Query("fromPlace") fromPlace: String,
        @Query("toPlace") toPlace: String,
        @Query("time") time: String, // format HH:mm:ss
        @Query("date") date: String, // format YYYY-MM-DD
        @Query("mode") mode: String = "TRANSIT,TRAINISH",
        @Query("maxWalkDistance") maxWalkDistance: Double = 2000.0
    ): Response<TrainRouteResponse>
}
