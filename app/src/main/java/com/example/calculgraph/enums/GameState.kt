package com.example.calculgraph.enums

import com.example.calculgraph.enums.GameState.*

enum class GameState {
    PLAY,
    WAIT,
    END
}

fun String.toGameState(): GameState = when(this) {
    "PLAY"  -> PLAY
    "WAIT"  -> WAIT
    "END"   -> END
    else    -> throw error("wrong gameState")
}
