package com.example.calculgraph.helpers

import com.example.calculgraph.activity.AnyActivity.Companion.preGen
import com.example.calculgraph.constant.*
import com.example.calculgraph.enums.Operation
import com.example.calculgraph.enums.Operation.*
import com.example.calculgraph.enums.reverse
import com.example.calculgraph.states.Inscription
import kotlin.math.pow
import kotlin.random.Random

class GraphGenerator(private val kolNodes: Int, private val kolBranch: Int) {
    companion object {
        var shutdown = false
    }

    fun generateGraph(kolMoves: Int, currentNode: Int, mode: String) {
        fun factorial(n: Int) = (2..n).fold(1L, Long::times)
        if (factorial(kolNodes) < kolBranch) throw error("Too match branches!")

        val probList: List<Operation> = when(mode) {
            "standard"  -> PROB_LIST_STANDARD
            "set"       -> PROB_LIST_SET
            "max"       -> PROB_LIST_MAX
            "any"       -> PROB_LIST_ANY
            else        -> throw error("Wring mode!")
        }
        lateinit var kolInIndex: MutableList<Int>

        fun doBounds(oper: Operation) = when(oper) {
            PLUS, MINUS -> BOUNDS_PLUS_MINUS
            MULTIPLICATION, DIVISION -> BOUNDS_MULTIPLICATION_DIVISION
            DEGREE, ROOT -> BOUNDS_DEGREE_ROOT
            NONE -> throw error("unreal")
        }

        fun generateOne(i: Int) {
            var j = i
            while (j == i || preGen.data[i][j].oper != NONE) {
                j = Random.nextInt(0, kolNodes)
            }
            val oper = probList.random()
            val num = doBounds(oper).let { Random.nextInt(it.first, it.second) }
            preGen.data[i][j] = Inscription(oper, num)
            preGen.data[j][i] = Inscription(oper.reverse(), num)
            kolInIndex[i]++
            kolInIndex[j]++
        }

//        TODO("rewrite this")
        fun correctGraph(): Boolean {
            val list = listTo(preGen.data)
            preGen.possibleNumbers = List(CURRENT_NUMBER_MAX) { it }

            fun dfs(cur: Int, last: Int, length: Int): Boolean {
                if (preGen.possibleNumbers.isEmpty() || shutdown) return false
                if (length == 0) {
                    return true
                }
                val answers = mutableListOf<Boolean>()
                for (i in list[cur]) {
                    if (i == last) continue
                    preGen.possibleNumbers = preGen.possibleNumbers.filterMap({ movingHelper(cur, i, it, preGen.data) }, { movingHelper(i, cur, it, preGen.data) })
                    answers.add(dfs(i, cur, length - 1))
                    preGen.possibleNumbers = preGen.possibleNumbers.map { movingHelper(i, cur, it, preGen.data) }
                }
                return answers.any { it } && preGen.possibleNumbers.isNotEmpty()
            }

            return dfs(currentNode, MAGIC, kolMoves)
        }


        var kolIter = 0
        do {
            preGen.data = MutableList(kolNodes) {
                MutableList(kolNodes) {
                    Inscription(NONE, null)
                }
            }
            kolInIndex = List(kolNodes) {0}.toMutableList()
            for (i in 0 until kolNodes - 1) {
                generateOne(i)
            }
            var kol = kolBranch - kolNodes + 1
            if (kolInIndex[kolNodes - 1] == 0) {
                generateOne(kolNodes - 1)
                kol--
            }
            while (kol != 0) {
                var i = MAGIC
                while (i < 0 || kolInIndex[i] == kolNodes - 1) {
                    i = Random.nextInt(0, kolNodes)
                }
                generateOne(i)
                kol--
            }
            kolIter++
        } while (!correctGraph() && !shutdown)
        println("Iteration count: $kolIter")
        if (shutdown) {
            println("Shutdown generateGraph")
        } else {
            println("possibleNumbers.size ${preGen.possibleNumbers.size}")
        }
    }
}

private fun <E> List<E>.filterMap(function1: (E) -> E, function2: (E) -> E) =
    this.map { Pair(it, function1(it)) }
        .filter { a -> a.first == function2(a.second) }
        .map { b -> b.second }

fun listTo(data: List<List<Inscription>>) = data.map { line ->
    line.mapIndexed { i, inscription -> Pair(i, inscription) }
        .filter { it.second.oper != NONE }
        .map { it.first }
}

fun movingHelper(from: Int, to: Int, x: Int, data: List<List<Inscription>>) = when(data[from][to].oper) {
    NONE -> throw error("wrong move!")
    PLUS -> data[from][to].num?.let { x + it } ?: throw error("wrong data num!")
    MINUS -> data[from][to].num?.let { x - it } ?: throw error("wrong data num!")
    MULTIPLICATION -> data[from][to].num?.let { x * it } ?: throw error("wrong data num!")
    DIVISION -> if (data[from][to].num == 0) throw error("0 division")
    else data[from][to].num?.let { x / it } ?: throw error("wrong data num!")
    DEGREE -> data[from][to].num?.let { x.toDouble().pow(it) }?.toInt() ?: throw error("wrong data num!")
    ROOT -> if (data[from][to].num == 0) throw error("0 division")
    else data[from][to].num?.let { x.toDouble().pow(1.0 / it) }?.toInt() ?: throw error("wrong data num!")
}
