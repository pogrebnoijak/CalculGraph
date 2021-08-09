package com.example.calculgraph.service

import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.IBinder
import android.util.Log
import com.example.calculgraph.activity.AnyActivity.Companion.preGen
import com.example.calculgraph.constant.MAGIC
import com.example.calculgraph.helpers.GraphGenerator
import com.example.calculgraph.playField.Field
import java.util.concurrent.CountDownLatch
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors


class GraphGeneratorService : Service() {
    override fun onBind(intent: Intent?): IBinder? { return null }

    private val logSERV = "appLogs.Service"
    private lateinit var es: ExecutorService

    companion object {
        lateinit var resultLauncher: () -> Unit
        fun updatePreGen(context: Context, doAfter: () -> Unit = {}) {
            preGen.apply {
                GraphGenerator.shutdown = true
                resultLauncher = {
                    GraphGenerator.shutdown = false
                    latch = CountDownLatch(1)
                    filed = Field().apply { preparationField("any", context) }
                    doAfter()
                }
                latch.countDown()
            }
        }
    }

    override fun onCreate() {
        super.onCreate()
        Log.d(logSERV, "GraphGeneratorService: onCreate")
        es = Executors.newFixedThreadPool(1)
    }

    override fun onDestroy() {
        es.shutdown()
        super.onDestroy()
        Log.d(logSERV, "GraphGeneratorService: onDestroy")
    }

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        Log.d(logSERV, "GraphGeneratorService onStartCommand")
        val kolMoves = intent.getIntExtra("kolMoves", MAGIC)
        val currentNode = intent.getIntExtra("currentNode", MAGIC)
        val mode = intent.getStringExtra("mode") ?: throw error("no mode GraphGeneratorService")
        val kolNodes = intent.getIntExtra("kolNodes", MAGIC)
        val kolBranch = intent.getIntExtra("kolBranch", MAGIC)
        val mr = GenHelper(kolMoves, currentNode, mode, kolNodes, kolBranch, startId)
        es.execute(mr)
        return super.onStartCommand(intent, flags, startId)
    }

    private inner class GenHelper(private val kolMoves: Int, private val currentNode: Int, val mode: String,
                                  private val kolNodes: Int, private val kolBranch: Int, private val startId: Int) : Runnable {
        override fun run() {
            Log.d(logSERV, "GenHelper-$startId start, kolMoves = $kolMoves, currentNode = $currentNode, mode = $mode")
            val generator = GraphGenerator(kolNodes, kolBranch)
            preGen.actual = false
            generator.generateGraph(kolMoves, currentNode, mode)
            preGen.actual = true
            preGen.latch.await()
            resultLauncher()
            Log.d(logSERV, "GenHelper-$startId end, stopSelfResult($startId) = ${stopSelfResult(startId)}")
        }
    }
}
