package com.example.calculgraph.helpers

import android.content.Context
import com.example.calculgraph.R
import com.example.calculgraph.activity.AnyActivity.Companion.settings


object LanguageHelper {
    fun String.topicTranslation(): String = if (settings.language == "English") this
        else when(this) {
            "Standard"  -> "Стандарт"
            "Other"     -> "Другая"
            else        -> throw error("topicTranslation error")
        }

    fun String.topicUnTranslation(): String = if (settings.language == "English") this
    else when(this) {
        "Стандарт"   -> "Standard"
        "Другая"        -> "Other"
        else            -> throw error("topicUnTranslation error")
    }

    fun String.computabilityTranslation(): String = if (settings.language == "English") this
    else when(this) {
        "EASY"      -> "ПРОСТО"
        "MEDIUM"    -> "СРЕДНЕ"
        "HARD"      -> "СЛОЖНО"
        "INSANE"    -> "БУЗУМНО"
        else        -> throw error("computabilityTranslation error")
    }

    fun String.computabilityUnTranslation(): String = if (settings.language == "English") this
    else when(this) {
        "ПРОСТО"    -> "EASY"
        "СРЕДНЕ"    -> "MEDIUM"
        "СЛОЖНО"    -> "HARD"
        "БУЗУМНО"   -> "INSANE"
        else        -> throw error("computabilityUnTranslation error")
    }

//    TODO("do other like this")
    fun String.modeTranslation(context: Context): String = if (settings.language == "English") this
    else when(this) {
        "standard"  -> context.getString(R.string.standard)
        "set"       -> context.getString(R.string.set)
        "max"       -> context.getString(R.string.max)
        else        -> throw error("modeTranslation error")
    }
}