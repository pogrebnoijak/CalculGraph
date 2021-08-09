package com.example.calculgraph.activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import com.example.calculgraph.R
import com.example.calculgraph.dataBase.DBWorker
import com.example.calculgraph.enums.GameState.*
import com.example.calculgraph.enums.Sounds.*
import com.example.calculgraph.helpers.SoundPoolHelper.playSound
import com.example.calculgraph.helpers.SoundPoolHelper.playWaitingPause
import com.example.calculgraph.helpers.SoundPoolHelper.playWaitingStart
import com.example.calculgraph.service.GraphGeneratorService
import com.example.calculgraph.states.SaveState
import java.util.concurrent.CountDownLatch


class WaitActivity : AnyActivity() {
    private var isNewGame = true
    private lateinit var saveState: SaveState
    private val dbWorker = DBWorker()

    init {
        GraphGeneratorService.resultLauncher = {
            Log.d(logTAG, "WaitActivity: resultLauncher")
            runOnUiThread {
                preGen.latch = CountDownLatch(1)
                newGame(true)
            }
        }
    }

    private fun newGame(needStart: Boolean = false) {
        Log.d(logTAG, "WaitActivity: newGame")
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
            Log.d(logTAG, "WaitActivity: set activity_wait")
            setContentView(R.layout.activity_wait)
            setTransitionActivity(R.id.waitAll)
            preGen.latch.countDown()
            playWaitingStart()
            setButtons()
        }
    }

    override fun onStop() {
        super.onStop()
        playWaitingPause()
    }

    override fun setButtons() {
        findViewById<Button>(R.id.menu).setOnClickListener {
            playSound(MENU)
            preGen.latch = CountDownLatch(1)
            val intent = Intent(this, MainActivity :: class.java )
            dbWorker.updateSaveState(saveState.apply { gameStatus = END })
            startActivity(intent)
            startActivity(intent, transitionActivity.toBundle())
        }
    }

    private fun setSaveState(): Boolean {
        saveState = dbWorker.init(this, false)
        return saveState.gameStatus == PLAY
    }
}
