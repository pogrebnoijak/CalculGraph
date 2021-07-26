package com.example.calculgraph.activity

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.core.view.size
import com.example.calculgraph.R
import com.example.calculgraph.dataBase.DBHelper
import com.example.calculgraph.dataBase.DBWorker
import com.example.calculgraph.enums.Computability
import com.example.calculgraph.enums.Computability.*
import com.example.calculgraph.enums.toComputability
import com.example.calculgraph.states.SettingsState


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
            val dbWorker = DBWorker()
            dbWorker.init(this)
            dbWorker.updateSettings()
            startActivity(intent)
            finish()
        }
    }

    private fun setOther() {
        fun tuningSpinner(rIdSmth: Int, list: Array<Any>, update: (String) -> Unit) {
            findViewById<Spinner>(rIdSmth).apply {
                adapter = ArrayAdapter(this@SettingsActivity, android.R.layout.simple_spinner_dropdown_item, list)
                onItemSelectedListener = object: AdapterView.OnItemSelectedListener{
                    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                        update(list[position].toString())
                    }

                    override fun onNothingSelected(parent: AdapterView<*>?) {
                        parent?.setSelection(0)
                    }
                }
            }
        }

        tuningSpinner(R.id.language, arrayOf("English", "Русский")) { updateLanguage(it) }
        tuningSpinner(R.id.topic, arrayOf("Standard", "Other")) { updateTopic(it) }
        tuningSpinner(R.id.computability, Computability.values() as Array<Any>) { updateComputability(it) }
        tuningSpinner(R.id.time, arrayOf(10, 15, 30, 60, 300, 3600)) { updateTime(it) }

        findViewById<Switch>(R.id.sound).setOnCheckedChangeListener {
            _: CompoundButton, isChecked: Boolean -> updateSound(isChecked)
        }
    }

    private fun setSettings() {
        val _settings: SettingsState = (DBHelper(this).read("settings") ?: throw error("No settings in the db")) as SettingsState

        fun getIndexByName(spin: Spinner, name: Any): Int {
            (0 until spin.count).forEach { i ->
                if (spin.getItemAtPosition(i) == name) return i
            }
            throw error("spinner error")
        }

        findViewById<Spinner>(R.id.language).let {
            it.setSelection(getIndexByName(it, _settings.language))
        }
        findViewById<Spinner>(R.id.topic).let {
            it.setSelection(getIndexByName(it, _settings.topic))
        }
        findViewById<Spinner>(R.id.computability).let {
            it.setSelection(getIndexByName(it, _settings.computability))
        }
        findViewById<Spinner>(R.id.time).let {
            it.setSelection(getIndexByName(it, _settings.time))
        }
        findViewById<Switch>(R.id.sound).isChecked = _settings.sound
    }

    private fun updateLanguage(language: String) {
        settings.language = language
//        TODO("finish this")
    }

    private fun updateSound(sound: Boolean) {
        settings.sound = sound
//        TODO("finish this")
    }

    private fun updateTopic(topic: String) {
        settings.topic = topic
//        TODO("finish this")
    }

    private fun updateComputability(compatibility: String) {
        settings.computability = compatibility.toComputability()
    }

    private fun updateTime(time: String) {
        settings.time = time.toInt()
    }
}