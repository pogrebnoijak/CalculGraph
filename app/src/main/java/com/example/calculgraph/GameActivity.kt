package com.example.calculgraph

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.graphics.*
import android.os.Bundle
import android.util.DisplayMetrics
import android.view.MotionEvent
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.MotionEventCompat
import com.example.calculgraph.constant.*
import java.util.*
import kotlin.math.*


class GameActivity : AnyActivity() {
    private var play = true
    private var time = 0L           // ms
    private lateinit var background: DrawView
    private lateinit var curField: Field
    private lateinit var vecCentres: List<Pair<Float, Float>>
    private lateinit var touchDown: Pair<Float, Float>

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
        setButtons()
        getIntents()
        doField()
        addDraws()
        setRendering()
    }

    override fun onTouchEvent(event: MotionEvent) = when (MotionEventCompat.getActionMasked(event)) {
            MotionEvent.ACTION_DOWN -> {
                touchDown = Pair(event.rawX, event.rawY)
                true
            }
            MotionEvent.ACTION_UP -> {
                treatment(touchDown, Pair(event.rawX, event.rawY))
                true
            }
            else -> super.onTouchEvent(event)
        }

    override fun setButtons() {
        findViewById<Button>(R.id.menu).setOnClickListener {
            val intent = Intent(this, MainActivity :: class.java )
            play = false
            startActivity(intent)
            finish()
        }
        findViewById<Button>(R.id.back).setOnClickListener {
            curField.back()
        }
    }

    private fun getIntents() {
        val a = intent.getStringExtra("mode")
    }

    private fun addDraws() {
        findViewById<ConstraintLayout>(R.id.draw).addView(DrawViewConstant(this))
        background = DrawView(this)
        findViewById<ConstraintLayout>(R.id.draw).addView(background)
    }

    @SuppressLint("SetTextI18n")
    private fun doField() {
        curField = Field(KOL_MOVES,KOL_NODES)
        findViewById<TextView>(R.id.kolMoves).text = "moves: ${curField.kolMoves}"
        findViewById<TextView>(R.id.totalNumber).text = "need to get ${curField.totalNumber}"
        vecCentres = (0 until curField.graph.kolNode).map { 2.0 * it / curField.graph.kolNode }
            .map { Pair(cos(PI * it).toFloat(), sin(PI * it).toFloat()) }
    }

    private fun setRendering() {
        val timer = Timer()
        val motion = object : TimerTask() {
            override fun run() {
                if(play) {
                    time += DRAWING
                    background.invalidate()
                    if (time >= ALL_TIME) play = false
                }
            }
        }
        timer.schedule(motion, 0, DRAWING)
    }

    private fun treatment(start: Pair<Float, Float>, end: Pair<Float, Float>) {
        operator fun Pair<Float, Float>.minus(v: Pair<Float, Float>) = Pair(first - v.first, second - v.second)
        fun Pair<Float, Float>.skalProd(v: Pair<Float, Float>) = first * v.first + second * v.second
        fun Pair<Float, Float>.l2() = sqrt(skalProd(this))
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
                ?.let { curField.move(it) }
        }
    }


    abstract inner class Draws(context: Context?) : View(context) {
        val p: Paint = Paint()
        val centerH = size.height / 2
        val centerW = size.width / 2
        val rad = centerW * RAD_K
        val radIn = centerW * RAD_IN_K
        val radMini = centerW * RAD_MINI_K
        val radInner = centerW * RAD_INNER_K
        val rect = RectF(centerW - rad, centerH - rad, centerW + rad, centerH + rad)
        val centers: List<Pair<Float, Float>> =
            vecCentres.map { pair -> Pair(pair.first * radIn + centerW, pair.second * radIn + centerH) }
    }

    private inner class DrawViewConstant(context: Context?) : Draws(context) {
        override fun onDraw(canvas: Canvas) {
            canvas.run {
                p.apply {
                    strokeWidth = AVERAGE_WIDTH
                    textSize = centerW * TEXT_SIZE_K
                    textAlign = Paint.Align.CENTER
                }
                for (i in 0 until curField.graph.kolNode) {
                    for (j in 0 until curField.graph.kolNode) {
                        if (curField.graph.data[i][j].oper == Operation.NONE) continue
                        if (i > j) {
                            p.color = Color.MAGENTA
                            drawLine(centers[i].first, centers[i].second, centers[j].first, centers[j].second, p)
                            val vec = Pair(centers[j].first - centers[i].first, centers[j].second - centers[i].second) // it's so normal to calculate because it's constant
                            p.color = Color.GREEN
//                            TODO("write normal root")
                            drawText(curField.graph.data[i][j].toString(), centers[i].first + RIBS_POSITION * vec.first,
                                centers[i].second + RIBS_POSITION * vec.second - (p.descent() + p.ascent())/2, p)
                            drawText(curField.graph.data[j][i].toString(), centers[i].first + (1 - RIBS_POSITION) * vec.first,
                                centers[i].second + (1 - RIBS_POSITION) * vec.second - (p.descent() + p.ascent())/2, p)
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
                drawText(curField.currentNumber.toString(), centerW, centerH - (p.descent() + p.ascent())/2, p)

                p.apply {
                    strokeWidth = LARGE_WIDTH
                    color = R.color.color3
                    style = Paint.Style.STROKE
                }
                drawArc(rect, -90F, time * 360F / ALL_TIME, false, p)

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
