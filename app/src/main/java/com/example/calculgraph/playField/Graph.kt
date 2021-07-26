package com.example.calculgraph.playField

import com.example.calculgraph.enums.Operation.*
import com.example.calculgraph.enums.*
import com.example.calculgraph.constant.*
import kotlin.random.Random

class Graph(val kolNode: Int, kolMoves: Int, currentNode:Int, mode: String, val kolBranch: Int = 6) {
    inner class Inscription(val oper: Operation, val num: Int?) {
        override fun toString() = oper.opToString() + num.toString()
    }

    val data: Array<Array<Inscription>> = generateGraph(kolMoves, currentNode, mode)

    fun listTo(data: Array<Array<Inscription>>) = data.map { line ->
        line.mapIndexed { i, insc -> Pair(i, insc) }
            .filter { it.second.oper != NONE }
            .map { it.first }
    }

//    TODO("rewrite to normal generation")
    private fun generateGraph(kolMoves: Int, currentNode: Int, mode: String): Array<Array<Inscription>> {
        fun factorial(n: Int) = (2..n).fold(1L, Long::times)
        if (factorial(kolNode) < kolBranch) throw error("Too match branches!")

        val probList: List<Operation> = when(mode) {
            "standard" -> PROB_LIST_STANDARD
            "set" -> PROB_LIST_SET
            "max" -> PROB_LIST_MAX
            else -> throw error("Wring mode!")
        }
        lateinit var _data: Array<Array<Inscription>>
        val kolInIndex = Array(kolNode) {0}

        fun doBounds(oper: Operation) = when(oper) {
            PLUS, MINUS                 -> BOUNDS_PLUS_MINUS
            MULTIPLICATION, DIVISION    -> BOUNDS_MULTIPLICATION_DIVISION
            DEGREE, ROOT                -> BOUNDS_DEGREE_ROOT
            NONE                        -> throw error("unreal")
        }

        fun generateOne(i: Int) {
            var j = i
            while (j == i || _data[i][j].oper != NONE) {
                j = Random.nextInt(0, kolNode)
            }
            val oper = probList.random()
            val num = doBounds(oper).let { Random.nextInt(it.first, it.second) }
            _data[i][j] = Inscription(oper, num)
            _data[j][i] = Inscription(oper.reverse(), num)
            kolInIndex[i]++
            kolInIndex[j]++
        }

        fun correctGraph(): Boolean {
            val list = listTo(_data)

            fun dfs(cur: Int, last: Int, length: Int): Boolean {
                if (length == 0) return true
                for (i in list[cur]) {
                    if (i == last) continue
                    if(dfs(i, cur, length - 1)) return true
                }
                return false
            }

            return dfs(currentNode, -1, kolMoves)
        }


        do {
            _data = Array(kolNode) {
                Array(kolNode) {
                    Inscription(NONE, null)
                }
            }
            for (i in 0 until kolNode - 1) {
                generateOne(i)
            }
            var kol = kolBranch - kolNode + 1
            if (kolInIndex[kolNode - 1] == 0) {
                generateOne(kolNode - 1)
                kol--
            }
            while (kol != 0) {
                var i = -1
                while (i < 0 || kolInIndex[i] == kolNode - 1) {
                    i = Random.nextInt(0, kolNode)
                }
                generateOne(i)
                kol--
            }
        } while (!correctGraph())
        return _data
    }
}

// for tests
internal fun correctnessGraph(graph: Graph): Boolean {
    if (graph.data.size != graph.kolNode || graph.data[0].size != graph.kolNode || graph.kolBranch < graph.kolNode) return false
    var kol = 0
    for (i in 0 until graph.kolNode) {
        for (j in 0 until graph.kolNode) {
            if (graph.data[i][j].oper == NONE) continue
            kol++
            if (graph.data[i][j].oper in listOf(DIVISION, ROOT) && graph.data[i][j].num == 0) return false
            if (graph.data[i][j].num == null) return false
        }
    }
    return kol == graph.kolBranch
}
