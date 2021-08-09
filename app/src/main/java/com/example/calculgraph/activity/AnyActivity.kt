package com.example.calculgraph.activity

import android.app.ActivityOptions
import android.os.Bundle
import android.os.Handler
import android.text.format.DateUtils
import android.view.View
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import com.example.calculgraph.R
import com.example.calculgraph.enums.Theme.*
import com.example.calculgraph.states.PreGenerationState
import com.example.calculgraph.states.SettingsState


abstract class AnyActivity : AppCompatActivity() {
    companion object {
        lateinit var settings: SettingsState
        lateinit var preGen: PreGenerationState
    }

    protected val handler = Handler()
    protected lateinit var transitionActivity: ActivityOptions

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme()
        super.onCreate(savedInstanceState)
        prepare()
    }

    override fun finish() { handler.postDelayed({ super.finish() }, DateUtils.SECOND_IN_MILLIS / 2) }

    protected open fun setButtons() {}

    private fun prepare() {
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
        )
        window.decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_IMMERSIVE
                or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_FULLSCREEN)
    }

    private fun setTheme() {
        when (settings.theme) {
            STANDARD -> setTheme(R.style.Theme_CalculGraph)
            OTHER -> setTheme(R.style.Theme_CalculGraph2)
        }
    }

    protected fun setTransitionActivity(id: Int) {
        transitionActivity = ActivityOptions.makeSceneTransitionAnimation(this, findViewById(id), "")
    }
}

