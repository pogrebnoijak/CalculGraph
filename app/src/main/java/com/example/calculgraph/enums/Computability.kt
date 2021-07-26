package com.example.calculgraph.enums

enum class Computability {
    EASY,
    MEDIUM,
    HARD,
    INSANE
}

fun String.toComputability(): Computability = when(this) {
    "EASY"      -> Computability.EASY
    "MEDIUM"    -> Computability.MEDIUM
    "HARD"      -> Computability.HARD
    "INSANE"    -> Computability.INSANE
    else -> throw error("wrong computability")
}