package com.example.calculgraph.activity

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.os.Bundle
import android.util.DisplayMetrics
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import com.example.calculgraph.R
import com.example.calculgraph.constant.*
import com.example.calculgraph.dataBase.DBWorker
import com.example.calculgraph.enums.*
import com.example.calculgraph.helpers.Size
import com.example.calculgraph.helpers.SoundPoolHelper.playSound
import com.example.calculgraph.playField.Field
import java.util.*
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.sqrt


// TODO("rewrite")
abstract class AnyGameActivity : AnyActivity() {
    protected val size: Size
        get() {
            val metrics = DisplayMetrics()
            windowManager.defaultDisplay.getMetrics(metrics)
            return Size(metrics.widthPixels * 1F, metrics.heightPixels * LAYOUT_CONST)
        }
    protected lateinit var computability: Computability
    protected val timer = Timer()
    protected val dbWorker = DBWorker()
    protected var motion: TimerTask = object: TimerTask() { override fun run() {} }
    protected lateinit var background: Draws
    protected lateinit var curField: Field
    protected lateinit var vecCentres: List<Pair<Float, Float>>
    protected lateinit var touchDown: Pair<Float, Float>
    protected lateinit var mode: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game)
        setTransitionActivity(R.id.gameAll)
        window.enterTransition = null
    }


    protected fun sets() {
        writeField()
        setButtons()
        addDraws()
        setRendering()
    }

    @SuppressLint("SetTextI18n")
    protected open fun writeField() {
        findViewById<TextView>(R.id.kolMoves).text = getString(R.string.updateMoves, curField.kolMoves)
        val str = when(mode) {
            "standard" -> curField.totalNumbers[0].toString()
            "set" -> curField.totalNumbers.toString()
            "max" -> getString(R.string.max)
            else -> throw error("wrong mode!")
        }
        findViewById<TextView>(R.id.totalNumber).text = getString(R.string.need, str)
        vecCentres = (0 until curField.graph.kolNodes).map { 2.0 * it / curField.graph.kolNodes }
            .map { Pair(cos(PI * it).toFloat(), sin(PI * it).toFloat()) }
    }

    @SuppressLint("SetTextI18n")
    override fun setButtons() {
        findViewById<Button>(R.id.menu).setOnClickListener {
            playSound(Sounds.MENU)
            exitGame()
        }
        findViewById<Button>(R.id.back).setOnClickListener {
            curField.back()
            findViewById<TextView>(R.id.kolMoves).text = getString(R.string.updateMoves, curField.kolMoves)
        }
    }

    protected open fun addDraws() {
        findViewById<ConstraintLayout>(R.id.draw).removeAllViews()
        findViewById<ConstraintLayout>(R.id.draw).addView(DrawViewConstant(this@AnyGameActivity))
    }

    protected open fun setRendering() {
        motion.cancel()
        motion = object : TimerTask() { override fun run() { background.invalidate() } }
        timer.schedule(motion, 0, DRAWING)
    }

    protected fun treatment(start: Pair<Float, Float>, end: Pair<Float, Float>) {
        operator fun Pair<Float, Float>.minus(v: Pair<Float, Float>) = Pair(first - v.first, second - v.second)
        fun angle(v1: Pair<Float, Float>, v2: Pair<Float, Float>) = // cos
            v1.skalProd(v2) / (v1.l2() * v2.l2())  // v1, v2 not 0

        if ((end - start).l2() > CASUAL_MOVE) {
            curField.graph.data[curField.currentNode]
                .mapIndexed { i, inscription -> Pair(i, inscription) }
                .filter { it.second.oper != Operation.NONE }
                .map { Pair(it.first, angle(end - start, vecCentres[it.first] - vecCentres[curField.currentNode])) }
                .filter { it.second >= cos(THRESHOLD_ANGLE) }
                .maxByOrNull { it.second }
                ?.first
                ?.let { move(it) }
        }
    }

    @SuppressLint("SetTextI18n")
    protected open fun move(to: Int) {}

    protected open fun endGame() = runOnUiThread { doDialogEnd() }

    @SuppressLint("SetTextI18n")
    protected open fun doDialogEnd() {}

    protected open fun exitGame() {}


    // Draws

    abstract inner class Draws(context: Context?) : View(context) {
        val p: Paint = Paint()
        val centerH = size.height / 2
        val centerW = size.width / 2
        val rad = centerW * RAD_K
        private val radIn = centerW * RAD_IN_K
        val radMini = centerW * RAD_MINI_K
        val radInner = centerW * RAD_INNER_K
        val lettering = centerW * RIBS_POSITION
        val rect = RectF(centerW - rad, centerH - rad, centerW + rad, centerH + rad)
        val centers: List<Pair<Float, Float>> =
            vecCentres.map { pair -> Pair(pair.first * radIn + centerW, pair.second * radIn + centerH) }
    }

    protected inner class DrawViewConstant(context: Context?) : Draws(context) {
        @SuppressLint("DrawAllocation")
        override fun onDraw(canvas: Canvas) {
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

                p.apply {
                    color = Color.GREEN
                    strokeWidth = SMALL_WIDTH
                }
                for (i in 0 until curField.graph.kolNodes) {
                    for (j in 0 until curField.graph.kolNodes) {
                        if (curField.graph.data[i][j].oper != Operation.NONE && i > j) {
                            val vec = Pair(centers[j].first - centers[i].first, centers[j].second - centers[i].second).run {
                                l2().let { Pair(first/it, second/it) }
                            }
                            p.textSize = centerW * TEXT_SIZE_K
                            if (curField.graph.data[i][j].oper in listOf(
                                    Operation.ROOT,
                                    Operation.DEGREE
                                )) {
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

    abstract inner class DrawHelper(context: Context?) : Draws(context) {
        fun drawText(canvas: Canvas) {
            p.apply {
                strokeWidth = SMALL_WIDTH
                color = Color.GREEN
                textSize = centerW * TEXT_SIZE_BIG_K
                textAlign = Paint.Align.CENTER
            }

            if (mode == "set") { // believe that length = 3
                (-1..1).forEach { i ->
                    canvas.drawText(curField.currentNumbers[i+1].toString(), centerW, centerH - (p.descent() + p.ascent())/2 + i * centerW * SET_TURN_K, p)
                }
            }
            else canvas.drawText(curField.currentNumbers[0].toString(), centerW, centerH - (p.descent() + p.ascent())/2, p)
        }

        fun drawCircles(canvas: Canvas) {
            p.apply {
                strokeWidth = LARGE_WIDTH
                color = Color.YELLOW
                style = Paint.Style.FILL
            }
            centers.forEach {
                canvas.drawCircle(it.first, it.second, radMini, p)
            }
        }

        fun drawPosCircle(canvas: Canvas, pair: Pair<Float, Float>) {
            p.color = Color.RED
            canvas.drawCircle(pair.first, pair.second, radMini, p)
        }
    }
}

internal fun Pair<Float, Float>.skalProd(v: Pair<Float, Float>) = first * v.first + second * v.second
internal fun Pair<Float, Float>.l2() = sqrt(skalProd(this))
