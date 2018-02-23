package me.rozkmin.gmapspolyline

import com.google.gson.annotations.SerializedName
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Url

/**
 * Created by jaroslawmichalik on 23.02.2018
 */
interface DirectionsApi {
    @GET
    fun getGeocodeDirectionsResponse(@Url requestOptions : String) : Call<GeocodedResponse>
}

data class GeocodedResponse(val routes: List<RouteElement> = listOf())

data class RouteElement(val legs: List<Leg>)

data class Leg(val steps: List<Step>)

data class Step(
        @SerializedName("start_location")
        val start: LatLngStep,
        @SerializedName("end_location")
        val end: LatLngStep,
        val polyline: Poly)

data class Poly(val points: String = "")

data class LatLngStep(val lat: Double, val lng: Double)