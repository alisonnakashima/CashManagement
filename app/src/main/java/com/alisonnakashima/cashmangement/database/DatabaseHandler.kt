package com.alisonnakashima.cashmangement.database

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.database.Cursor


import com.alisonnakashima.cashmangement.entity.Inserter


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
        private const val DATABABASE_VERSION = 2
        public const val ID = 0
        public const val TYPE = 1
        public const val DATE = 2
        public const val DESCRIPTION = 3
        public const val VALUE = 4

    }

    fun insert(inserter: Inserter){
        val db = this.writableDatabase

        val registro = ContentValues()
        registro.put("type", inserter.type)
        registro.put("date", inserter.date)
        registro.put("description", inserter.description)
        registro.put("value", inserter.value)

        db.insert(TABLE_NAME, null, registro)

    }

    fun entryList(){

    }

    fun balance(): Double {
        val db = this.writableDatabase
        var saldo : Double = 0.0;

        val registro = db.query(
            TABLE_NAME,
            null,
            null,
            null,
            null,
            null,
            null
        )

        while ( registro.moveToNext() ){
            if (registro.getString(TYPE) == "Débito" ) {
                saldo += registro.getString(VALUE).toDouble() * (-1)
            }
            else
                saldo += registro.getString(VALUE).toDouble()
        }

        System.out.println(saldo)
        return saldo

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