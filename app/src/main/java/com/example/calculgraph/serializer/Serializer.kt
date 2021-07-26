package com.example.calculgraph.serializer

import com.google.gson.Gson

object Serializer {
    private val gson = Gson()

    internal fun listToBytes(list: List<Any>): ByteArray =
        gson.toJson(list).toByteArray()

    internal fun bytesToList(byteArray: ByteArray, type: java.lang.reflect.Type): List<Any> =
        gson.fromJson(byteArray.toString(), type)
}