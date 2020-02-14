package com.movienote.android

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

object DirectorDataBase{

    // Database Name
    const val DB_NAME = "DirectorDatabase"

    // Database Version
    const val DB_VERSION = 1

    // Database Table Name
    const val DIRECTOR_DATABASE_TABLE_NAME = "DirectorDatabaseTable"

    // Database Column Name
    const val DIRECTOR_ID = "_id"
    const val DIRECTOR_NAME = "Director"
    const val DIRECTOR_NUM = "ItemNum"

    class DataBase(context: Context?) : SQLiteOpenHelper(context, DB_NAME, null, DB_VERSION){

        // Make DataBase Table
        override fun onCreate(db: SQLiteDatabase?) {
            db?.execSQL("""
            CREATE TABLE $DIRECTOR_DATABASE_TABLE_NAME (
                $DIRECTOR_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                $DIRECTOR_NAME TEXT UNIQUE NOT NULL,
                $DIRECTOR_NUM INTEGER NOT NULL);
                """)
        }

        // Upgrade DataBase
        override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        }
    }

    // insertDirector
    fun insert(context: Context?, director: String){

        // Opening Database
        val db = DataBase(context).writableDatabase

        db.use{ db ->
            // Insert record
            val record = ContentValues().apply {
                put(DIRECTOR_NAME, director)
                put(DIRECTOR_NUM, 1)
            }

            // Insert to database
            db.insert(DIRECTOR_DATABASE_TABLE_NAME, null, record)
        }
        db.close()
    }

    // queryDirectorList
    fun queryList(context: Context?, columns: Array<String>?, selection: String?, selectionArgs: Array<String>?,
                          groupBy: String?, having: String?, orderBy: String?) : MutableList<CategoryData> {

        // Opening Database
        val db = DataBase(context).readableDatabase

        // Search all from database
        val cursor = db.query(DIRECTOR_DATABASE_TABLE_NAME, columns, selection, selectionArgs, groupBy, having, orderBy)

        val directorList = mutableListOf<CategoryData>()
        cursor.use {
            while (cursor.moveToNext()) {
                val id = cursor.getInt(cursor.getColumnIndex(DIRECTOR_ID))
                val director = cursor.getString(cursor.getColumnIndex(DIRECTOR_NAME))
                val itemNum = cursor.getInt(cursor.getColumnIndex(DIRECTOR_NUM))
                directorList.add(CategoryData(id, director, itemNum))
            }
        }
        db.close()
        return directorList
    }

    fun update(context: Context?, id: Int, cv: ContentValues){
        val db = DataBase(context).writableDatabase
        db.update(DIRECTOR_DATABASE_TABLE_NAME, cv, "$DIRECTOR_ID=?", arrayOf(id.toString()))
        db.close()
    }

    fun delete(context: Context, id: Int){
        val db = DataBase(context).readableDatabase
        db.delete(DIRECTOR_DATABASE_TABLE_NAME, "$DIRECTOR_ID=?", arrayOf(id.toString()))
        db.close()
    }
}

