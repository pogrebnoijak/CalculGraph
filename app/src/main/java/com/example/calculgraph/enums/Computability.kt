package com.example.calculgraph.enums

import com.example.calculgraph.enums.Computability.*

enum class Computability {
    EASY,
    MEDIUM,
    HARD,
    INSANE
}

fun String.toComputability(): Computability = when(this) {
    "EASY"      -> EASY
    "MEDIUM"    -> MEDIUM
    "HARD"      -> HARD
    "INSANE"    -> INSANE
    else        -> throw error("wrong computability")
}