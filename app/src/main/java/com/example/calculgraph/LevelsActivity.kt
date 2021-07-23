package com.example.calculgraph

import android.content.Intent
import android.os.Bundle
import android.view.View

//import kotlinx.android.synthetic.main.activity_levels.*

class LevelsActivity : AnyActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        prepare()
        setContentView(R.layout.activity_levels)
        setButtons()
    }

    override fun setButtons() {
        findViewById<View>(R.id.back).setOnClickListener {
            val intent = Intent(this, MainActivity :: class.java )
            startActivity(intent)
            finish()
        }
    }
}