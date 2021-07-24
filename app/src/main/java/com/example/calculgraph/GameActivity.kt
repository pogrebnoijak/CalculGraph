package com.example.calculgraph

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.graphics.*
import android.os.Bundle
import android.util.DisplayMetrics
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import java.util.*
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin


class GameActivity : AnyActivity() {
    private val drawing = 10L       // ms
    private var play = true
    private lateinit var background: DrawView
    private lateinit var curField: Field
    private var allTime = 60_000L   // ms
    private var time = 0L           // ms

    inner class Size(val width: Float, val height: Float)
    val size: Size
        get() {
            val metrics = DisplayMetrics()
            windowManager.defaultDisplay.getMetrics(metrics)
            return Size(metrics.widthPixels * 1F, metrics.heightPixels * 0.7F)
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        prepare()
        setContentView(R.layout.activity_game)
        setButtons()
        getIntents()
        addDraws()
        doField()
        setRendering()
    }

    override fun setButtons() {
        findViewById<Button>(R.id.menu).setOnClickListener {
            val intent = Intent(this, MainActivity :: class.java )
            play = false
            startActivity(intent)
            finish()
        }
        findViewById<Button>(R.id.back).setOnClickListener {
            val intent = Intent(this, MainActivity :: class.java )
            startActivity(intent)
            finish()
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
        curField = Field(2,4)
        findViewById<TextView>(R.id.kolMoves).text = "moves: ${curField.kolMoves}"
        findViewById<TextView>(R.id.totalNumber).text = "need to get ${curField.totalNumber}!"
    }

    private fun setRendering() {
        val timer = Timer()
        val motion = object : TimerTask() {
            override fun run() {
                if(play) {
                    time += drawing
                    background.invalidate()
                    if (time >= allTime) play = false
                }
            }
        }
        timer.schedule(motion, 0, drawing)
    }


    abstract inner class Draws(context: Context?) : View(context) {
        val p: Paint = Paint()
        val centerH = size.height / 2
        val centerW = size.width / 2
        val rad = centerW * 0.9F
        val radIn = centerW * 0.7F
        val radMini = centerW * 0.1F
        val radInner = centerW * 0.3F
        val rect = RectF(centerW - rad, centerH - rad, centerW + rad, centerH + rad)
        val centers: List<Pair<Float, Float>>
            get() = (0 until curField.graph.kolNode).map { 2.0 * it / curField.graph.kolNode }
                .map { Pair((centerW + radIn * cos(PI * it)).toFloat(), (centerH + radIn * sin(PI * it)).toFloat()) }
                .toList()
    }

    private inner class DrawViewConstant(context: Context?) : Draws(context) {
        override fun onDraw(canvas: Canvas) {
            canvas.run {
                p.apply {
                    color = Color.MAGENTA
                    strokeWidth = 5F
                }
                for (i in 0 until curField.graph.kolNode) {
                    for (j in 0 until curField.graph.kolNode) {
                        if (curField.graph.data[i][j].oper == Operation.NONE) continue
                        if (i > j) drawLine(centers[i].first, centers[i].second, centers[j].first, centers[j].second, p)
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
        override fun onDraw(canvas: Canvas) {
            canvas.run {
                p.apply {
                    strokeWidth = 3F
                    color = Color.GREEN
                    textSize = centerW * 0.1F
                    textAlign = Paint.Align.CENTER
                }
                drawText(curField.currentNumber.toString(), centerW, centerH - (p.descent() + p.ascent())/2, p)

                p.apply {
                    strokeWidth = 7F
                    color = Color.rgb(50, 50, 140)
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
