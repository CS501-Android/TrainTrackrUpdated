package com.example.trainfinal

import com.google.gson.annotations.SerializedName

data class WeatherResponse (
    @SerializedName("coordinate") val coordinate: Coordinate,
    @SerializedName("sys") val sys: Sys,
    @SerializedName("weather") val weather: ArrayList<Weather>,
    @SerializedName("main") val main: Main,
    @SerializedName("wind") val wind: Wind,
    @SerializedName("rain") val rain: Rain,
    @SerializedName("clouds") val clouds: Cloud,
    @SerializedName("dt") val dt: Float,
    @SerializedName("id") val id: Int,
    @SerializedName("name") val name: String,
    @SerializedName("cod") val cod: Float,
)

class Weather (
    @SerializedName("id") val id: Int,
    @SerializedName("main") val main: String,
    @SerializedName("description") val description: String,
    @SerializedName("icon") val icon: String
)
data class Coordinate (
    @SerializedName("lon") val lon: Float,
    @SerializedName("lat") val lat: Float,
)
data class Sys (
    @SerializedName("country") val country: String,
    @SerializedName("sunrise") val sunrise: Long,
    @SerializedName("sunset") val sunset: Long,
)
data class Main (
    @SerializedName("temp") val temp: Float,
    @SerializedName("humidity") val humidity: Float,
    @SerializedName("pressure") val pressure: Float,
    @SerializedName("tempMin") val tempMin: Float,
    @SerializedName("tempMax") val tempMax: Float,
)
data class Wind (
    @SerializedName("speed") val speed: Float,
    @SerializedName("deg") val deg: Float,
)
data class Rain (
    @SerializedName("h3") val h3: Float,
)
data class Cloud (
    @SerializedName("all") val all: Float,
)