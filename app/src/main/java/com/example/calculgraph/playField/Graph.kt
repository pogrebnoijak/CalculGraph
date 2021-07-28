package com.example.calculgraph.playField

import com.example.calculgraph.enums.Operation.*
import com.example.calculgraph.enums.*
import com.example.calculgraph.constant.*
import kotlin.random.Random

class Graph(val kolNodes: Int) {
    inner class Inscription(val oper: Operation, val num: Int?) {
        override fun toString() = oper.opToString() + num.toString()
    }

//    TODO(do val)
    lateinit var data: List<List<Inscription>>
    var kolBranch: Int = MAGIC

    fun init(kolMoves: Int, currentNode:Int, mode: String, _kolBranch: Int = kolBranches()) {
        kolBranch = _kolBranch
        data = generateGraph(kolMoves, currentNode, mode)
    }

    fun listTo(data: List<List<Inscription>>) = data.map { line ->
        line.mapIndexed { i, inscription -> Pair(i, inscription) }
            .filter { it.second.oper != NONE }
            .map { it.first }
    }

//    TODO("rewrite to normal generation")
    private fun generateGraph(kolMoves: Int, currentNode: Int, mode: String): List<List<Inscription>> {
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
            PLUS, MINUS                 -> BOUNDS_PLUS_MINUS
            MULTIPLICATION, DIVISION    -> BOUNDS_MULTIPLICATION_DIVISION
            DEGREE, ROOT                -> BOUNDS_DEGREE_ROOT
            NONE                        -> throw error("unreal")
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
        return tempData
    }

    private fun kolBranches(): Int = Random.nextInt(kolNodes, kolNodes * (kolNodes - 1) / 2 + 1)
}

// for tests
internal fun correctnessGraph(graph: Graph): Boolean {
    if (graph.data.size != graph.kolNodes || graph.data[0].size != graph.kolNodes || graph.kolBranch < graph.kolNodes) return false
    var kol = 0
    for (i in 0 until graph.kolNodes) {
        for (j in 0 until graph.kolNodes) {
            if (graph.data[i][j].oper == NONE) continue
            kol++
            if (graph.data[i][j].oper in listOf(DIVISION, ROOT) && graph.data[i][j].num == 0) return false
            if (graph.data[i][j].num == null) return false
        }
    }
    return kol == graph.kolBranch
}
