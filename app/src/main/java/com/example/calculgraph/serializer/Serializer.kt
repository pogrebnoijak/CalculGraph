package com.example.calculgraph.serializer

import com.google.gson.Gson

object Serializer {
    private val gson = Gson()

    fun listToBytes(list: List<Any>): ByteArray =
        gson.toJson(list).toByteArray()

    fun bytesToList(byteArray: ByteArray, type: java.lang.reflect.Type): List<Any> =
        gson.fromJson(String(byteArray), type)
}