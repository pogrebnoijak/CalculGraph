package com.example.calculgraph.activity

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import com.example.calculgraph.R
import com.example.calculgraph.enums.Sounds.*
import com.example.calculgraph.helpers.SoundPoolHelper.playSound

class LevelsActivity : AnyActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_levels)
        setTransitionActivity(R.id.levelsAll)
        setButtons()
    }

    override fun setButtons() {
        findViewById<Button>(R.id.menu).setOnClickListener {
            playSound(MENU)
            val intent = Intent(this, MainActivity :: class.java )
            startActivity(intent, transitionActivity.toBundle())
            finish()
        }
    }
}