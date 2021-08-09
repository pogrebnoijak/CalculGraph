package com.example.calculgraph.enums
import android.content.Context
import com.example.calculgraph.R
import com.example.calculgraph.enums.Theme.*

enum class Theme {
    STANDARD,
    OTHER
}

fun Theme.thToString() = when(this) {
    STANDARD    -> "Standard"
    OTHER       -> "Other"
}

fun String.toTheme(): Theme = when(this) {
    "Standard"  -> STANDARD
    "Other"     -> OTHER
    else        -> throw error("wrong topic")
}

fun topicValues() = Theme.values().toList().map { it.thToString() }

fun Theme.getColor(context: Context): Int = when(this) {
    STANDARD    -> context.getColor(R.color.white)
    OTHER       -> context.getColor(R.color.black)
}

fun Theme.getArcColor(context: Context): Int = when(this) {
    STANDARD    -> context.getColor(R.color.arcColor1)
    OTHER       -> context.getColor(R.color.arcColor2)
}