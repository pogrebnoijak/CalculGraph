package com.example.calculgraph.dataBase

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log
import com.example.calculgraph.constant.DEFAULT_ID
import com.example.calculgraph.constant.MAX_ID
import com.example.calculgraph.enums.*
import com.example.calculgraph.helpers.Serializer
import com.example.calculgraph.states.*
import com.google.gson.reflect.TypeToken


class DBHelper(context: Context) : SQLiteOpenHelper(context, "DBHelper", null, 1) {
    private val logTag = "DBLog"

    override fun onCreate(db: SQLiteDatabase) {
        Log.d(logTag, "--- onCreate statistic ---")
        db.execSQL(
            ("create table IF NOT EXISTS statistic ("
                    + "id integer primary key autoincrement,"
                    + "kolGame integer,"
                    + "sredScore double,"
                    + "maxScore integer" + ");")
        )

        Log.d(logTag, "--- onCreate settings ---")
        db.execSQL(
            ("create table IF NOT EXISTS settings ("
                    + "id integer primary key autoincrement,"
                    + "sound integer,"
                    + "language text,"
                    + "theme text,"
                    + "computability text,"
                    + "moves integer,"
                    + "time integer" + ");")
        )

        Log.d(logTag, "--- onCreate saveState ---")
        db.execSQL(
            ("create table IF NOT EXISTS saveState ("
                    + "id integer primary key autoincrement,"
                    + "gameStatus text,"
                    + "time integer,"
                    + "allTime integer,"
                    + "score integer,"
                    + "kolMoves integer,"
                    + "computability text,"
                    + "currentNode integer,"
                    + "mode text,"
                    + "currentNumbers blob,"
                    + "totalNumbers blob,"
                    + "history blob,"
                    + "answer blob,"
                    + "data blob" + ");")
        )

        Log.d(logTag, "--- onCreate levels ---")
        db.execSQL(
            ("create table IF NOT EXISTS levels ("
                    + "id integer primary key autoincrement,"
                    + "kolMoves integer,"
                    + "currentNode integer,"
                    + "numbers blob,"
                    + "totalNumbers blob,"
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
        repeat(MAX_ID) { db.insert("statistic", null, cvStat) }

        val cvSett = ContentValues()
        cvSett.put("sound", 1)
        cvSett.put("language", "English")
        cvSett.put("theme", "Standard")
        cvSett.put("computability", "EASY")
        cvSett.put("moves", 3)
        cvSett.put("time", 60)
        db.insert("settings", null, cvSett)

        val emptyList = Serializer.listToBytes(listOf())
        val cvSave = ContentValues()
        cvSave.put("gameStatus", "PLAY")
        cvSave.put("time", 0)
        cvSave.put("allTime", 60_000L)
        cvSave.put("score", 0)
        cvSave.put("kolMoves", 3)
        cvSave.put("computability", "EASY")
        cvSave.put("currentNode", 0)
        cvSave.put("mode", "standard")
        cvSave.put("currentNumbers", emptyList)
        cvSave.put("totalNumbers", emptyList)
        cvSave.put("history", emptyList)
        cvSave.put("answer", Serializer.listToBytes(listOf(0,0,0,0))) // for default count Moves
        cvSave.put("data", emptyList)
        db.insert("saveState", null, cvSave)

        val cvLevels = ContentValues()
        cvLevels.put("kolMoves", 2)
        cvLevels.put("currentNode", 1)
        cvLevels.put("numbers", Serializer.listToBytes(listOf(1)))
        cvLevels.put("totalNumbers", Serializer.listToBytes(listOf(3)))
        cvLevels.put("data", Serializer.listToBytes(MutableList(4) {
            MutableList(4) {
                Inscription(Operation.PLUS, 1)
            }
        }))
        repeat(200) { db.insert("levels", null, cvLevels) }
    }

    fun update(state: State, id: Int = DEFAULT_ID) {
        val cv = ContentValues()
        val db = writableDatabase
        val rowID: Int
        lateinit var tableName: String
        when (state) {
            is StatisticState -> {
                tableName = "statistic"
                Log.d(logTag, "--- Update in $tableName: ---")

                cv.put("kolGame", state.kolGame)
                cv.put("sredScore", state.sredScore)
                cv.put("maxScore", state.maxScore)
            }

            is SettingsState -> {
                tableName = "settings"
                Log.d(logTag, "--- Update in $tableName: ---")

                cv.put("sound", state.sound)
                cv.put("language", state.language)
                cv.put("theme", state.theme.thToString())
                cv.put("computability", state.computability.toString())
                cv.put("moves", state.moves)
                cv.put("time", state.time)
            }

            is SaveState -> {
                tableName = "saveState"
                Log.d(logTag, "--- Update in $tableName: ---")

                cv.put("gameStatus", state.gameStatus.toString())
                cv.put("time", state.time)
                cv.put("allTime", state.allTime)
                cv.put("score", state.score)
                cv.put("kolMoves", state.kolMoves)
                cv.put("computability", state.computability.toString())
                cv.put("currentNode", state.currentNode)
                cv.put("mode", state.mode)
                cv.put("currentNumbers", Serializer.listToBytes(state.currentNumbers))
                cv.put("totalNumbers", Serializer.listToBytes(state.totalNumbers))
                cv.put("history", Serializer.listToBytes(state.history))
                cv.put("answer", Serializer.listToBytes(state.answer))
                cv.put("data", Serializer.listToBytes(state.data))
            }

            is LevelState -> {
                tableName = "levels"
                Log.d(logTag, "--- Update in $tableName: ---")
                cv.put("kolMoves", state.kolMoves)
                cv.put("currentNode", state.currentNode)
                cv.put("numbers", Serializer.listToBytes(state.numbers))
                cv.put("totalNumbers", Serializer.listToBytes(state.totalNumbers))
                cv.put("data", Serializer.listToBytes(state.data))
            }
        }
        rowID = db.update(tableName, cv, "id = ?", arrayOf("$id"))
        Log.d(logTag, "$tableName updated, rowID = $rowID")
        close()
    }

    fun read(table: String, id: Int = DEFAULT_ID) : State? {  // id only for statistic
        val list: Array<String> = when(table) {
            "statistic" -> arrayOf("id", "kolGame", "sredScore", "maxScore")
            "settings"  -> arrayOf("id", "sound", "language", "theme", "computability", "moves", "time")
            "saveState" -> arrayOf("id", "gameStatus", "time", "allTime","score", "kolMoves", "computability",
                "currentNode", "mode", "currentNumbers", "totalNumbers", "history", "answer", "data")
            "levels"    -> arrayOf("id", "kolMoves", "currentNode", "numbers", "totalNumbers", "data")
            else        -> throw error("wrong table name")
        }

        val db = writableDatabase
        val state: State?
        Log.d(logTag, "--- Read in $table: ---")
        val cv = db.query(table, list, "id = ?", arrayOf("$id"), null, null, null)

        state = if (!cv.moveToNext()) null else when(table) {
            "statistic" -> {
                StatisticState(cv.getInt(cv.getColumnIndex("kolGame")),
                    cv.getDouble(cv.getColumnIndex("sredScore")),
                    cv.getInt(cv.getColumnIndex("maxScore")))
            }
            "settings"  -> {
                SettingsState(cv.getInt(cv.getColumnIndex("sound")) != 0,
                    cv.getString(cv.getColumnIndex("language")),
                    cv.getString(cv.getColumnIndex("theme")).toTheme(),
                    cv.getString(cv.getColumnIndex("computability")).toComputability(),
                    cv.getInt(cv.getColumnIndex("moves")),
                    cv.getInt(cv.getColumnIndex("time")))
            }
            "saveState" -> {
                SaveState(cv.getString(cv.getColumnIndex("gameStatus")).toGameState(),
                    cv.getLong(cv.getColumnIndex("time")),
                    cv.getLong(cv.getColumnIndex("allTime")),
                    cv.getInt(cv.getColumnIndex("score")),
                    cv.getInt(cv.getColumnIndex("kolMoves")),
                    cv.getString(cv.getColumnIndex("computability")).toComputability(),
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
                        object : TypeToken<List<List<Inscription>>>() {}.type) as List<List<Inscription>>)
            }
            "levels"    -> {
                LevelState(cv.getInt(cv.getColumnIndex("kolMoves")),
                    cv.getInt(cv.getColumnIndex("currentNode")),
                    Serializer.bytesToList(cv.getBlob(cv.getColumnIndex("numbers")),
                        object : TypeToken<List<Int>>() {}.type) as List<Int>,
                    Serializer.bytesToList(cv.getBlob(cv.getColumnIndex("totalNumbers")),
                        object : TypeToken<List<Int>>() {}.type) as List<Int>,
                    Serializer.bytesToList(cv.getBlob(cv.getColumnIndex("data")),
                        object : TypeToken<List<List<Inscription>>>() {}.type) as List<List<Inscription>>)
            }
            else        -> throw error("wrong table name")
        }

        cv.close()
        db.close()
        return state
    }
}

