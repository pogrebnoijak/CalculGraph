package com.example.calculgraph.playField

import android.content.Context
import com.example.calculgraph.activity.AnyActivity.Companion.settings
import java.util.*
import com.example.calculgraph.helpers.movingHelper
import kotlin.random.Random
import com.example.calculgraph.constant.*
import com.example.calculgraph.enums.Operation.*
import com.example.calculgraph.enums.Sounds.*
import com.example.calculgraph.helpers.SoundPoolHelper.playSound
import com.example.calculgraph.helpers.listTo
import com.example.calculgraph.states.Inscription
import java.lang.Integer.MIN_VALUE


class Field(var kolMoves: Int = settings.moves, kolNodes: Int = KOL_NODES[settings.computability] ?: throw error("wrong computability"),
            var currentNode: Int = Random.nextInt(0, kolNodes)) {
    val graph = Graph(kolNodes)
    lateinit var currentNumbers: List<Int>
    lateinit var totalNumbers: List<Int>
    val history = Stack<Int>()
    val answer = Stack<Int>()

    fun preparationField(mode: String, context: Context) {
        graph.preparationGraph(kolMoves, currentNode, mode, context)
    }

    fun init(mode: String, data: List<List<Inscription>>, possibleNumbers: List<Int>) {
        currentNumbers = graph.init(mode, data, possibleNumbers)

        val list = listTo(graph.data)

        if (mode == "max") {
            fun Pair<Int, List<Int>>.mapSecond(function: (List<Int>) -> List<Int>) = Pair(first, function(second))

            fun doPath(q: Int, cur: Int, last: Int, kol: Int): Pair<Int, List<Int>> {
                if (kol == 0) return Pair(if (last == MAGIC) q else movingHelper(last, cur, listOf(q)).first(), listOf(cur))
                return list[cur].filter { it != last }
                    .map { next -> doPath(if (last == MAGIC) q else movingHelper(last, cur, listOf(q)).first(), next, cur, kol - 1) }
                    .maxByOrNull { it.first }?.mapSecond { it + cur } ?: Pair(MIN_VALUE, listOf())
            }

            val p = doPath(currentNumbers[0], currentNode, MAGIC, kolMoves)
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

            doPath(currentNode, MAGIC, kolMoves)
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

    fun set(
        _currentNode: Int,
        _currentNumbers: List<Int>,
        _totalNumbers: List<Int>,
        _history: List<Int>,
        _answer: List<Int>,
        _data: List<List<Inscription>>
    ) {
        currentNode = _currentNode
        currentNumbers = _currentNumbers
        totalNumbers = _totalNumbers
        _history.forEach { history.add(it) }
        _answer.forEach { answer.add(it) }
        graph.data = _data
        graph.kolBranch = graph.data.sumOf { it.count { inscription -> inscription.oper != NONE } }
    }

    fun move(to: Int): Boolean {
        if (history.isNotEmpty() && history.peek() == to) back()
        else if (kolMoves != 0) {
            playSound(TAP)
            history.add(currentNode)
            moving(currentNode, to)
            currentNode = to
            kolMoves--
        }
        return checkWin()
    }

    private fun checkWin() = currentNumbers == totalNumbers

    fun back() {
        if(history.isNotEmpty()) {
            playSound(TAP)
            kolMoves++
            val from = currentNode
            currentNode = history.pop()
            moving(from, currentNode)
        }
    }

    private fun moving(from: Int, to: Int) { currentNumbers = movingHelper(from, to, currentNumbers) }

    private fun movingHelper(from: Int, to: Int, list: List<Int>) = list.map { movingHelper(from, to, it, graph.data) }
}