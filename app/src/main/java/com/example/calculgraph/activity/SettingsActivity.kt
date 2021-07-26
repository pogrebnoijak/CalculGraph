package com.example.calculgraph.activity

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.Spinner
import com.example.calculgraph.R

class SettingsActivity : AnyActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        prepare()
        setContentView(R.layout.activity_settings)
        setButtons()
        setOther()
        setSettings()
    }

    override fun setButtons() {
        findViewById<Button>(R.id.menu).setOnClickListener {
            val intent = Intent(this, MainActivity :: class.java )
            startActivity(intent)
            finish()
        }
    }

    private fun setOther() {
        val listLanguages = arrayOf("English", "Русский")
        val langs = findViewById<Spinner>(R.id.language)
        langs.adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, listLanguages)
        langs.onItemSelectedListener = object: AdapterView.OnItemSelectedListener{
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                val language = listLanguages[position]
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                parent?.setSelection(0)
            }
        }
    }

    private fun setSettings() {

    }
}