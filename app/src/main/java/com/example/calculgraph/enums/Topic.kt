package com.example.calculgraph.enums
import com.example.calculgraph.enums.Topic.*

enum class Topic {
    STANDARD,
    OTHER
}

fun Topic.topToString() = when(this) {
    STANDARD -> "Standard"
    OTHER -> "Other"
}

fun String.toTopic(): Topic = when(this) {
    "Standard"  -> STANDARD
    "Other"     -> OTHER
    else        -> throw error("wrong topic")
}

fun topicValues() = Topic.values().toList().map { it.topToString() }.toTypedArray()