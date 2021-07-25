package com.example.calculgraph

import java.util.*
import kotlin.math.pow
import kotlin.random.Random
import com.example.calculgraph.Operation.*
import com.example.calculgraph.constant.*
import java.lang.Integer.MIN_VALUE

class Field(var kolMoves: Int, kolNode: Int, mode: String) {
    var currentNode = Random.nextInt(0, kolNode)
    val graph = Graph(kolNode, kolMoves, currentNode, mode)
    private val lenList = if (mode == "set") SET_LENGTH else 1
    var currentNumbers = List(lenList) { Random.nextInt(0, CURRENT_NUMBER_MAX) }
    var totalNumbers: List<Int>
    private val history = Stack<Int>()
    private val answer = Stack<Int>()

    init {
        val list = graph.listTo(graph.data)

        if (mode == "max") {
            fun Pair<Int, List<Int>>.mapSecond(function: (List<Int>) -> List<Int>) = Pair(first, function(second))

            fun doPath(q: Int, cur: Int, last: Int, kol: Int): Pair<Int, List<Int>> {
                if (kol == 0) return Pair(if (last == -1) q else _moving(last, cur, listOf(q)).first(), listOf(cur))
                return list[cur].filter { it != last }
                    .map { next -> doPath(if (last == -1) q else _moving(last, cur, listOf(q)).first(), next, cur, kol - 1) }
                    .maxByOrNull { it.first }?.mapSecond { it + cur } ?: Pair(MIN_VALUE, listOf())
            }

            val p = doPath(currentNumbers[0], currentNode, -1, kolMoves)
            totalNumbers = listOf(p.first)
            answer.addAll(p.second)
        }
        else {
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
                } while (!doPath(next, cur, kol - 1))
                answer.add(cur)
                return true
            }

            doPath(currentNode, -1, kolMoves)
            val tempCurrentNumber = currentNumbers
            answer.windowed(2).reversed().forEach {
                moving(it[1], it[0])
                println(graph.data[it[1]][it[0]])                                                   //
            }
            totalNumbers = currentNumbers
            currentNumbers = tempCurrentNumber
        }
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

    private fun checkWin() = currentNumbers == totalNumbers

    fun back() {
        if(history.isNotEmpty()) {
            kolMoves++
            val from = currentNode
            currentNode = history.pop()
            moving(from, currentNode)
        }
    }

    private fun moving(from: Int, to: Int) { currentNumbers = _moving(from, to, currentNumbers) }

    private fun _moving(from: Int, to: Int, list: List<Int>) = when(graph.data[from][to].oper) {
        NONE              -> throw error("wrong move!")
        PLUS              -> list.map { x -> graph.data[from][to].num?.let { x + it } ?: throw error("wrong data num!") }
        MINUS             -> list.map { x -> graph.data[from][to].num?.let { x - it } ?: throw error("wrong data num!") }
        MULTIPLICATION    -> list.map { x -> graph.data[from][to].num?.let { x * it } ?: throw error("wrong data num!") }
        DIVISION          -> if (graph.data[from][to].num == 0) throw error("0 division")
        else list.map { x -> graph.data[from][to].num?.let { x / it } ?: throw error("wrong data num!") }
        DEGREE            -> list.map { x -> graph.data[from][to].num?.let { x.toDouble().pow(it) }?.toInt() ?: throw error("wrong data num!") }
        ROOT              -> if (graph.data[from][to].num == 0) throw error("0 division")
        else list.map { x -> graph.data[from][to].num?.let { x.toDouble().pow(1.0 / it) }?.toInt() ?: throw error("wrong data num!") }
    }
}
