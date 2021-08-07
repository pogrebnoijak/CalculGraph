package com.example.calculgraph.constant

import com.example.calculgraph.enums.Computability
import com.example.calculgraph.enums.Computability.*
import com.example.calculgraph.enums.Operation.*
import kotlin.math.PI

// const
const val DRAWING = 10L         // ms
const val LAYOUT_CONST = 0.7F
const val LAYOUT_CONST_LEVELS = 0.9F
const val MAGIC = -1
const val MINUTE = 60
const val HOUR = 3600
const val DEFAULT_ID = 1
const val MAX_STREAM = 1
const val KOL_LEVELS = 25
const val KOL_LEVELS_IN_LINE = 5
const val NUMBER_FIRST_LEVEL = 1

// point
const val SMALL_WIDTH = 3F
const val AVERAGE_WIDTH = 5F
const val LARGE_WIDTH = 7F

// changing
const val RIBS_POSITION = 0.25F
const val RAD_K = 0.95F
const val RAD_IN_K = 0.8F
const val RAD_MINI_K = 0.1F
const val RAD_INNER_K = 0.3F
const val TEXT_SIZE_K = 0.08F
const val TEXT_SIZE_K2 = 0.06F
const val SET_TURN_K = 0.1F
const val DIALOG_K = 0.42F
const val TEXT_SHIFT_ROOT_X = 0.04F
const val TEXT_SHIFT_ROOT_Y = 0.02F
const val TEXT_SHIFT_DEGREE_X = 0.03F
const val TEXT_SHIFT_DEGREE_Y = 0.03F
const val TEXT_SIZE_BIG_K = 0.1F
const val CASUAL_MOVE = 10F
const val ANSWER_K = 50
const val ANSWER_WAIT_K = 2
const val LEVEL_BUTTON_SIZE_K = 0.15F
const val LEVEL_BUTTON_DIF = (1F - LEVEL_BUTTON_SIZE_K * KOL_LEVELS_IN_LINE) / (KOL_LEVELS_IN_LINE + 1)
const val LEVEL_BUTTON_SIZE_AND_DIF = LEVEL_BUTTON_SIZE_K + LEVEL_BUTTON_DIF

// params
const val SET_LENGTH = 3
const val CURRENT_NUMBER_MAX = 9999
const val THRESHOLD_ANGLE = PI/12
const val MAX_MOVES = 8
const val MIN_MOVES = 2
val KOL_NODES = mapOf(EASY to 4, MEDIUM to 5, HARD to 6, INSANE to 8)
val LANGUAGES = arrayOf("English", "Русский")
val TIMES = listOf(10, 15, 30, 60, 300, 600, 3600)
val MODES = listOf("standard", "set", "max")
val BOUNDS_PLUS_MINUS = Pair(1, 100)
val BOUNDS_MULTIPLICATION_DIVISION = Pair(2, 20)
val BOUNDS_DEGREE_ROOT = Pair(2, 4)
val PROB_LIST_STANDARD = listOf(PLUS, PLUS, PLUS, MINUS, MINUS, MINUS, MULTIPLICATION, MULTIPLICATION, DIVISION, DIVISION, DEGREE, ROOT)
val PROB_LIST_SET = listOf(PLUS, PLUS, MINUS, MINUS, MULTIPLICATION, DIVISION)
val PROB_LIST_MAX = listOf(PLUS, MINUS, MULTIPLICATION, DIVISION, DEGREE, ROOT)
val PROB_LIST_ANY = listOf(PLUS, PLUS, PLUS, MINUS, MINUS, MINUS, MULTIPLICATION, DIVISION)
val LEVELS_GROUP_KOL = MODES.size * Computability.values().size
val MAX_ID = TIMES.size * (MAX_MOVES - MIN_MOVES + 1) * LEVELS_GROUP_KOL
val LEVELS_ALL_KOL = KOL_LEVELS * LEVELS_GROUP_KOL
