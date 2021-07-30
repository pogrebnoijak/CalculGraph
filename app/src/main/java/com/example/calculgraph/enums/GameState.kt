package com.example.calculgraph.enums

import com.example.calculgraph.enums.GameState.*

enum class GameState {
    PLAY,
    WAIT,
    END,
    WAIT_SHOW
}

fun String.toGameState(): GameState = when(this) {
    "PLAY"      -> PLAY
    "WAIT"      -> WAIT
    "END"       -> END
    "WAIT_SHOW" -> WAIT_SHOW
    else        -> throw error("wrong gameState")
}
