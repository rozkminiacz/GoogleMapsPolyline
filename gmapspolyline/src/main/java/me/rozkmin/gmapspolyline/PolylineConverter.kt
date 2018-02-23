package me.rozkmin.gmapspolyline

import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.PolylineOptions

/**
 * Created by jaroslawmichalik on 23.02.2018
 */
class PolylineConverter : Converter<GeocodedResponse?, List<PolylineOptions>> {
    override fun convert(from: GeocodedResponse?): List<PolylineOptions> {
        val polylineMutableList = mutableListOf<PolylineOptions>()
        from?.apply {
            routes.forEach {
                it.legs.forEach {
                    polylineMutableList.addAll(it.steps.map {
                        PolylineOptions()
                                .add(LatLng(it.start.lat, it.start.lng))
                                .add(LatLng(it.end.lat, it.end.lng))
                    })
                }
            }
        }
        return polylineMutableList.toList()
    }
}