package com.example.calculgraph.activity

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import com.example.calculgraph.R
import com.example.calculgraph.dataBase.DBWorker
import com.example.calculgraph.enums.GameState.*
import com.example.calculgraph.service.GraphGeneratorService
import com.example.calculgraph.states.SaveState
import java.util.concurrent.CountDownLatch


class WaitActivity : AnyActivity() {
    private var isNewGame = true
    private lateinit var saveState: SaveState
    private val dbWorker = DBWorker()

    init {
        GraphGeneratorService.resultLauncher = {
            println("resultLauncher")
            runOnUiThread {
                preGen.latch = CountDownLatch(1)
                newGame(true)
            }
        }
    }

    private fun newGame(needStart: Boolean = false) {
        val intentGame = Intent(this, GameActivity :: class.java)
        intentGame.putExtra("mode", intent.getStringExtra("mode") ?: throw error("Wrong intent!"))
        intentGame.putExtra("isNewGame", isNewGame)
        intentGame.putExtra("needStart", needStart)
        startActivity(intentGame)
        finish()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        isNewGame = intent.getBooleanExtra("isNewGame", true)
        if (preGen.actual || (setSaveState() && !isNewGame)) newGame()
        else {
            setContentView(R.layout.activity_wait)
            preGen.latch.countDown()
            setButtons()
        }
    }

    override fun setButtons() {
        findViewById<Button>(R.id.menu).setOnClickListener {
            preGen.latch = CountDownLatch(1)
            val intent = Intent(this, MainActivity :: class.java )
            dbWorker.updateSaveState(saveState.apply { gameStatus = END })
            startActivity(intent)
            finish()
        }
    }

    private fun setSaveState(): Boolean {
        saveState = dbWorker.init(this, false)
        return saveState.gameStatus == PLAY
    }
}
