package com.example.calculgraph

import com.example.calculgraph.Operation.*

enum class Operation {
    PLUS,
    MINUS,
    MULTIPLICATION,
    DIVISION,
    DEGREE,
    ROOT,
    NONE
}

fun Operation.reverse() = when(this) {
    PLUS            -> MINUS
    MINUS           -> PLUS
    MULTIPLICATION  -> DIVISION
    DIVISION        -> MULTIPLICATION
    DEGREE          -> ROOT
    ROOT            -> DEGREE
    NONE            -> NONE
}

fun Operation.opToString() = when(this) {
    PLUS -> "+"
    MINUS -> "-"
    MULTIPLICATION -> "*"
    DIVISION -> "/"
    DEGREE -> "x"
    ROOT -> "\u221a¯"
    NONE -> ""
}