package com.example.calculgraph

import java.util.*
import kotlin.math.pow
import kotlin.random.Random
import com.example.calculgraph.Operation.*
import com.example.calculgraph.constant.*

class Field(var kolMoves: Int, kolNode: Int) {
    val graph = Graph(kolNode)
    var currentNode = Random.nextInt(0, graph.kolNode)
    var currentNumber = Random.nextInt(0, CURRENT_NUMBER_MAX)
    val totalNumber = Random.nextInt(0, CURRENT_TOTAL_MAX)
    private val history = Stack<Int>()

    fun move(to: Int) {
        if (history.isNotEmpty() && history.peek() == to) back()
        else {
            history.push(currentNode)
            moving(currentNode, to)
            currentNode = to
            kolMoves--
        }
    }

    fun back() {
        if(history.isNotEmpty()) {
            kolMoves++
            val from = currentNode
            currentNode = history.pop()
            moving(from, currentNode)
        }
    }

    private fun moving(from: Int, to: Int) {
        when(graph.data[from][to].oper) {
            NONE              -> throw error("wrong move!")
            PLUS              -> currentNumber += graph.data[from][to].num ?: throw error("wrong data num!")
            MINUS             -> currentNumber -= graph.data[from][to].num ?: throw error("wrong data num!")
            MULTIPLICATION    -> currentNumber *= graph.data[from][to].num ?: throw error("wrong data num!")
            DIVISION          -> if (graph.data[from][to].num == 0) throw error("0 division")
            else currentNumber /= graph.data[from][to].num ?: throw error("wrong data num!")
            DEGREE            -> graph.data[from][to].num?.let { currentNumber.toDouble().pow(it) } ?: throw error("wrong data num!")
            ROOT              -> if (graph.data[from][to].num == 0) throw error("0 division")
            else graph.data[from][to].num?.let { currentNumber.toDouble().pow(1 / it) } ?: throw error("wrong data num!")
        }
    }
}