package com.example.trainfinal

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherService {

    @GET("data/2.5/weather?appid=3cca344f938636fd9b21d4344ef5da63")
    fun reverse(@Query("lon") long: Double, @Query("lat") lat: Double): Call<WeatherResponse>
}