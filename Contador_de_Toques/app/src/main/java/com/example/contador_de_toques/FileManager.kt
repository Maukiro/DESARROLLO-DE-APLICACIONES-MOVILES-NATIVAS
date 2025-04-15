package com.example.contador_de_toques

import com.google.gson.Gson
import android.content.Context


object FileManager {
    fun saveGame(context: Context, gameState: GameState, format: String) {
        val fileName = "partida.$format"
        val data = when (format) {
            "json" -> Gson().toJson(gameState)
            "txt" -> "score=${gameState.score}\nlevel=${gameState.level}\ntime=${gameState.timeElapsed}"
            "xml" -> """<game><score>${gameState.score}</score><level>${gameState.level}</level></game>"""
            else -> ""
        }
        context.openFileOutput(fileName, Context.MODE_PRIVATE).use {
            it.write(data.toByteArray())
        }
    }

    fun loadGame(context: Context, format: String): GameState {
        val fileName = "partida.$format"
        val content = context.openFileInput(fileName).bufferedReader().readText()

        return when (format) {
            "json" -> Gson().fromJson(content, GameState::class.java)
            "txt" -> {
                val map = content.lines().associate {
                    val parts = it.split("=")
                    parts[0] to parts[1]
                }
                GameState(map["score"]!!.toInt(), map["level"]!!.toInt(), map["time"]!!.toLong(), "guinda", format)
            }
            "xml" -> {
                val score = Regex("<score>(\\d+)</score>").find(content)?.groupValues?.get(1)?.toInt() ?: 0
                val level = Regex("<level>(\\d+)</level>").find(content)?.groupValues?.get(1)?.toInt() ?: 1
                GameState(score, level, 0L, "azul", format)
            }
            else -> GameState(0, 1, 0, "guinda", format)
        }
    }
}
