package com.example.calculgraph.activity

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.graphics.*
import android.os.Bundle
import android.text.format.DateUtils.SECOND_IN_MILLIS
import android.util.Log
import android.view.MotionEvent
import android.view.WindowManager
import android.widget.Button
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import com.example.calculgraph.R
import com.example.calculgraph.constant.*
import com.example.calculgraph.enums.GameState.*
import com.example.calculgraph.enums.Operation.*
import com.example.calculgraph.enums.*
import com.example.calculgraph.playField.Field
import com.example.calculgraph.service.GraphGeneratorService
import com.example.calculgraph.service.GraphGeneratorService.Companion.updatePreGen
import com.example.calculgraph.states.SaveState
import java.util.*
import java.util.concurrent.CountDownLatch
import kotlin.math.*
import com.example.calculgraph.enums.Sounds.*
import com.example.calculgraph.helpers.SoundPoolHelper.playSound


class GameActivity : AnyGameActivity() {
    private var gameStatus = WAIT
    private var savingState = false
    private var time = MAGIC.toLong()                                                               // ms
    private var iter = MAGIC                                                                        // for answer
    private var iterMax = MAGIC                                                                     // for answer
    private var allTime = MAGIC.toLong()
    private var winCount = 0

    init {
        GraphGeneratorService.resultLauncher = {
            Log.d(logTAG, "GameActivity: resultLauncher")
            runOnUiThread {
                preGen.latch = CountDownLatch(1)
                newGame()
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        saveStatAndStartGame(dbWorker.init(this))
    }

    override fun onTouchEvent(event: MotionEvent) = when (event.action) {
        MotionEvent.ACTION_DOWN -> {
            touchDown = Pair(event.rawX, event.rawY)
            true
        }
        MotionEvent.ACTION_UP -> {
            if (gameStatus == PLAY) treatment(touchDown, Pair(event.rawX, event.rawY))
            true
        }
        else -> super.onTouchEvent(event)
    }

    private fun saveStatAndStartGame(saveState: SaveState) {
        if (getIntentsAndReturnGameStatus()) {
            if (saveState.gameStatus == PLAY) {
                dbWorker.apply {
                    updateStatistic(saveState.score)
                }
            }
            newGameOrLatchUpdate()
        }
        else continueGame(saveState)
    }

    private fun newGameOrLatchUpdate() {
        if (intent.getBooleanExtra("needStart", false)) newGame()
        else preGen.latch.countDown()
    }

    private fun getIntentsAndReturnGameStatus(): Boolean {
        mode = intent.getStringExtra("mode") ?: throw error("Wrong intent!")
        return intent.getBooleanExtra("isNewGame", true)
    }

    private fun newGamePreparation() {
        preGen.filed = Field().apply { preparationField(mode, this@GameActivity) }
    }

    private fun newGame() {
        Log.d(logTAG, "GameActivity: newGame")
        gameStatus = WAIT
        curField = preGen.filed
        newGamePreparation()
        curField.init(mode, preGen.data, preGen.possibleNumbers)
        time = 0L
        allTime = settings.time * SECOND_IN_MILLIS
        computability = settings.computability
        sets()
    }

    private fun continueGame(saveState: SaveState) {
        if (saveState.gameStatus != PLAY) {
            newGameOrLatchUpdate()
        }
        else {
            Log.d(logTAG, "GameActivity: continueGame")
            time = saveState.time
            allTime = saveState.allTime
            winCount = saveState.score
            computability = saveState.computability
            mode = saveState.mode
            redoField(saveState)
            sets()
        }
    }

    private fun redoField(saveState: SaveState) {
        saveState.apply {
            curField = Field(kolMoves, KOL_NODES[computability] ?: throw error("wrong computability"))
            curField.set(currentNode, currentNumbers, totalNumbers, history, answer, data)
        }
    }

    @SuppressLint("SetTextI18n")
    override fun writeField() {
        super.writeField()
        findViewById<TextView>(R.id.score).text = getString(R.string.score, winCount)
    }

    override fun addDraws() {
        super.addDraws()
        background = DrawView(this@GameActivity)
        findViewById<ConstraintLayout>(R.id.draw).addView(background)
    }

    override fun setRendering() {
        motion.cancel()
        gameStatus = PLAY
        iter = MAGIC
        motion = object : TimerTask() {
            override fun run() {
                if(gameStatus == PLAY) {
                    time += DRAWING
                    background.invalidate()
                    if (time >= allTime) {
                        gameStatus = END
                        endGame()
                    }
                } else if (gameStatus == END || gameStatus == WAIT_SHOW) {
                    when {
                        iter == MAGIC -> { // show answer
                            val path = generateAnswer()
                            iterMax = (path.size) * ANSWER_K
                            iter = 1
                            runOnUiThread {
                                findViewById<ConstraintLayout>(R.id.draw).removeView(background)
                                background = DrawViewAnswer(this@GameActivity, path + path.last())
                                findViewById<ConstraintLayout>(R.id.draw).addView(background)
                            }
                        }
                        iter < iterMax - 1 -> {
                            iter++
                            background.invalidate()
                        }
                        iter == iterMax - 1 -> {
                            if (gameStatus == WAIT_SHOW) {
                                gameStatus = WAIT
                                exitToWait(true)
                            }
//                            gameStatus = WAIT
//                            Log.d(logTAG, "GameActivity: show answer end")
                        }
                    }
                }
            }
        }
        timer.schedule(motion, 0, DRAWING)
    }

    override fun move(to: Int) {
        Log.d(logTAG, "GameActivity: move")
        val win = curField.move(to)
        findViewById<TextView>(R.id.kolMoves).text = getString(R.string.updateMoves, curField.kolMoves)
        if (gameStatus == PLAY && win) {
            Log.d(logTAG, "GameActivity: win")
            playSound(WIN)
            winCount++
            gameStatus = WAIT
            if (preGen.actual) preGen.latch.countDown()
            else exitToWait()
        }
    }

    override fun endGame() {
        playSound(LOSE)
        super.endGame()
    }

    override fun doDialogEnd() {
        Log.d(logTAG, "GameActivity: doDialogEnd")
        Dialog(this@GameActivity, R.style.AlertDialogCustom).apply {
            val params = window?.attributes ?: throw error("dialog error")
            params.y = -(size.height * DIALOG_K).toInt()
            setCanceledOnTouchOutside(false)
            setCancelable(false)

            setContentView(R.layout.dialog_yes_no)
            findViewById<TextView>(R.id.dial_text).text = getString(R.string.after_game, winCount)
            findViewById<Button>(R.id.yes).setOnClickListener {
                Log.d(logTAG, "GameActivity: doDialogEnd -> yes")
                playSound(TO)
                dismiss()
                if (!savingState) {
                    saveGameState()
                    savingState = true
                }
                dbWorker.updateStatistic(winCount)
                winCount = 0
                gameStatus = WAIT_SHOW
                preGen.latch.countDown()
            }
            findViewById<Button>(R.id.no).setOnClickListener {
                Log.d(logTAG, "GameActivity: doDialogEnd -> no")
                playSound(TO)
                dismiss()
                exitGame()
            }
            show()
            window?.let {
                it.attributes = params
                it.clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND)
                it.setLayout((size.width * WIDTH_DIALOG_K).toInt(), (size.height * HEIGHT_DIALOG_YES_NO).toInt())
            }
        }
    }

    override fun exitGame() {
        saveGameState()
        if (gameStatus == END)
            dbWorker.updateStatistic(winCount)
        val intent = Intent(this, MainActivity :: class.java )
        motion.cancel()
        timer.cancel()
        updatePreGen(this@GameActivity) {
            startActivity(intent, transitionActivity.toBundle())
            finish()
        }
    }

    private fun saveGameState() {
        Log.d(logTAG, "GameActivity: saveGameState")
        dbWorker.updateSaveState(SaveState(
            gameStatus,
            time,
            allTime,
            winCount,
            curField.kolMoves,
            computability,
            curField.currentNode,
            mode,
            curField.currentNumbers,
            curField.totalNumbers,
            curField.history,
            curField.answer,
            curField.graph.data)
        )
    }

    private fun generateAnswer(): List<Int> {
        var list = listOf(curField.currentNode) + curField.history.reversed()
        val answer = Stack<Int>().apply { addAll(curField.answer) }
        var lastEl = MAGIC
        while (list.isNotEmpty() && list.last() == answer.peek()) {
            lastEl = answer.pop()
            list = list.dropLast(1)
        }

        return mutableListOf<Int>().apply {
            (list + lastEl + answer.reversed()).forEach { el ->
                repeat(ANSWER_WAIT_K) { add(el) }
            }
        }
    }

    private fun exitToWait(updateStatistic: Boolean = false) {
        Log.d(logTAG, "GameActivity: exitToWait")
        saveGameState()
        if (updateStatistic)
            dbWorker.updateStatistic(winCount)
        val intent = Intent(this, WaitActivity :: class.java )
        intent.putExtra("mode", mode)
        intent.putExtra("isNewGame", true)
        motion.cancel()
        timer.cancel()
        startActivity(intent, transitionActivity.toBundle())
        finish()
    }

    inner class DrawView(context: Context?) : DrawHelper(context)  {
        override fun onDraw(canvas: Canvas) {
                drawText(canvas)

                p.apply {
                    strokeWidth = LARGE_WIDTH
                    color = settings.theme.getArcColor(this@GameActivity)
                    style = Paint.Style.STROKE
                }
                canvas.drawArc(rect, -90F, time * 360F / allTime, false, p)

                drawCircles(canvas)

                drawPosCircle(canvas, centers[curField.currentNode])
        }
    }

    private inner class DrawViewAnswer(context: Context?, val path: List<Int>) : DrawHelper(context) {
        @SuppressLint("ResourceAsColor", "DrawAllocation")
        override fun onDraw(canvas: Canvas) {
            val pos = iter / ANSWER_K
            val q = (iter % ANSWER_K).toFloat()
            if (curField.currentNode != path[pos]) move(path[pos])
            val drawCirclePos = Pair(
                centers[curField.currentNode].first + (centers[path[pos + 1]].first - centers[curField.currentNode].first) * (q / ANSWER_K),
                centers[curField.currentNode].second + (centers[path[pos + 1]].second - centers[curField.currentNode].second) * (q / ANSWER_K)
            )

            drawText(canvas)

            drawCircles(canvas)

            drawPosCircle(canvas, drawCirclePos)
        }
    }
}