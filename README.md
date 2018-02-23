# Google Maps Directions API - polyline drawer helper

## What is it?
It's small library written in Kotlin to utilize Google Directions API calls
and convert response to PolylineOptions, ready to add to GoogleMap.

## Why should I use it?
Well, you don't have to. But if you already reading this - 
consider using it if you don't want to parse nested jsons by yourself.

## How do I use it?

### Add dependency

That's simple. First - add dependency to your build.gradle:

```groovy
allprojects {
	repositories {
		...
		maven { url 'https://jitpack.io' }
	}
}
```

```groovy
implementation : "com.github.rozkminiacz:GoogleMapsPolyline:1.0"	
```

You must also provide google maps core dependency:

```groovy
implementation : "com.google.android.gms:play-services-maps:11.8.0"
```

### Create instance of DirectionsApiClient:

```kotlin
class AnotherBoringMapActivity : Activity(){
    fun createClient() {
        directionsApiClient = DirectionsApiClient(
                    apiKey = getString(R.string.google_directions_key))
    }
}
```

### Request route between points
```kotlin
class AnotherBoringMapActivity : Activity(){
    fun requestRoute(from : LatLng, to: LatLng){
       directionsApiClient.getRoutePolylines(origin = from, dest = to) {
            //do something with list of PolylineOptions
            //remember, that you are not on main thread here
       }
    }
}
```

### Customize request!
You can add some customization to Directions API request, just look at TransitOptions class. 
You can specify transit mode, and what to avoid during route planning.

### Write your own parser!
Yeah! That's possible! Just implement **Converter<GeocodedResponse?, List<PolylineOptions>>** and you are ready to go!

```kotlin
directionsApiClient = DirectionsApiClient(
    apiKey = getString(R.string.google_directions_key),
    polylineConverter = myConverter)
```

### I still don't know hot to use it
Look at example in this repo, in **app** directory.

## I want to improve this library!
Sure! Pull requests are welcome! Any reasonable extension will be added.

## Tools used:
* [Google Maps API](https://developers.google.com/maps/)
* [Kotlin Coroutines](https://kotlinlang.org/docs/reference/coroutines.html)
* [OkHttp](https://github.com/square/okhttp)
* [Retrofit](http://square.github.io/retrofit/)
* [Retrofit coroutines extensions](https://github.com/gildor/kotlin-coroutines-retrofit)
