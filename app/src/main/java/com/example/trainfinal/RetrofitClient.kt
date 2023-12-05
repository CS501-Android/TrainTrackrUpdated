package com.example.trainfinal

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {
    private val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl("http://your-otp-instance.com/otp/routers/default/") // Replace with your OpenTripPlanner instance URL
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val service: OpenTripPlannerService = retrofit.create(OpenTripPlannerService::class.java)
}