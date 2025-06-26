package com.example.agendaenkotlin.database

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DBHelper(context: Context?) : SQLiteOpenHelper(context, BD.DB_NAME, null, BD.DB_VERSION) {

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL("CREATE TABLE ${BD.TB_CONTACTOS} (" +
                    "${BD.ID} INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "${BD.NAME} TEXT, " +
                    "${BD.NUMBER} TEXT)")
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        if (oldVersion < 2) {
            migrateTo2(db)
        }
    }

    private fun migrateTo2(db: SQLiteDatabase) {
        db.execSQL("ALTER TABLE ${BD.TB_CONTACTOS} ADD COLUMN email TEXT")
    }
}