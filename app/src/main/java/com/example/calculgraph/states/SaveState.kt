package com.example.calculgraph.states

import com.example.calculgraph.constant.MAX_MOVES
import com.example.calculgraph.constant.MIN_MOVES
import com.example.calculgraph.constant.MODES
import com.example.calculgraph.constant.TIMES
import com.example.calculgraph.enums.Computability
import com.example.calculgraph.enums.GameState
import com.example.calculgraph.playField.Graph

data class SaveState(var gameStatus: GameState,
                     var time: Long,                                                                //in ms
                     var allTime: Long,                                                             //in ms
                     var score: Int,
                     var kolMoves: Int,
                     var computability: Computability,
                     var currentNode: Int,
                     var mode: String,
                     var currentNumbers: List<Int>,
                     var totalNumbers: List<Int>,
                     var history: List<Int>,
                     var answer: List<Int>,
                     var data: List<List<Graph.Inscription>>): State() {
    fun generateId(): Int {
//    val sizeTime = TIMES.size
        val sizeMoves = MAX_MOVES - MIN_MOVES + 1
        val sizeMode = MODES.size
        val sizeComputability = Computability.values().size

        val a = (TIMES.indexOf((allTime / 1000).toInt())) * sizeMoves * sizeMode * sizeComputability +
                (answer.size - 1 - MIN_MOVES) * sizeMode * sizeComputability +
                MODES.indexOf(mode) * sizeComputability +
                Computability.values().indexOf(computability) + 1
        println(a)
        return a
    }
}
