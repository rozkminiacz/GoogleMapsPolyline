package me.rozkmin.gmapspolyline

/**
 * Created by jaroslawmichalik on 23.02.2018
 */
class TransitOptions(
        val sensor: Boolean = false,
        val mode: MODE = MODE.DRIVING,
        private val whatToAvoidArray: Array<AVOID> = arrayOf()) {

    fun hasWhatToAvoid() = whatToAvoidArray.isNotEmpty()

    fun whatToAvoid(): String {
        val stringBuilder = StringBuilder()
        for (a in whatToAvoidArray){
            stringBuilder.append(a.thing)
            stringBuilder.append("|")
        }
        if(stringBuilder.isNotEmpty()) stringBuilder.deleteCharAt(stringBuilder.lastIndex)

        return stringBuilder.toString()
    }
}

enum class MODE(val type: String) {
    WALKING("walking"),
    DRIVING("driving"),
    BICYCLING("bicycling"),
    TRANSIT("transit")
}

enum class AVOID(val thing: String) {
    TOLLS("tolls"),
    HIGHWAYS("highways"),
    FERRIES("ferries")
}