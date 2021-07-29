package com.example.calculgraph.playField

import com.example.calculgraph.enums.Operation.*
import com.example.calculgraph.enums.*
import com.example.calculgraph.constant.*
import com.example.calculgraph.service.GraphGeneratorService
import kotlin.random.Random
import android.content.Intent
import android.content.Context


class Graph(val kolNodes: Int, var kolBranch: Int = Random.nextInt(kolNodes, kolNodes * (kolNodes - 1) / 2 + 1)) {
    class Inscription(val oper: Operation, val num: Int?) {
        override fun toString() = oper.opToString() + num.toString()
    }

//    TODO(do val)
    lateinit var data: List<List<Inscription>>

    fun preparationGraph(kolMoves: Int, currentNode: Int, mode: String, context: Context) {
        generateData(kolMoves, currentNode, mode, context)
    }

    fun init(mode: String, data: List<List<Inscription>>, possibleNumbers: List<Int>): List<Int> {
        this.data = data
        val lenList = if (mode == "set") SET_LENGTH else 1
        return List(lenList) { possibleNumbers.random() }
    }

    private fun generateData(kolMoves: Int, currentNode: Int, mode: String, context: Context) {
        val intent = Intent(context, GraphGeneratorService::class.java)
        intent.apply {
            putExtra("kolMoves", kolMoves)
            putExtra("currentNode", currentNode)
            putExtra("mode", mode)
            putExtra("kolNodes", kolNodes)
            putExtra("kolBranch", kolBranch)
            println("startService")
            context.startService(this)
        }
    }
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
