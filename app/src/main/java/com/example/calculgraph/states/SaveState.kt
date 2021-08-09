package com.example.calculgraph.states

import com.example.calculgraph.constant.MAX_MOVES
import com.example.calculgraph.constant.MIN_MOVES
import com.example.calculgraph.constant.MODES
import com.example.calculgraph.constant.TIMES
import com.example.calculgraph.enums.Computability
import com.example.calculgraph.enums.GameState

data class SaveState(
    var gameStatus: GameState,
    val time: Long,                                                                //in ms
    var allTime: Long,                                                             //in ms
    val score: Int,
    var kolMoves: Int,
    var computability: Computability,
    val currentNode: Int,
    var mode: String,
    val currentNumbers: List<Int>,
    val totalNumbers: List<Int>,
    val history: List<Int>,
    val answer: List<Int>,
    val data: List<List<Inscription>>): State() {
    fun generateId(): Int {
//    val sizeTime = TIMES.size
        val sizeMoves = MAX_MOVES - MIN_MOVES + 1
        val sizeMode = MODES.size
        val sizeComputability = Computability.values().size

        return (TIMES.indexOf((allTime / 1000).toInt())) * sizeMoves * sizeMode * sizeComputability +
                (answer.size - 1 - MIN_MOVES) * sizeMode * sizeComputability +
                MODES.indexOf(mode) * sizeComputability +
                Computability.values().indexOf(computability) + 1
    }
}
