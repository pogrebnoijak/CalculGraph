package com.example.calculgraph.activity

import android.os.Bundle
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
//            set(value) = if (field == null) field = value else throw error("no change settings")

        lateinit var preGen: PreGenerationState
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme()
        super.onCreate(savedInstanceState)
        prepare()
    }

    open fun setButtons() {}

    open fun prepare() {
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
}

