package com.example.calculgraph.enums
import com.example.calculgraph.enums.Theme.*

enum class Theme {
    STANDARD,
    OTHER
}

fun Theme.thToString() = when(this) {
    STANDARD -> "Standard"
    OTHER -> "Other"
}

fun String.toTheme(): Theme = when(this) {
    "Standard"  -> STANDARD
    "Other"     -> OTHER
    else        -> throw error("wrong topic")
}

fun topicValues() = Theme.values().toList().map { it.thToString() }