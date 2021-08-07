package com.example.calculgraph.states

data class LevelState(var kolMoves: Int,
                      var currentNode: Int,
                      var numbers: List<Int>,
                      var totalNumbers: List<Int>,
                      var data: List<List<Inscription>>) : State()
