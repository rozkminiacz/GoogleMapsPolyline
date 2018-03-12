package me.rozkmin.gmapspolyline

import android.util.Log
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.PolylineOptions
import kotlinx.coroutines.experimental.launch
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import ru.gildor.coroutines.retrofit.awaitResponse

/**
 * Created by jaroslawmichalik on 23.02.2018
 */
class DirectionsApiClient(
        private val apiKey: String,
        logHttp: Boolean = false,
        private val polylineConverter: Converter<GeocodedResponse?, List<PolylineOptions>> = PolylineConverter()) {

    companion object {
        val TAG: String = DirectionsApiClient::class.java.canonicalName
    }

    private val googleMapsApi: DirectionsApi

    init {
        val client = OkHttpClient.Builder()
                .apply {
                    if (logHttp) {
                        val httpLoggingInterceptor = HttpLoggingInterceptor(HttpLoggingInterceptor.Logger { p0 -> Log.d(TAG, p0) })
                        httpLoggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
                        addInterceptor(httpLoggingInterceptor)
                    }
                }
                .build()

        val retrofit = Retrofit.Builder()
                .baseUrl("https://maps.googleapis.com/maps/api/directions/")
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build()

        googleMapsApi = retrofit.create(DirectionsApi::class.java)
    }

    fun getRoutePolylines(origin: LatLng, dest: LatLng,
                          options: TransitOptions = TransitOptions(),
                          status: (STATE, STATUS) -> Unit = { _, _ -> },
                          polylines: (List<PolylineOptions>) -> Unit) {

        status(STATE.START, STATUS.NONE)

        launch {
            status(STATE.LOADING, STATUS.NONE)
            val response = googleMapsApi.getGeocodeDirectionsResponse(getUrl(origin, dest, options)).awaitResponse()

            if (response.isSuccessful) {
                polylines(polylineConverter.convert(response.body()))
                status(STATE.END, STATUS.SUCCESS)
            } else {
                status(STATE.END, STATUS.ERROR)
            }
        }
    }

    fun getDirectionSuggestions(
            origin: LatLng, dest: LatLng,
            options: TransitOptions = TransitOptions(),
            status: (STATE, STATUS) -> Unit = { _, _ -> },
            directions: (GeocodedResponse) -> Unit
    ){
        status(STATE.START, STATUS.NONE)

        launch {
            status(STATE.LOADING, STATUS.NONE)
            val response = googleMapsApi.getGeocodeDirectionsResponse(getUrl(origin, dest, options)).awaitResponse()

            if (response.isSuccessful) {
                response.body()?.apply {
                    directions.invoke(this)
                }
                status(STATE.END, STATUS.SUCCESS)
            } else {
                status(STATE.END, STATUS.ERROR)
            }
        }
    }

    private fun getUrl(origin: LatLng, dest: LatLng, transitOptions: TransitOptions): String = StringBuilder().apply {
        append("json?")
        append("origin=" + origin.latitude + "," + origin.longitude)
        append("&destination=" + dest.latitude + "," + dest.longitude)
        append("&sensor=${transitOptions.sensor}")
        append("&mode=${transitOptions.mode.type}")

        if (transitOptions.hasWhatToAvoid()) {
            append("&avoid=${transitOptions.whatToAvoid()}")
        }
        append("&key=$apiKey")
    }.toString()

}