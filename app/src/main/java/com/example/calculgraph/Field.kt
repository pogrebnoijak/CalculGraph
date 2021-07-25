package com.example.calculgraph

import java.util.*
import kotlin.math.pow
import kotlin.random.Random
import com.example.calculgraph.Operation.*
import com.example.calculgraph.constant.*

class Field(var kolMoves: Int, kolNode: Int) {
    var currentNode = Random.nextInt(0, kolNode)
    val graph = Graph(kolNode, kolMoves, currentNode)
    var currentNumber = Random.nextInt(0, CURRENT_NUMBER_MAX)
    var totalNumber: Int = 0
    private val history = Stack<Int>()
    private val answer = Stack<Int>()

    init {
        val list = graph.listTo(graph.data)

        fun doPath(cur: Int, last: Int, kol: Int): Boolean {
            if (kol == 0) {
                answer.add(cur)
                return true
            }
            val curList = list[cur].toMutableList()
            curList.remove(last)
            do {
                if (curList.isEmpty()) return false
                val next = curList.random()
                curList.remove(next)
            } while (!doPath(next, cur, kol-1))
            answer.add(cur)
            return true
        }

        doPath(currentNode, -1, kolMoves)
        val tempCurrentNumber = currentNumber
        answer.windowed(2).reversed().forEach{
            moving(it[1], it[0])
            println(graph.data[it[1]][it[0]])                                                       //
        }
        totalNumber = currentNumber
        currentNumber = tempCurrentNumber

//        println("answer - ${answer.toList()}")                                                    //
    }

    fun move(to: Int): Boolean {
        if (history.isNotEmpty() && history.peek() == to) back()
        else if (kolMoves != 0) {
            history.push(currentNode)
            moving(currentNode, to)
            currentNode = to
            kolMoves--
        }
        return checkWin()
    }

    private fun checkWin() = currentNumber == totalNumber

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
            DEGREE            -> currentNumber = graph.data[from][to].num?.let { currentNumber.toDouble().pow(it) }?.toInt() ?: throw error("wrong data num!")
            ROOT              -> if (graph.data[from][to].num == 0) throw error("0 division")
            else currentNumber = graph.data[from][to].num?.let { currentNumber.toDouble().pow(1.0 / it) }?.toInt() ?: throw error("wrong data num!")
        }
    }
}