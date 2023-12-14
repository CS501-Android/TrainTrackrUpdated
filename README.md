# TrainTrackr

## About US
**Why did we choose to create this application**

**Who is our target demographic**


## API Utilize 
- **Google Map API**
Google Map API was utilized to render current user locations. On top of such, it was used to portray potential stops and user-inputted stops within the Google Map.
Given the user input, it'll create routes and stopping points of the Trip detail.

- **Firebase API**
We've utilized the Firebase Email/Password authentication method for our Login. We plan to add another instance with Google or other social media Login as we plan to integrate
them with our application so you can share the information on twitter or Instagram. Firebase was also used as our Database explained later.

- **WeatherAPI**
When in doubt, use Weather API. All jokes aside, with our application, weather API makes a lot of sense. Currently, the way to use weather API is simply just a Retrofit client
being fed LatLong information from Google Map on Idle, updating given the location. In the future implementation, we've plan to utilize it for future planning instead of current.
```Kotlin
// WeatherResponses, required for Retfroit
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
```

- **SNCF (Implementing)**
TBD

- **OpenTripPlanner (Potentially?)**
OpenTripPlanner was technically our original plan, we've implemented Retrofit and specific classes regarding that front to receive the callback.
However, this one API required a server hosted locally or online. This was for a future expansion and not the current expansion.

## Database & Data Structures
**Database**
As mentioned prior, we've decided to utilize Firebase, with more information being displayed here: https://firebase.google.com/docs/database/. The process with this is really simple,
we've used the current Authenticated user's specific UID as our unique pointer creating multiple references a Map value pair in as Real-Time Database is based.
**User class**
```Kotlin
data class User (
    val username: String = "",
    val email: String = "",
    val profileImage: Uri? = null,
    val posts: MutableList<String> = mutableListOf(),
    val followers: MutableList<String> = mutableListOf(),
    val notification: MutableList<String> = mutableListOf()
) {
}
```
**Route class - a key-value pair for posts in User class**
```Kotlin
data class Route(
    val routeId: String = UUID.randomUUID().toString(),
    val routeTitle: String = "TMP",
    val routeDescription: String = "",
    val scene: Uri? = null,
    val rating: Int? = 0,
    val reviewList: MutableList<Review> = mutableListOf(),
    val stops: MutableList<RouteStops> = mutableListOf()
) {
}
```
**RouteStops - stops of the route?**
```Kotlin
data class RouteStops (
    val title: String? = null,
    val lat: Float? = 0.0f,
    val long: Float? = 0.0f,
    val image: Uri? = null
){
}
```

## Future Plans
