package com.example.calculgraph.states

import com.example.calculgraph.playField.Graph

data class SaveState(var endGame: Boolean,
                     var time: Long,
                     var score: Int,
                     var kolMoves: Int,
                     var currentNode: Int,
                     var mode: String,
                     var currentNumbers: List<Int>,
                     var totalNumbers: List<Int>,
                     var history: List<Int>,
                     var answer: List<Int>,
                     var data: List<List<Graph.Inscription>>): State()
