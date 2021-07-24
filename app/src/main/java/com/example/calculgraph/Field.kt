package com.example.calculgraph

import kotlin.random.Random

class Field(var kolMoves: Int) {
    private val graph = Graph()

    var time = 60

    var currentNode = Random.nextInt(0, graph.kolNode)

    private val history = ArrayDeque<Int>()

    fun move() {
        TODO()
    }

    fun back() {
        TODO()
    }
}