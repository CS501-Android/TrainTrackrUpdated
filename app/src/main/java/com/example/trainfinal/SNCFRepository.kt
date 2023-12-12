package com.example.trainfinal

import okhttp3.OkHttpClient
import okhttp3.Interceptor
import okhttp3.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class SNCFRepository {
    val sncfInterceptor = SNCFInterceptor("b12f312b-64fe-4ea2-adb8-6aa05b92815f")

    val okHttpClient = OkHttpClient.Builder()
        .addInterceptor(sncfInterceptor)
        .build()

    val retrofit = Retrofit.Builder()
        .baseUrl("https://api.sncf.com/v1/")
        .client(okHttpClient)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

}