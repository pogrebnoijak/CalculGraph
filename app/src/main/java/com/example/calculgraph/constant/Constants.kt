package com.example.calculgraph.constant

import com.example.calculgraph.enums.Computability.*
import com.example.calculgraph.enums.Operation.*
import kotlin.math.PI

// const
const val DRAWING = 10L         // ms
const val LAYOUT_CONST = 0.7F
const val MAGIC = -1

// point
const val SMALL_WIDTH = 3F
const val AVERAGE_WIDTH = 5F
const val LARGE_WIDTH = 7F

// changing
const val RIBS_POSITION = 0.2F
const val RAD_K = 0.9F
const val RAD_IN_K = 0.7F
const val RAD_MINI_K = 0.1F
const val RAD_INNER_K = 0.3F
const val TEXT_SIZE_K = 0.08F
const val TEXT_SIZE_K2 = 0.06F
const val SET_TURN_K = 0.1F
const val TEXT_SHIFT_ROOT_X = 0.04F
const val TEXT_SHIFT_ROOT_Y = 0.02F
const val TEXT_SHIFT_DEGREE_X = 0.03F
const val TEXT_SHIFT_DEGREE_Y = 0.03F
const val TEXT_SIZE_BIG_K = 0.1F
const val CASUAL_MOVE = 10F

// params
const val SET_LENGTH = 3
const val CURRENT_NUMBER_MAX = 100
const val THRESHOLD_ANGLE = PI/12
const val ALL_TIME = 60_000L     // ms
const val KOL_MOVES = 2
const val MAX_MOVES = 8
val KOL_NODES = mapOf(EASY to 4, MEDIUM to 5, HARD to 6, INSANE to 8)

val LANGUAGES = arrayOf("English", "Русский")
val TIMES = arrayOf(10, 15, 30, 60, 300, 3600)
val BOUNDS_PLUS_MINUS = Pair(1, 100)
val BOUNDS_MULTIPLICATION_DIVISION = Pair(2, 25)
val BOUNDS_DEGREE_ROOT = Pair(2, 5)
val PROB_LIST_STANDARD = listOf(PLUS, PLUS, PLUS, MINUS, MINUS, MINUS, MULTIPLICATION, MULTIPLICATION, DIVISION, DIVISION, DEGREE, ROOT)
val PROB_LIST_SET = listOf(PLUS, PLUS, MINUS, MINUS, MULTIPLICATION, DIVISION)
val PROB_LIST_MAX = listOf(PLUS, MINUS, MULTIPLICATION, DIVISION, DEGREE, ROOT)