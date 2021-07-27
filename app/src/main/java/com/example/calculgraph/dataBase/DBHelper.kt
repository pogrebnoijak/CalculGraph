package com.example.calculgraph.dataBase

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log
import com.example.calculgraph.enums.toComputability
import com.example.calculgraph.enums.toTopic
import com.example.calculgraph.enums.topToString
import com.example.calculgraph.playField.Graph
import com.example.calculgraph.serializer.Serializer
import com.example.calculgraph.states.SaveState
import com.example.calculgraph.states.SettingsState
import com.example.calculgraph.states.State
import com.example.calculgraph.states.StatisticState
import com.google.gson.reflect.TypeToken


class DBHelper(context: Context) : SQLiteOpenHelper(context, "DBHelper", null, 1) {
    private val LOG_TAG = "DBLog"

    override fun onCreate(db: SQLiteDatabase) {
        Log.d(LOG_TAG, "--- onCreate statistic ---")
        db.execSQL(
            ("create table IF NOT EXISTS statistic ("
                    + "id integer primary key autoincrement,"
                    + "kolGame integer,"
                    + "sredScore double,"
                    + "maxScore integer" + ");")
        )

        Log.d(LOG_TAG, "--- onCreate settings ---")
        db.execSQL(
            ("create table IF NOT EXISTS settings ("
                    + "id integer primary key autoincrement,"
                    + "sound integer,"
                    + "language text,"
                    + "topic text,"
                    + "computability text,"
                    + "moves integer,"
                    + "time integer" + ");")
        )

        Log.d(LOG_TAG, "--- onCreate saveState ---")
        db.execSQL(
            ("create table IF NOT EXISTS saveState ("
                    + "id integer primary key autoincrement,"
                    + "endGame integer,"
                    + "time integer,"
                    + "allTime integer,"
                    + "score integer,"
                    + "kolMoves integer,"
                    + "currentNode integer,"
                    + "mode text,"
                    + "currentNumbers blob,"
                    + "totalNumbers blob,"
                    + "history blob,"
                    + "answer blob,"
                    + "data blob" + ");")
        )
        addDefaults(db)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {}

    private fun addDefaults(db: SQLiteDatabase) {
        val cvStat = ContentValues()
        cvStat.put("kolGame", 0)
        cvStat.put("sredScore", 0.0)
        cvStat.put("maxScore", 0)
        db.insert("statistic", null, cvStat)

        val cvSett = ContentValues()
        cvSett.put("sound", 1)
        cvSett.put("language", "English")
        cvSett.put("topic", "Standard")
        cvSett.put("computability", "EASY")
        cvSett.put("moves", 3)
        cvSett.put("time", 60)
        db.insert("settings", null, cvSett)

        val emptyList = Serializer.listToBytes(listOf())
        val cvSave = ContentValues()
        cvSave.put("endGame", 1)
        cvSave.put("time", 0)
        cvSave.put("allTime", 0)
        cvSave.put("score", 0)
        cvSave.put("kolMoves", 0)
        cvSave.put("currentNode", 0)
        cvSave.put("mode", "")
        cvSave.put("currentNumbers", emptyList)
        cvSave.put("totalNumbers", emptyList)
        cvSave.put("history", emptyList)
        cvSave.put("answer", emptyList)
        cvSave.put("data", emptyList)
        db.insert("saveState", null, cvSave)
    }

    fun update(state: State) {
        val cv = ContentValues()
        val db = writableDatabase
        val rowID: Int
        lateinit var tableName: String
        when (state) {
            is StatisticState -> {
                tableName = "statistic"
                Log.d(LOG_TAG, "--- Update in $tableName: ---")

                cv.put("kolGame", state.kolGame)
                cv.put("sredScore", state.sredScore)
                cv.put("maxScore", state.maxScore)
            }

            is SettingsState -> {
                tableName = "settings"
                Log.d(LOG_TAG, "--- Update in $tableName: ---")

                cv.put("sound", state.sound)
                cv.put("language", state.language)
                cv.put("topic", state.topic.topToString())
                cv.put("computability", state.computability.toString())
                cv.put("moves", state.moves)
                cv.put("time", state.time)
            }

            is SaveState -> {
                tableName = "saveState"
                Log.d(LOG_TAG, "--- Update in $tableName: ---")

                cv.put("endGame", state.endGame)
                cv.put("time", state.time)
                cv.put("allTime", state.allTime)
                cv.put("score", state.score)
                cv.put("kolMoves", state.kolMoves)
                cv.put("currentNode", state.currentNode)
                cv.put("mode", state.mode)
                cv.put("currentNumbers", Serializer.listToBytes(state.currentNumbers))
                cv.put("totalNumbers", Serializer.listToBytes(state.totalNumbers))
                cv.put("history", Serializer.listToBytes(state.history))
                cv.put("answer", Serializer.listToBytes(state.answer))
                cv.put("data", Serializer.listToBytes(state.data))
            }
        }
        rowID = db.update(tableName, cv, null, null)
        Log.d(LOG_TAG, "row updated, ID = $rowID")
        close()
    }

    fun read(table: String) : State? {
        val list: Array<String> = when(table) {
            "statistic" -> arrayOf("id", "kolGame", "sredScore", "maxScore")
            "settings"  -> arrayOf("id", "sound", "language", "topic", "computability", "moves", "time")
            "saveState"  -> arrayOf("id", "endGame", "time", "allTime","score", "kolMoves", "currentNode",
                "mode", "currentNumbers", "totalNumbers", "history", "answer", "data")
            else        -> throw error("wrong table name")
        }

        val db = writableDatabase
        val state: State?
        Log.d(LOG_TAG, "--- Read in $table: ---")
        val cv = db.query(table, list, null, null, null, null, null)

        state = if (!cv.moveToNext()) null else when(table) {
            "statistic" -> {
                StatisticState(cv.getInt(cv.getColumnIndex("kolGame")),
                    cv.getDouble(cv.getColumnIndex("sredScore")),
                    cv.getInt(cv.getColumnIndex("maxScore")))
            }
            "settings"  -> {
                SettingsState(cv.getInt(cv.getColumnIndex("sound")) != 0,
                    cv.getString(cv.getColumnIndex("language")),
                    cv.getString(cv.getColumnIndex("topic")).toTopic(),
                    cv.getString(cv.getColumnIndex("computability")).toComputability(),
                    cv.getInt(cv.getColumnIndex("moves")),
                    cv.getInt(cv.getColumnIndex("time")))
            }
            "saveState" -> {
                SaveState(cv.getInt(cv.getColumnIndex("endGame")) != 0,
                    cv.getLong(cv.getColumnIndex("time")),
                    cv.getLong(cv.getColumnIndex("allTime")),
                    cv.getInt(cv.getColumnIndex("score")),
                    cv.getInt(cv.getColumnIndex("kolMoves")),
                    cv.getInt(cv.getColumnIndex("currentNode")),
                    cv.getString(cv.getColumnIndex("mode")),
                    Serializer.bytesToList(cv.getBlob(cv.getColumnIndex("currentNumbers")),
                        object : TypeToken<List<Int>>() {}.type) as List<Int>,
                    Serializer.bytesToList(cv.getBlob(cv.getColumnIndex("totalNumbers")),
                        object : TypeToken<List<Int>>() {}.type) as List<Int>,
                    Serializer.bytesToList(cv.getBlob(cv.getColumnIndex("history")),
                        object : TypeToken<List<Int>>() {}.type) as List<Int>,
                    Serializer.bytesToList(cv.getBlob(cv.getColumnIndex("answer")),
                        object : TypeToken<List<Int>>() {}.type) as List<Int>,
                    Serializer.bytesToList(cv.getBlob(cv.getColumnIndex("data")),
                        object : TypeToken<List<List<Graph.Inscription>>>() {}.type) as List<List<Graph.Inscription>>
                )
            }
            else        -> throw error("wrong table name")
        }

        cv.close()
        db.close()
        return state
    }
}