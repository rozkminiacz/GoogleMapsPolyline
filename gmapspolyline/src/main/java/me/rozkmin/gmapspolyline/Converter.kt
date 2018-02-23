package me.rozkmin.gmapspolyline

/**
 * Created by jaroslawmichalik on 23.02.2018
 */
interface Converter<in FROM, out TO> {
    fun convert(from: FROM) : TO
}