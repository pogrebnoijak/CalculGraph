package com.example.calculgraph.service

import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.util.Log
import com.example.calculgraph.constant.MAGIC
import com.example.calculgraph.helpers.GraphGenerator
import com.example.calculgraph.playField.Graph
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors


class GraphGeneratorService : Service() {
    override fun onBind(intent: Intent?): IBinder? { return null }

    private val logTAG = "myLogs"
    private lateinit var es: ExecutorService

    companion object {
        lateinit var resultLauncher: (List<List<Graph.Inscription>>, List<Int>) -> Unit
    }

    override fun onCreate() {
        super.onCreate()
        Log.d(logTAG, "GraphGeneratorService onCreate")
        es = Executors.newFixedThreadPool(1)
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d(logTAG, "GraphGeneratorService onDestroy")
    }

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        Log.d(logTAG, "GraphGeneratorService onStartCommand")
        val kolMoves = intent.getIntExtra("kolMoves", MAGIC)
        val currentNode = intent.getIntExtra("currentNode", MAGIC)
        val mode = intent.getStringExtra("mode") ?: throw error("no mode GraphGeneratorService")
        val kolNodes = intent.getIntExtra("kolNodes", MAGIC)
        val kolBranch = intent.getIntExtra("kolBranch", MAGIC)
        val mr = GenHelper(kolMoves, currentNode, mode, kolNodes, kolBranch, startId)
        es.execute(mr)
        es.shutdown()
        return super.onStartCommand(intent, flags, startId)
    }

    private inner class GenHelper(private val kolMoves: Int, private val currentNode: Int, val mode: String,
                                  private val kolNodes: Int, private val kolBranch: Int, private val startId: Int) : Runnable {
        init {
            Log.d(logTAG, "GenHelper#$startId create")
        }

        override fun run() {
            Log.d(logTAG, "GenHelper#$startId start, kolMoves = $kolMoves, currentNode = $currentNode, mode = $mode")
            val generator = GraphGenerator(kolNodes, kolBranch)
            val data = generator.generateGraph(kolMoves, currentNode, mode)
            resultLauncher(data, generator.possibleNumbers)
            Log.d(logTAG, "GenHelper#" + startId + " end, stopSelfResult("+ startId + ") = " + stopSelfResult(startId))
        }
    }
}