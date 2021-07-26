package com.example.calculgraph.activity

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import com.example.calculgraph.R

class LevelsActivity : AnyActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        prepare()
        setContentView(R.layout.activity_levels)
        setButtons()
    }

    override fun setButtons() {
        findViewById<Button>(R.id.menu).setOnClickListener {
            val intent = Intent(this, MainActivity :: class.java )
            startActivity(intent)
            finish()
        }
    }
}