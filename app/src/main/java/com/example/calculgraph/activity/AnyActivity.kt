package com.example.calculgraph.activity

import android.view.View
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import com.example.calculgraph.states.SettingsState

abstract class AnyActivity : AppCompatActivity() {
    companion object {
        lateinit var settings: SettingsState
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
}

