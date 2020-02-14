package com.movienote.android

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.net.Uri
import java.net.URI

// Database Name
private const val DB_NAME = "MovieDatabase"

// Database Version
private const val DB_VERSION = 1

// Database Table Name
private const val MOVIE_DATABASE_TABLE_NAME = "MovieDatabaseTable"

// Database Column Name
private const val ID_NUM = "_id"
private const val MAINTITLE_NAME = "MainTitle"
private const val SUBTITLE_NAME = "SubTitle"
private const val FURIGANA_NAME = "Furigana"
private const val DIRECTOR_NAME = "Director"
private const val ACTOR_NAME = "Actor"
private const val GENRE_NAME = "Genre"
private const val COUNTRY_NAME = "Country"
private const val IMAGEURI_NAME = "Uri"
private const val RATING_NAME = "rate"
private const val FOVORITE = "favorite"

class MovieDataBase(context: Context?) : SQLiteOpenHelper(context, DB_NAME, null, DB_VERSION){

    // Make DataBase Table
    override fun onCreate(db: SQLiteDatabase?) {
        db?.execSQL("""
            CREATE TABLE $MOVIE_DATABASE_TABLE_NAME (
                $ID_NUM INTEGER PRIMARY KEY AUTOINCREMENT,
                $MAINTITLE_NAME TEXT NOT NULL,
                $SUBTITLE_NAME TEXT,
                $FURIGANA_NAME TEXT,
                $DIRECTOR_NAME INTEGER,
                $ACTOR_NAME INTEGER,
                $GENRE_NAME INTEGER,
                $COUNTRY_NAME INTEGER,
                $IMAGEURI_NAME TEXT,
                $RATING_NAME INTEGER NOT NULL,
                $FOVORITE INTEGER);
                """)
    }

    // Upgrade DataBase
    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
    }
}

// insertMovieData
fun insertMovieData(context: Context?, md: MovieData){

    // Opening Database
    val database = MovieDataBase(context).writableDatabase

    database.use{ db ->
        // Insert record
        val record = ContentValues().apply {
            put(MAINTITLE_NAME, md.mainTitle)
            put(SUBTITLE_NAME, md.subTitle)
            put(FURIGANA_NAME, md.furigana)
            put(DIRECTOR_NAME, md.director_ID)
            put(ACTOR_NAME, md.actor_ID)
            put(GENRE_NAME, md.genre_ID)
            put(COUNTRY_NAME, md.country_ID)
            put(IMAGEURI_NAME, md.imageUri)
            put(RATING_NAME, md.rating)
            put(FOVORITE, md.favorite)
        }

        // Insert to database
        db.insert(MOVIE_DATABASE_TABLE_NAME, null, record)
    }
}

// queryMovieData
fun queryMovieData(context: Context?) : MutableList<MovieData>{

    // Opening Database
    val database = MovieDataBase(context).readableDatabase

    // Search all from database
    val cursor = database.query(MOVIE_DATABASE_TABLE_NAME, null, null, null, null, null, "$FURIGANA_NAME='', $FURIGANA_NAME ASC", null)

    val movieDataList = mutableListOf<MovieData>()
    cursor.use{
        while(cursor.moveToNext()){
            val id: Int = cursor.getInt(cursor.getColumnIndex(ID_NUM))
            val mainTitle: String = cursor.getString(cursor.getColumnIndex(MAINTITLE_NAME))
            val subTitle: String = cursor.getString(cursor.getColumnIndex(SUBTITLE_NAME))
            val furigana: String = cursor.getString(cursor.getColumnIndex(FURIGANA_NAME))
            val director: Int = cursor.getInt(cursor.getColumnIndex(DIRECTOR_NAME))
            val actor: Int = cursor.getInt(cursor.getColumnIndex(ACTOR_NAME))
            val genre: Int = cursor.getInt(cursor.getColumnIndex(GENRE_NAME))
            val country: Int = cursor.getInt(cursor.getColumnIndex(COUNTRY_NAME))
            val uri: String = cursor.getString(cursor.getColumnIndex(IMAGEURI_NAME))
            val rate: Int = cursor.getInt(cursor.getColumnIndex(RATING_NAME))
            val favorite: Int = cursor.getInt(cursor.getColumnIndex(FOVORITE))

            val movieData = MovieData(id, mainTitle, subTitle, furigana, director, actor, genre, country, uri, rate, favorite)

            movieDataList.add(movieData)
        }
    }
    database.close()
    return movieDataList
}

fun queryOneMovieData(context: Context?, id: Int) : MovieData{

    // Opening Database
    val database = MovieDataBase(context).readableDatabase

    // Search all from database
    val cursor = database.query(MOVIE_DATABASE_TABLE_NAME, null, "$ID_NUM=?", arrayOf(id.toString()), null, null, null, null)

    var movieData = MovieData(-1, "","","", -1,-1,-1,-1,"", -1, IsFavorite.UNSELECTED.rawValue)
    cursor.use{
        while(cursor.moveToNext()){
            val id: Int = cursor.getInt(cursor.getColumnIndex(ID_NUM))
            val mainTitle: String = cursor.getString(cursor.getColumnIndex(MAINTITLE_NAME))
            val subTitle: String = cursor.getString(cursor.getColumnIndex(SUBTITLE_NAME))
            val furigana: String = cursor.getString(cursor.getColumnIndex(FURIGANA_NAME))
            val director: Int = cursor.getInt(cursor.getColumnIndex(DIRECTOR_NAME))
            val actor: Int = cursor.getInt(cursor.getColumnIndex(ACTOR_NAME))
            val genre: Int = cursor.getInt(cursor.getColumnIndex(GENRE_NAME))
            val country: Int = cursor.getInt(cursor.getColumnIndex(COUNTRY_NAME))
            val uri: String = cursor.getString(cursor.getColumnIndex(IMAGEURI_NAME))
            val rate: Int = cursor.getInt(cursor.getColumnIndex(RATING_NAME))
            val favorite: Int = cursor.getInt(cursor.getColumnIndex(FOVORITE))

            movieData = MovieData(id, mainTitle, subTitle, furigana, director, actor, genre, country, uri, rate, favorite)
        }
    }
    database.close()
    return movieData
}

// delete
fun delete(context: Context, id: Int){
    // Opening Database
    val database = MovieDataBase(context).readableDatabase
    database.delete(MOVIE_DATABASE_TABLE_NAME, "$ID_NUM = ?", arrayOf(id.toString()))

    database.close()
}

fun update(context: Context?, movieData: MovieData){
    val database = MovieDataBase(context).readableDatabase

    val cv = ContentValues()
    cv.put(MAINTITLE_NAME, movieData.mainTitle)
    cv.put(SUBTITLE_NAME, movieData.subTitle)
    cv.put(FURIGANA_NAME, movieData.furigana)
    cv.put(DIRECTOR_NAME, movieData.director_ID)
    cv.put(ACTOR_NAME, movieData.actor_ID)
    cv.put(GENRE_NAME, movieData.genre_ID)
    cv.put(COUNTRY_NAME, movieData.country_ID)
    cv.put(IMAGEURI_NAME, movieData.imageUri)
    cv.put(RATING_NAME, movieData.rating)
    cv.put(FOVORITE, movieData.favorite)

    database.update(MOVIE_DATABASE_TABLE_NAME, cv, "$ID_NUM = ?", arrayOf("${movieData._id}"))
    database.close()
}

fun updateCategory(context: Context, oldID: Int, newID: Int, type: String){
    val db = MovieDataBase(context).readableDatabase
    val cv = ContentValues()
    cv.put(type, newID)
    db.update(MOVIE_DATABASE_TABLE_NAME, cv, "$type=?", arrayOf(oldID.toString()))
    db.close()
}

fun queryForSearch(context: Context?, title: String, directorID: Int, actroID: Int, genreID: Int, rating: Int, favoriteID: Int) : MutableList<MovieData>{
    val db = MovieDataBase(context).readableDatabase

    val selection = getSelection(title, directorID, actroID, genreID, rating, favoriteID)
    val array = getArray(title, directorID, actroID, genreID, rating, favoriteID)

    val cursor = db.query(MOVIE_DATABASE_TABLE_NAME, null, selection, array, null, null, null, null)

    val movieDataList = mutableListOf<MovieData>()
    cursor.use{
        while(cursor.moveToNext()){
            val id: Int = cursor.getInt(cursor.getColumnIndex(ID_NUM))
            val mainTitle: String = cursor.getString(cursor.getColumnIndex(MAINTITLE_NAME))
            val subTitle: String = cursor.getString(cursor.getColumnIndex(SUBTITLE_NAME))
            val furigana: String = cursor.getString(cursor.getColumnIndex(FURIGANA_NAME))
            val director: Int = cursor.getInt(cursor.getColumnIndex(DIRECTOR_NAME))
            val actor: Int = cursor.getInt(cursor.getColumnIndex(ACTOR_NAME))
            val genre: Int = cursor.getInt(cursor.getColumnIndex(GENRE_NAME))
            val country: Int = cursor.getInt(cursor.getColumnIndex(COUNTRY_NAME))
            val uri: String = cursor.getString(cursor.getColumnIndex(IMAGEURI_NAME))
            val rate: Int = cursor.getInt(cursor.getColumnIndex(RATING_NAME))
            val favorite: Int = cursor.getInt(cursor.getColumnIndex(FOVORITE))

            val movieData = MovieData(id, mainTitle, subTitle, furigana, director, actor, genre, country, uri, rate, favorite)

            movieDataList.add(movieData)
        }
    }
    db.close()
    return movieDataList
}

private fun getSelection(title: String, directorID: Int, actroID: Int, genreID: Int, rating: Int, favorite: Int) : String?{
    var selection = ""

    if(title.isNotEmpty())
        selection += "$MAINTITLE_NAME=? and "

    if(directorID != -1)
        selection += "$DIRECTOR_NAME=? and "

    if(actroID != -1)
        selection += "$ACTOR_NAME=? and "

    if(genreID != -1)
        selection += "$GENRE_NAME=? and "

    if(rating != 0)
        selection += "$RATING_NAME=? and "

    if(favorite == IsFavorite.SELECTED.rawValue)
        selection += "$FOVORITE=?"

     selection = selection.removeSuffix("and ")

    return if(selection.isNotEmpty()) selection else null
}

private fun getArray(title: String, directorID: Int, actroID: Int, genreID: Int, rating: Int, favorite: Int) : Array<String>?{
    var array = arrayListOf<String>()

    if(title.isNotEmpty())
        array.add(title)

    if(directorID != -1)
        array.add(directorID.toString())

    if(actroID != -1)
        array.add(actroID.toString())

    if(genreID != -1)
        array.add(genreID.toString())

    if(rating != 0)
        array.add(rating.toString())

    if(favorite == IsFavorite.SELECTED.rawValue)
        array.add(favorite.toString())

    return if(array.count() == 0) null else array.toTypedArray()
}