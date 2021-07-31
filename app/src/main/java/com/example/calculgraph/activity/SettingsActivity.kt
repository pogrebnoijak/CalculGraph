package com.example.calculgraph.activity

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.core.widget.doAfterTextChanged
import com.example.calculgraph.R
import com.example.calculgraph.constant.*
import com.example.calculgraph.dataBase.DBHelper
import com.example.calculgraph.dataBase.DBWorker
import com.example.calculgraph.enums.*
import com.example.calculgraph.enums.Computability.*
import com.example.calculgraph.helpers.TimeWorking.showTime
import com.example.calculgraph.helpers.TimeWorking.toTime
import com.example.calculgraph.service.GraphGeneratorService.Companion.updatePreGen
import com.example.calculgraph.states.SettingsState


class SettingsActivity : AnyActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
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
            updatePreGen(this@SettingsActivity) {
                startActivity(intent)
                finish()
            }
        }
    }

    private fun setOther() {
        fun tuningSpinner(id: Int, list: Array<Any>, update: (String) -> Unit) {
            findViewById<Spinner>(id).apply {
                adapter = ArrayAdapter(this@SettingsActivity, R.layout.spinner, R.id.sp, list)
                onItemSelectedListener = object: AdapterView.OnItemSelectedListener{
                    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                        update(list[position].toString())
                    }

                    override fun onNothingSelected(parent: AdapterView<*>?) {}
                }
            }
        }

        findViewById<Switch>(R.id.sound).setOnCheckedChangeListener {
                _: CompoundButton, isChecked: Boolean -> updateSound(isChecked)
        }
        tuningSpinner(R.id.language, Array(LANGUAGES.size) { LANGUAGES[it] } ) { updateLanguage(it) }
        tuningSpinner(R.id.topic, Array(topicValues().size) { topicValues()[it] } ) { updateTopic(it) }
        tuningSpinner(R.id.computability, Array(Computability.values().size) { Computability.values()[it] } ) { updateComputability(it) }
        findViewById<EditText>(R.id.moves).apply {
            doAfterTextChanged { text ->
                text.toString().let {
                    if(it.isNotEmpty()) when {
                        it.toInt() > MAX_MOVES  -> setText(MAX_MOVES.toString())
                        it.toInt() < MIN_MOVES  -> setText(MIN_MOVES.toString())
                        else                    -> updateMoves(it.toInt())
                    }
                }
            }
        }
        tuningSpinner(R.id.time, Array(TIMES.size) { showTime(TIMES[it]) }) { updateTime(it) }
    }

    private fun setSettings() {
        val (sound, language, topic, computability, moves, time) = (DBHelper(this).read("settings") ?: throw error("No settings in the db")) as SettingsState

        fun getIndexByName(spin: Spinner, name: Any): Int {
            (0 until spin.count).forEach { i ->
                if (spin.getItemAtPosition(i) == name) return i
            }
            throw error("spinner error")
        }

        findViewById<Switch>(R.id.sound).isChecked = sound
        findViewById<Spinner>(R.id.language).let {
            it.setSelection(getIndexByName(it, language))
        }
        findViewById<Spinner>(R.id.topic).let {
            it.setSelection(getIndexByName(it, topic.topToString()))
        }
        findViewById<Spinner>(R.id.computability).let {
            it.setSelection(getIndexByName(it, computability))
        }
        findViewById<EditText>(R.id.moves).setText(moves.toString())
        findViewById<Spinner>(R.id.time).let {
            it.setSelection(getIndexByName(it, showTime(time)))
        }
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
        settings.topic = topic.toTopic()
//        TODO("finish this")
    }

    private fun updateComputability(compatibility: String) {
        settings.computability = compatibility.toComputability()
    }

    private fun updateTime(time: String) {
        settings.time = time.toTime()
    }

    private fun updateMoves(moves: Int) {
        settings.moves = moves
    }
}
