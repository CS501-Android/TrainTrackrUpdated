package com.example.trainfinal

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface SNCF {
    @GET("coverage/sncf/commercial_modes")
    fun getTransportModes(): Call<SNCFResponse>

    @GET("coverage/sncf/journeys")
    fun getJourneys(
        @Query("from") from: String,
        @Query("to") to: String,
        @Query("datetime") datetime: String
    ): Call<SNCFResponse>

    @GET("coverage/sncf/stop_areas/stop_area:SNCF:{stopAreaId}/departures")
    fun getNextDepartures(
        @Path("stopAreaId") stopAreaId: String,
        @Query("datetime") datetime: String
    ): Call<SNCFResponse>
}