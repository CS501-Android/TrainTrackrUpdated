package com.example.trainfinal

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClientInstance {
    private const val BASE_URL = "https://api.sncf.com/v1/"

    val retrofitInstance: Retrofit
        get() = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(OkHttpClient.Builder().addInterceptor { chain ->
                val original = chain.request()
                val request = original.newBuilder()
                    .header("Authorization", "b12f312b-64fe-4ea2-adb8-6aa05b92815f")
                    .method(original.method, original.body)
                    .build()
                chain.proceed(request)
            }.build())
            .build()
}

