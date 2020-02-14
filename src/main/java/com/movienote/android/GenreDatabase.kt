package com.movienote.android

import android.content.ContentValues
import android.content.Context
import android.content.res.Resources
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper



object GenreDataBase {

    // DataBaseName
    const val DB_NAME = "GenreDatabase"

    // DataBaseVersion
    const val DB_VERSION = 1

    const val GENRE_DATABASE_TABLE_NAME = "GenreDatabaseTable"

    const val GENRE_ID = "GenreID"
    const val GENRE_NAME = "Genre"
    const val GENRE_NUM = "itemNum"

    class DataBase(context: Context?) : SQLiteOpenHelper(context, DB_NAME, null, DB_VERSION){

        override fun onCreate(db: SQLiteDatabase?) {
            // Making DataBase table
            db?.execSQL("""
            CREATE TABLE $GENRE_DATABASE_TABLE_NAME (
            $GENRE_ID INTEGER PRIMARY KEY AUTOINCREMENT,
            $GENRE_NAME TEXT UNIQUE NOT NULL,
            $GENRE_NUM INTEGER NOT NULL)""")
        }

        override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        }
    }

    fun initialGenreDatabase(context: Context?) {
        val db = DataBase(context).writableDatabase
        val res: Resources? = context?.resources

        val initialGenreList = res?.getStringArray(R.array.genre)

        if(initialGenreList != null) {
            db.use { db ->
                for (genre in initialGenreList) {
                    // Insert record
                    val record = ContentValues().apply {
                        put(GENRE_NAME, genre)
                        put(GENRE_NUM, 0)
                    }

                    // Insert to database
                    db.insert(GENRE_DATABASE_TABLE_NAME, null, record)
                }
            }
        }
    }

    fun insert(context: Context?, genreItem: String){
        // Opening Database
        val db = DataBase(context).writableDatabase

        db.use{ db ->
            // Insert record
            val record = ContentValues().apply {
                put(GENRE_NAME, genreItem)
                put(GENRE_NUM, 0)
            }

            // Insert to database
            db.insert(GENRE_DATABASE_TABLE_NAME, null, record)
        }
        db.close()
    }

    fun queryList(context: Context?, columns: Array<String>?, selection: String?, selectionArgs: Array<String>?,
              groupBy: String?, having: String?, orderBy: String?) : MutableList<CategoryData>{
        // Opening Database
        val db = DataBase(context).readableDatabase

        // Search all from database
        val cursor = db.query(GENRE_DATABASE_TABLE_NAME, columns, selection, selectionArgs, groupBy, having, orderBy)

        val genreList = mutableListOf<CategoryData>()
        cursor.use{
            while(cursor.moveToNext()){
                val id = cursor.getInt(cursor.getColumnIndex(GENRE_ID))
                val genreItem = cursor.getString(cursor.getColumnIndex(GENRE_NAME))
                val itemNum = cursor.getInt(cursor.getColumnIndex(GENRE_NUM))

                genreList.add(CategoryData(id, genreItem, itemNum))
            }
        }
        db.close()
        return genreList
    }

    fun update(context: Context?, id: Int, cv: ContentValues){
        val db = DataBase(context).writableDatabase
        db.update(GENRE_DATABASE_TABLE_NAME, cv, "$GENRE_ID=?", arrayOf(id.toString()))
        db.close()
    }

    fun delete(context: Context, id: Int){
        // Opening Database
        val db = DataBase(context).readableDatabase
        db.delete(GENRE_DATABASE_TABLE_NAME, "$GENRE_ID = ?", arrayOf(id.toString()))

        db.close()
    }
}

