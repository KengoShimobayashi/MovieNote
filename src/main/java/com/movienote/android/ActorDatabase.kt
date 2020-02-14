package com.movienote.android

import android.content.ClipData
import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper


object ActorDataBase{

    // Database Name
    const val DB_NAME = "ActorDatabase"

    // Database Version
    const val DB_VERSION = 1

    // Database Table Name
    const val ACTOR_DATABASE_TABLE_NAME = "ActorDatabaseTable"

    // Database Column Name
    const val ACTOR_ID = "_id"
    const val ACTOR_NAME = "Actor"
    const val ACTOR_NUM = "ItemNum"

    class DataBase(context: Context?) : SQLiteOpenHelper(context, DB_NAME, null, DB_VERSION){

        // Make DataBase Table
        override fun onCreate(db: SQLiteDatabase?) {
            db?.execSQL("""
            CREATE TABLE $ACTOR_DATABASE_TABLE_NAME (
                $ACTOR_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                $ACTOR_NAME TEXT UNIQUE NOT NULL,
                $ACTOR_NUM ITEGER NOT NULL);
                """)
        }

        // Upgrade DataBase
        override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        }
    }

    // insert
    fun insert(context: Context?, actor: String){

        // Opening Database
        val db = DataBase(context).writableDatabase

        db.use{ db ->
            // Insert record
            val record = ContentValues().apply {
                put(ACTOR_NAME, actor)
                put(ACTOR_NUM, 1)
            }

            // Insert to database
            db.insert(ACTOR_DATABASE_TABLE_NAME, null, record)
        }
        db.close()
    }

    // queryList
    fun queryList(context: Context?, columns: Array<String>?, selection: String?, selectionArgs: Array<String>?,
                  groupBy: String?, having: String?, orderBy: String?) : MutableList<CategoryData> {

        // Opening Database
        val database = DataBase(context).readableDatabase

        // Search all from database
        val cursor = database.query(ACTOR_DATABASE_TABLE_NAME, columns, selection, selectionArgs, groupBy, having, orderBy)

        val actorList = mutableListOf<CategoryData>()
        cursor.use {
            while (cursor.moveToNext()) {
                val id = cursor.getInt(cursor.getColumnIndex(ACTOR_ID))
                val actor = cursor.getString(cursor.getColumnIndex(ACTOR_NAME))
                val itemNum = cursor.getInt(cursor.getColumnIndex(ACTOR_NUM))
                actorList.add(CategoryData(id, actor, itemNum))
            }
        }
        database.close()
        return actorList
    }

    fun update(context: Context?, id: Int, cv: ContentValues){
        val db = DataBase(context).writableDatabase
        db.update(ACTOR_DATABASE_TABLE_NAME, cv, "$ACTOR_ID=?", arrayOf(id.toString()))
        db.close()
    }

    fun delete(context: Context, id: Int){
        val db = DataBase(context).readableDatabase

        db.delete(ACTOR_DATABASE_TABLE_NAME, "$ACTOR_ID=?", arrayOf(id.toString()))
        db.close()
    }
}