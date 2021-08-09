package com.example.calculgraph.helpers

import android.content.Context
import com.example.calculgraph.R
import com.example.calculgraph.activity.AnyActivity.Companion.settings


object LanguageHelper {
    fun String.themeTranslation(context: Context): String = if (settings.language == "English") this
    else when(this) {
        "Standard"  -> context.getString(R.string.theme_standard)
        "Other"     -> context.getString(R.string.theme_other)
        else        -> throw error("topicTranslation error")
    }

    fun String.themeUnTranslation(context: Context): String = if (settings.language == "English") this
    else when(this) {
        context.getString(R.string.theme_standard)  -> "Standard"
        context.getString(R.string.theme_other)     -> "Other"
        else                                        -> throw error("topicUnTranslation error")
    }

    fun String.computabilityTranslation(context: Context): String = if (settings.language == "English") this
    else when(this) {
        "EASY"      -> context.getString(R.string.easy)
        "MEDIUM"    -> context.getString(R.string.medium)
        "HARD"      -> context.getString(R.string.hard)
        "INSANE"    -> context.getString(R.string.insane)
        else        -> throw error("computabilityTranslation error")
    }

    fun String.computabilityUnTranslation(context: Context): String = if (settings.language == "English") this
    else when(this) {
        context.getString(R.string.easy)    -> "EASY"
        context.getString(R.string.medium)  -> "MEDIUM"
        context.getString(R.string.hard)    -> "HARD"
        context.getString(R.string.insane)  -> "INSANE"
        else                                -> throw error("computabilityUnTranslation error")
    }

    fun String.modeTranslation(context: Context): String = if (settings.language == "English") this
    else when(this) {
        "standard"  -> context.getString(R.string.standard)
        "set"       -> context.getString(R.string.set)
        "max"       -> context.getString(R.string.max)
        else        -> throw error("modeTranslation error")
    }
}