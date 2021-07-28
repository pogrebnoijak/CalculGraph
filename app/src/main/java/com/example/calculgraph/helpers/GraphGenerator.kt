package com.example.calculgraph.helpers

import com.example.calculgraph.constant.*
import com.example.calculgraph.enums.Operation
import com.example.calculgraph.enums.Operation.*
import com.example.calculgraph.enums.reverse
import com.example.calculgraph.playField.Graph.Inscription
import kotlin.math.pow
import kotlin.random.Random

class GraphGenerator(private val kolNodes: Int, private val kolBranch: Int) {
    var possibleNumbers: List<Int> = listOf(1)

    fun generateGraph(kolMoves: Int, currentNode: Int, mode: String): List<List<Inscription>> {
        fun factorial(n: Int) = (2..n).fold(1L, Long::times)
        if (factorial(kolNodes) < kolBranch) throw error("Too match branches!")

        val probList: List<Operation> = when(mode) {
            "standard" -> PROB_LIST_STANDARD
            "set" -> PROB_LIST_SET
            "max" -> PROB_LIST_MAX
            else -> throw error("Wring mode!")
        }
        lateinit var tempData: MutableList<MutableList<Inscription>>
        val kolInIndex = List(kolNodes) {0}.toMutableList()

        fun doBounds(oper: Operation) = when(oper) {
            PLUS, MINUS -> BOUNDS_PLUS_MINUS
            MULTIPLICATION, DIVISION -> BOUNDS_MULTIPLICATION_DIVISION
            DEGREE, ROOT -> BOUNDS_DEGREE_ROOT
            NONE -> throw error("unreal")
        }

        fun generateOne(i: Int) {
            var j = i
            while (j == i || tempData[i][j].oper != NONE) {
                j = Random.nextInt(0, kolNodes)
            }
            val oper = probList.random()
            val num = doBounds(oper).let { Random.nextInt(it.first, it.second) }
            tempData[i][j] = Inscription(oper, num)
            tempData[j][i] = Inscription(oper.reverse(), num)
            kolInIndex[i]++
            kolInIndex[j]++
        }

        fun correctGraph(): Boolean {
            val list = listTo(tempData)

            fun dfs(cur: Int, last: Int, length: Int): Boolean {
                if (length == 0) return true
                for (i in list[cur]) {
                    if (i == last) continue
                    if(dfs(i, cur, length - 1)) return true
                }
                return false
            }

            return dfs(currentNode, MAGIC, kolMoves)
        }

//        TODO("fix duplicate")
        fun movingHelper(from: Int, to: Int, num: Int) = when(tempData[from][to].oper) {
            NONE -> throw error("wrong move!")
            PLUS -> num.let { x -> tempData[from][to].num?.let { x + it } ?: throw error("wrong data num!") }
            MINUS -> num.let { x -> tempData[from][to].num?.let { x - it } ?: throw error("wrong data num!") }
            MULTIPLICATION -> num.let { x -> tempData[from][to].num?.let { x * it } ?: throw error("wrong data num!") }
            DIVISION -> if (tempData[from][to].num == 0) throw error("0 division")
            else num.let { x -> tempData[from][to].num?.let { x / it } ?: throw error("wrong data num!") }
            DEGREE -> num.let { x -> tempData[from][to].num?.let { x.toDouble().pow(it) }?.toInt() ?: throw error("wrong data num!") }
            ROOT -> if (tempData[from][to].num == 0) throw error("0 division")
            else num.let { x -> tempData[from][to].num?.let { x.toDouble().pow(1.0 / it) }?.toInt() ?: throw error("wrong data num!") }
        }

//        TODO("rewrite this")
//        fun correctGraph(): Boolean {
//            val list = listTo(tempData)
//            possibleNumbers = List(CURRENT_NUMBER_MAX) { it }
//
//            fun dfs(cur: Int, last: Int, length: Int): Boolean {
//                if (length == 0) return true
//                val answers = mutableListOf<Boolean>()
//                for (i in list[cur]) {
//                    if (i == last) continue
//                    possibleNumbers = possibleNumbers.filterMap({ movingHelper(cur, i, it) }, { movingHelper(i, cur, it) })
//                    answers.add(dfs(i, cur, length - 1))
//                    possibleNumbers = possibleNumbers.map { movingHelper(i, cur, it) }
//                }
//                return answers.any { it }
//            }
//
//            return dfs(currentNode, MAGIC, kolMoves) && possibleNumbers.isNotEmpty()
//        }


        do {
            tempData = MutableList(kolNodes) {
                MutableList(kolNodes) {
                    Inscription(NONE, null)
                }
            }
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
        } while (!correctGraph())
        println("possibleNumbers.size ${possibleNumbers.size}")
        return tempData
    }

    fun listTo(data: List<List<Inscription>>) = data.map { line ->
        line.mapIndexed { i, inscription -> Pair(i, inscription) }
            .filter { it.second.oper != NONE }
            .map { it.first }
    }
}

private fun <E> List<E>.filterMap(function1: (E) -> E, function2: (E) -> E) =
    this.map { Pair(it, function1(it)) }
        .filter { a -> a.first == function2(a.second) }
        .map { b -> b.second }
