package com.example.calculgraph.activity

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.DisplayMetrics
import android.view.View
import android.widget.*
import com.example.calculgraph.R
import com.example.calculgraph.enums.Sounds.*
import com.example.calculgraph.helpers.SoundPoolHelper.playSound
import com.example.calculgraph.constant.*
import com.example.calculgraph.enums.Computability
import com.example.calculgraph.enums.Computability.*
import com.example.calculgraph.helpers.Size


class LevelsActivity : AnyActivity() {
    private val size: Size
        get() {
            val metrics = DisplayMetrics()
            windowManager.defaultDisplay.getMetrics(metrics)
            return Size(metrics.widthPixels * 1F, metrics.heightPixels * LAYOUT_CONST_LEVELS)
        }
    private var sideSize = MAGIC * 1F
    private var mode = MODES.first()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_levels)
        setTransitionActivity(R.id.levelsAll)
        setButtons()
        setOther()
    }

    override fun setButtons() {
        findViewById<Button>(R.id.menu).setOnClickListener {
            playSound(MENU)
            val intent = Intent(this, MainActivity :: class.java )
            startActivity(intent, transitionActivity.toBundle())
            finish()
        }
        sideSize = size.width * LEVEL_BUTTON_SIZE_K
        addLevelsButtons()
    }

    private fun addLevelsButtons() {
        fun layoutId(computability: Computability) = when(computability) {
            EASY    -> R.id.levelsEasy
            MEDIUM  -> R.id.levelsMedium
            HARD    -> R.id.levelsHard
            INSANE  -> R.id.levelsInsane
        }
        val height = (((KOL_LEVELS / KOL_LEVELS_IN_LINE) * LEVEL_BUTTON_SIZE_AND_DIF + LEVEL_BUTTON_DIF) * size.width).toInt()

        Computability.values().forEach { comp ->
            val id = layoutId(comp)
            findViewById<RelativeLayout>(id).layoutParams.height = height
            (1..KOL_LEVELS).forEach { num ->
                addLevelsButton(num, comp, id)
            }
        }
    }

    @SuppressLint("ResourceType")
    private fun addLevelsButton(num: Int, computability: Computability, layoutId: Int) {
        val p: Pair<Float, Float> = Pair(
            (LEVEL_BUTTON_DIF + ((num - 1) % KOL_LEVELS_IN_LINE) * LEVEL_BUTTON_SIZE_AND_DIF) * size.width,
            (LEVEL_BUTTON_DIF + ((num - 1) / KOL_LEVELS_IN_LINE) * LEVEL_BUTTON_SIZE_AND_DIF) * size.width)

        findViewById<RelativeLayout>(layoutId).addView(
            Button(this).apply {
                id = num + Computability.values().indexOf(computability) * KOL_LEVELS
                text = num.toString()
                layoutParams = LinearLayout.LayoutParams(sideSize.toInt(), sideSize.toInt()).apply {
                    leftMargin = p.first.toInt()
                    topMargin = p.second.toInt()
                }
                setOnClickListener { println("click $computability-$num") }
            }
        )
    }

    private fun setOther() {
        findViewById<Spinner>(R.id.modeLevels).apply {
            adapter = ArrayAdapter(this@LevelsActivity, R.layout.spinner, R.id.sp, MODES)
            onItemSelectedListener = object: AdapterView.OnItemSelectedListener {
                override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                    playSound(SHIFT)
                    mode = MODES[position]
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {}
            }
        }
    }
}