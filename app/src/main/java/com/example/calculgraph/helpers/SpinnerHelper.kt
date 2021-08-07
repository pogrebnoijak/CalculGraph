package com.example.calculgraph.helpers

import android.widget.Spinner

object SpinnerHelper {
    fun getIndexByName(spin: Spinner, name: Any): Int {
        (0 until spin.count).forEach { i ->
            if (spin.getItemAtPosition(i) == name) return i
        }
        throw error("spinner error")
    }
}