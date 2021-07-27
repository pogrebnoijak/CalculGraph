package com.example.calculgraph.activity

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.graphics.*
import android.os.Bundle
import android.text.format.DateUtils.SECOND_IN_MILLIS
import android.util.DisplayMetrics
import android.view.MotionEvent
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.MotionEventCompat
import com.example.calculgraph.R
import com.example.calculgraph.constant.*
import com.example.calculgraph.dataBase.DBWorker
import com.example.calculgraph.enums.*
import com.example.calculgraph.playField.Field
import com.example.calculgraph.states.SaveState
import java.util.*
import kotlin.math.*


class GameActivity : AnyActivity() {
    private var play = true
    private var savingState = false
    private var time = MAGIC.toLong()                                                               // ms
    //    TODO(remove val")
    private var allTime = MAGIC.toLong()
    private var winCount = 0
    private lateinit var computability: Computability
    private val timer = Timer()
    private val dbWorker = DBWorker()
    private var motion: TimerTask = object: TimerTask() { override fun run() {} }
    private lateinit var background: DrawView
    private lateinit var curField: Field
    private lateinit var vecCentres: List<Pair<Float, Float>>
    private lateinit var touchDown: Pair<Float, Float>
    private lateinit var mode: String

    inner class Size(val width: Float, val height: Float)
    val size: Size
        get() {
            val metrics = DisplayMetrics()
            windowManager.defaultDisplay.getMetrics(metrics)
            return Size(metrics.widthPixels * 1F, metrics.heightPixels * LAYOUT_CONST)
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        prepare()
        setContentView(R.layout.activity_game)
        saveStatAndStartGame(dbWorker.init(this))
    }

    override fun onTouchEvent(event: MotionEvent) = when (MotionEventCompat.getActionMasked(event)) {
            MotionEvent.ACTION_DOWN -> {
                touchDown = Pair(event.rawX, event.rawY)
                true
            }
            MotionEvent.ACTION_UP -> {
                if (play) treatment(touchDown, Pair(event.rawX, event.rawY))
                true
            }
            else -> super.onTouchEvent(event)
        }

    private fun saveStatAndStartGame(saveState: SaveState) {
        if (getIntentsAndReturnGameStatus()) {
            if (!saveState.endGame) {
                dbWorker.apply {
                    updateStatistic(saveState.score)
                }
            }
            newGame()
        }
        else continueGame(saveState)
    }

    private fun getIntentsAndReturnGameStatus(): Boolean {
        mode = intent.getStringExtra("mode") ?: throw error("Wrong intent!")
        return intent.getBooleanExtra("isNewGame", true)
    }

    private fun newGame() {
        doField()
        time = 0L
        allTime = settings.time * SECOND_IN_MILLIS
        computability = settings.computability
        sets()
    }

    private fun continueGame(saveState: SaveState) {
        if (saveState.endGame) newGame()
        else {
            time = saveState.time
            allTime = saveState.allTime
            winCount = saveState.score
            computability = saveState.computability
            mode = saveState.mode
            redoField(saveState)
            sets()
        }
    }

    @SuppressLint("SetTextI18n")
    private fun doField() {
        curField = Field()
        curField.init(mode)
        writeField()
    }

    private fun redoField(saveState: SaveState) {
        saveState.apply {
            curField = Field(kolMoves, KOL_NODES[computability] ?: throw error("wrong computability"))
            curField.set(currentNode, currentNumbers, totalNumbers, history, answer, data)
        }
        writeField()
    }

    private fun writeField() {
        findViewById<TextView>(R.id.score).text = "score $winCount"
        findViewById<TextView>(R.id.kolMoves).text = "moves: ${curField.kolMoves}"
        val str = when(mode) {
            "standard" -> curField.totalNumbers[0].toString()
            "set" -> curField.totalNumbers.toString()
            "max" -> "maximum"
            else -> throw error("wrong mode!")
        }
        findViewById<TextView>(R.id.totalNumber).text = "need: $str"
        vecCentres = (0 until curField.graph.kolNodes).map { 2.0 * it / curField.graph.kolNodes }
            .map { Pair(cos(PI * it).toFloat(), sin(PI * it).toFloat()) }
    }

    private fun sets() {
        setButtons()
        addDraws()
        setRendering()
    }

    override fun setButtons() {
        findViewById<Button>(R.id.menu).setOnClickListener {
            exitGame()
        }
        findViewById<Button>(R.id.back).setOnClickListener {
            curField.back()
            findViewById<TextView>(R.id.kolMoves).text = "moves: ${curField.kolMoves}"
        }
    }

    private fun addDraws() {
        findViewById<ConstraintLayout>(R.id.draw).removeAllViews()
        findViewById<ConstraintLayout>(R.id.draw).addView(DrawViewConstant(this))
        background = DrawView(this)
        findViewById<ConstraintLayout>(R.id.draw).addView(background)
    }

    private fun setRendering() {
        motion.cancel()
        play = true
        motion = object : TimerTask() {
            override fun run() {
                if(play) {
                    time += DRAWING
                    background.invalidate()
                    if (time >= allTime) {
                        play = false
                        endGame()
                    }
                }
            }
        }
        timer.schedule(motion, 0, DRAWING)
    }

    private fun treatment(start: Pair<Float, Float>, end: Pair<Float, Float>) {
        operator fun Pair<Float, Float>.minus(v: Pair<Float, Float>) = Pair(first - v.first, second - v.second)
        fun angle(v1: Pair<Float, Float>, v2: Pair<Float, Float>) = // cos
            v1.skalProd(v2) / (v1.l2() * v2.l2())  // v1, v2 not 0

        if ((end - start).l2() > CASUAL_MOVE) {
            curField.graph.data[curField.currentNode]
                .mapIndexed { i, insc -> Pair(i, insc) }
                .filter { it.second.oper != Operation.NONE }
                .map { Pair(it.first, angle(end - start, vecCentres[it.first] - vecCentres[curField.currentNode])) }
                .filter { it.second >= cos(THRESHOLD_ANGLE) }
                .maxByOrNull { it.second }
                ?.first
                ?.let { move(it) }
        }
    }

    private fun move(to: Int) {
        val win = curField.move(to)
        findViewById<TextView>(R.id.kolMoves).text = "moves: ${curField.kolMoves}"
        if (win) {
            winCount++
            newGame()
        }
    }

    private fun endGame() = runOnUiThread { doDialogEnd() }

    private fun doDialogEnd() {
        Dialog(this@GameActivity, R.style.AlertDialogCustom).apply {
            setCanceledOnTouchOutside(false)
            setCancelable(false)

            val params = window?.attributes ?: throw error("dialog error")
            params.y = -(size.height * DIALOG_K).toInt()
            window?.attributes = params

            setContentView(R.layout.dialog_yes_no)
            findViewById<TextView>(R.id.dial_text).text = "You have scored $winCount points. Do you want to play again?"
            show()
            findViewById<Button>(R.id.yes).setOnClickListener {
                dismiss()
                if (!savingState) {
                    saveGameState()
                    savingState = true
                }
                dbWorker.updateStatistic(winCount)
                winCount = 0
                newGame()
            }
            findViewById<Button>(R.id.no).setOnClickListener {
                dismiss()
                exitGame()
            }
        }
    }

    private fun exitGame() {
        saveGameState()
        if (!play)
            dbWorker.updateStatistic(winCount)
        val intent = Intent(this, MainActivity :: class.java )
        play = false
        motion.cancel()
        timer.cancel()
        startActivity(intent)
        finish()
    }

    private fun saveGameState() {
        dbWorker.updateSaveState(SaveState(
            !play,
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


    abstract inner class Draws(context: Context?) : View(context) {
        val p: Paint = Paint()
        val centerH = size.height / 2
        val centerW = size.width / 2
        val rad = centerW * RAD_K
        val radIn = centerW * RAD_IN_K
        val radMini = centerW * RAD_MINI_K
        val radInner = centerW * RAD_INNER_K
        val lettering = centerW * RIBS_POSITION
        val rect = RectF(centerW - rad, centerH - rad, centerW + rad, centerH + rad)
        val centers: List<Pair<Float, Float>> =
            vecCentres.map { pair -> Pair(pair.first * radIn + centerW, pair.second * radIn + centerH) }
    }

    private inner class DrawViewConstant(context: Context?) : Draws(context) {
        override fun onDraw(canvas: Canvas) {
//            TODO("write normal numbers")
            canvas.run {
                p.apply {
                    strokeWidth = AVERAGE_WIDTH
                    textAlign = Paint.Align.CENTER
                    color = Color.MAGENTA
                }
                for (i in 0 until curField.graph.kolNodes) {
                    for (j in 0 until curField.graph.kolNodes) {
                        if (curField.graph.data[i][j].oper != Operation.NONE && i > j)
                            drawLine(centers[i].first, centers[i].second, centers[j].first, centers[j].second, p)
                    }
                }

                p.color = Color.GREEN
                for (i in 0 until curField.graph.kolNodes) {
                    for (j in 0 until curField.graph.kolNodes) {
                        if (curField.graph.data[i][j].oper != Operation.NONE && i > j) {
                            val vec = Pair(centers[j].first - centers[i].first, centers[j].second - centers[i].second).run {
                                l2().let { Pair(first/it, second/it) }
                            }
                            p.textSize = centerW * TEXT_SIZE_K
                            if (curField.graph.data[i][j].oper in listOf(Operation.ROOT, Operation.DEGREE)) {
                                drawText(curField.graph.data[i][j].oper.opToString(), centers[i].first + lettering * vec.first,
                                    centers[i].second + lettering * vec.second - (p.descent() + p.ascent()) / 2, p)
                                drawText(curField.graph.data[j][i].oper.opToString(), centers[j].first - lettering * vec.first,
                                    centers[j].second - lettering * vec.second - (p.descent() + p.ascent()) / 2, p)
                                p.textSize = centerW * TEXT_SIZE_K2

                                class Temp(val i: Int, val j: Int, val let: Float)
                                val temp = if (curField.graph.data[i][j].oper == Operation.ROOT) Temp(i,j,lettering) else Temp(j,i,-lettering)
                                drawText(curField.graph.data[temp.i][temp.j].num.toString(), centers[temp.i].first + temp.let * vec.first - centerW * TEXT_SHIFT_ROOT_X,
                                    centers[temp.i].second + temp.let * vec.second - (p.descent() + p.ascent()) / 2 - centerW * TEXT_SHIFT_ROOT_Y, p)
                                drawText(curField.graph.data[temp.j][temp.i].num.toString(), centers[temp.j].first - temp.let * vec.first + centerW * TEXT_SHIFT_DEGREE_X,
                                    centers[temp.j].second - temp.let * vec.second - (p.descent() + p.ascent()) / 2 - centerW * TEXT_SHIFT_DEGREE_Y, p)
                            }
                            else {
                                drawText(curField.graph.data[i][j].toString(), centers[i].first + lettering * vec.first,
                                    centers[i].second + lettering * vec.second - (p.descent() + p.ascent()) / 2, p)
                                drawText(curField.graph.data[j][i].toString(), centers[j].first - lettering * vec.first,
                                    centers[j].second - lettering * vec.second - (p.descent() + p.ascent()) / 2, p)
                            }
                        }
                    }
                }

                p.apply {
                    color = Color.GRAY
                    style = Paint.Style.FILL
                }
                drawCircle(centerW, centerH, radInner, p)

                p.apply {
                    color = Color.WHITE
                    style = Paint.Style.STROKE
                }
                drawCircle(centerW, centerH, rad, p)
            }
        }
    }

    private inner class DrawView(context: Context?) : Draws(context) {
        @SuppressLint("ResourceAsColor")
        override fun onDraw(canvas: Canvas) {
            canvas.run {
                p.apply {
                    strokeWidth = SMALL_WIDTH
                    color = Color.GREEN
                    textSize = centerW * TEXT_SIZE_BIG_K
                    textAlign = Paint.Align.CENTER
                }
                if (mode == "set") { // believe that length = 3
                    (-1..1).forEach { i ->
                        drawText(curField.currentNumbers[i+1].toString(), centerW, centerH - (p.descent() + p.ascent())/2 + i * centerW * SET_TURN_K, p)
                    }
                }
                else drawText(curField.currentNumbers[0].toString(), centerW, centerH - (p.descent() + p.ascent())/2, p)

                p.apply {
                    strokeWidth = LARGE_WIDTH
                    color = R.color.color3
                    style = Paint.Style.STROKE
                }
                drawArc(rect, -90F, time * 360F / allTime, false, p)

                p.apply {
                    color = Color.YELLOW
                    style = Paint.Style.FILL
                }
                centers.forEach {
                    drawCircle(it.first, it.second, radMini, p)
                }

                p.apply {
                    color = Color.RED
                }
                drawCircle(centers[curField.currentNode].first, centers[curField.currentNode].second, radMini, p)
            }
        }
    }
}

private fun Pair<Float, Float>.skalProd(v: Pair<Float, Float>) = first * v.first + second * v.second
private fun Pair<Float, Float>.l2() = sqrt(skalProd(this))