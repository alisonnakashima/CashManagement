package com.alisonnakashima.cashmangement.database

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.database.Cursor


import com.alisonnakashima.cashmangement.entity.Insertions


class DatabaseHandler (context: Context): SQLiteOpenHelper (context, DATABASE_NAME, null, DATABABASE_VERSION ){
    override fun onCreate(db: SQLiteDatabase?) {
        db?.execSQL("CREATE TABLE IF NOT EXISTS ${TABLE_NAME} (_id INTEGER PRIMARY KEY AUTOINCREMENT, type TEXT, date DATE, description TEXT, value TEXT)")

    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL("DROP TABLE IF EXISTS ${TABLE_NAME}")
        onCreate( db )
    }

    companion object {
        private const val DATABASE_NAME = "db-insertions.sqlite"
        private const val TABLE_NAME = "insertions"
        private const val DATABABASE_VERSION = 1
        public const val ID = 0
        public const val TYPE = 1
        public const val DATE = 2
        public const val DESCRIPTION = 3
        public const val VALUE = 4

    }

    fun insert(insertions: Insertions){
        val db = this.writableDatabase

        val registro = ContentValues()
        registro.put("type", insertions.type)
        registro.put("date", insertions.date)
        registro.put("description", insertions.description)
        registro.put("value", insertions.value)

        db.insert(TABLE_NAME, null, registro)

    }

    fun entryList(){

    }

    fun balance(){

    }

    fun cursorList() : Cursor {
        val db = this.writableDatabase

        val registro = db.query(
            TABLE_NAME,
            null,
            null,
            null,
            null,
            null,
            null
        )
        return registro
    }

}