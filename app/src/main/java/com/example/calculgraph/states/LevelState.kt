package com.example.calculgraph.states

data class LevelState(val kolMoves: Int,
                      val currentNode: Int,
                      val numbers: List<Int>,
                      val totalNumbers: List<Int>,
                      val data: List<List<Inscription>>) : State()
