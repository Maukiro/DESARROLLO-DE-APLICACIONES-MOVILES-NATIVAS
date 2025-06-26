package com.example.agendaenkotlin.repository

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import com.example.agendaenkotlin.database.BD
import com.example.agendaenkotlin.database.DBHelper
import com.example.agendaenkotlin.model.Contacto

object ContactosRepository {


    private fun conexionDB(context: Context): SQLiteDatabase{
        val dbFile = context.getDatabasePath(BD.DB_NAME)
        return SQLiteDatabase.openOrCreateDatabase(dbFile,null)
    }

    // Insertar un nuevo contacto
    fun insertarContacto(nombre: String, numero: String, context: Context) {
        conexionDB(context).use { db->
            val values = ContentValues().apply {
                put(BD.NAME, nombre)
                put(BD.NUMBER, numero)
            }
            db.insert(BD.TB_CONTACTOS, null, values)
        }
    }


    // Obtener todos los contactos
    fun obtenerContactos(context: Context): ArrayList<Contacto> {
        val listaContactos = ArrayList<Contacto>()
        if (tablaExiste(context)) {
            context.openOrCreateDatabase(BD.DB_NAME, Context.MODE_PRIVATE, null).use { db ->
                db.rawQuery("SELECT * FROM ${BD.TB_CONTACTOS}", null).use { c ->
                    if (c.moveToFirst()) {
                        do {
                            val id = c.getInt(c.getColumnIndexOrThrow(BD.ID))
                            val nombre = c.getString(c.getColumnIndexOrThrow(BD.NAME))
                            val numero = c.getString(c.getColumnIndexOrThrow(BD.NUMBER))
                            listaContactos.add(Contacto(id, nombre, numero))
                        } while (c.moveToNext())
                    }
                }
            }
        } else {
            DBHelper(context).writableDatabase.close()
        }
        return listaContactos
    }

     private fun tablaExiste(context: Context): Boolean {
        val existe: Boolean
        context.openOrCreateDatabase(BD.DB_NAME, Context.MODE_PRIVATE, null).use { db ->
            db.rawQuery(
                "SELECT name FROM sqlite_master WHERE type='table' AND name=?",
                arrayOf(BD.TB_CONTACTOS)
            ).use { c ->
                existe = c.count > 0
            }
        }
        return existe
    }


    fun eliminarContacto(context: Context, id: Int): Boolean{
        return conexionDB(context).use { db ->
            val fila = db.delete(BD.TB_CONTACTOS, "${BD.ID} = ?", arrayOf(id.toString()))
            fila > 0
        }
    }




}