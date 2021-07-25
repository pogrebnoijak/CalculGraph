package com.example.calculgraph

import com.example.calculgraph.Operation.*
import kotlin.random.Random

class Graph(val kolNode: Int, val kolBranch: Int = 6) {
    inner class Inscription(val oper: Operation, val num: Int?)

    val data: Array<Array<Inscription>> = generateGraph()

    private fun generateGraph(): Array<Array<Inscription>> {
        fun factorial(n: Int) = (2..n).fold(1L, Long::times)
        if (factorial(kolNode) < kolBranch) throw error("Too match branches!")

        val probList = listOf(PLUS, PLUS, PLUS, MINUS, MINUS, MINUS, MULTIPLICATION, MULTIPLICATION, DIVISION, DIVISION, DEGREE, ROOT)
        val _data = Array(kolNode) {
            Array(kolNode) {
                Inscription(NONE, null)
            }
        }
        val kolInIndex = Array(kolNode) {0}

        fun doBounds(oper: Operation) = when(oper) {
            PLUS, MINUS     -> Pair(0, 100)
            MULTIPLICATION  -> Pair(0, 20)
            DIVISION        -> Pair(1, 20)
            DEGREE          -> Pair(0, 5)
            ROOT            -> Pair(1, 5)
            NONE            -> throw error("unreal")
        }

        fun generateOne(i: Int) {
            var j = i
            while (j == i || _data[i][j].oper != NONE) {
                j = Random.nextInt(0, kolNode)
            }
            val oper = probList[Random.nextInt(0, probList.size)]
            val num = doBounds(oper).let { Random.nextInt(it.first, it.second) }
            _data[i][j] = Inscription(oper, num)
            _data[j][i] = Inscription(oper.reverse(), num)
            kolInIndex[i]++
            kolInIndex[j]++
        }

        for (i in 0 until kolNode - 1) {
            generateOne(i)
        }
        var kol = kolBranch - kolNode + 1
        if (kolInIndex[kolNode-1] == 0) {
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
        return _data
    }
}

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
