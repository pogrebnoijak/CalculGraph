package com.example.calculgraph.playField

import com.example.calculgraph.enums.Operation.*
import com.example.calculgraph.enums.*
import com.example.calculgraph.constant.*
import com.example.calculgraph.helpers.GraphGenerator
import kotlin.random.Random

class Graph(val kolNodes: Int) {
    class Inscription(val oper: Operation, val num: Int?) {
        override fun toString() = oper.opToString() + num.toString()
    }

//    TODO(do val)
    lateinit var data: List<List<Inscription>>
    var kolBranch: Int = MAGIC
    lateinit var generator: GraphGenerator

    fun init(kolMoves: Int, currentNode:Int, mode: String, _kolBranch: Int = kolBranches()): List<Int> {
        kolBranch = _kolBranch
        generator = GraphGenerator(kolNodes, kolBranch)
        data = generator.generateGraph(kolMoves, currentNode, mode)
        val lenList = if (mode == "set") SET_LENGTH else 1
        return List(lenList) { generator.possibleNumbers.random() }
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
