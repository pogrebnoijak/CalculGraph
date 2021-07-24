package com.example.calculgraph

class Graph(val kolNode: Int = 4, val kolBranch: Int = 6) {
    val data: Array<Array<Pair<Operation, Int>>> = generateGraph()

    private fun generateGraph(): Array<Array<Pair<Operation, Int>>> {
        TODO()
    }
}

fun correctnessGraph(graph: Graph): Boolean {
    TODO()
}