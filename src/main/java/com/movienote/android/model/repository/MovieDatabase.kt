package com.movienote.android.model.repository

import android.content.Context
import android.content.pm.ApplicationInfo
import android.graphics.Movie
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.movienote.android.R
import com.movienote.android.model.dao.*
import com.movienote.android.model.entity.*
import com.movienote.android.ui.Application

@Database(entities = [
    MovieDataEntity::class,
    DirectorDataEntity::class,
    ActorDataEntity::class,
    GenreDataEntity::class,
    CountryDataEntity::class], version = MovieDataBase.DATABASE_VERSION, exportSchema = false)
abstract class MovieDataBase : RoomDatabase(){

    abstract fun movieDataDao() : MovieDataDao

    abstract fun directorDataDao() : DirectorDataDao

    abstract fun actorDataDao() : ActorDataDao

    abstract fun genreDataDao() : GenreDataDao

    abstract fun countryDataDao() : CountryDataDao

    companion object {

        // region --private propaties--

        // DATABASE_NAME
        private const val DATABASE_NAME = "Movies-user.db"

        // FOLDER_PATH
        private const val FOLDER_PATH = "database"

        // instance
        private lateinit var instance : MovieDataBase

        // sLock
        private val sLock = Object()

        // endregion

        // region --public porpaties--

        // DATABASE_VERSION
        const val DATABASE_VERSION = 2

        // endregion

        // region --public methods--

        // init
        fun init(context : Context) : MovieDataBase {
                synchronized(sLock) {

                    val builder =
                        Room.databaseBuilder(context, MovieDataBase::class.java, DATABASE_NAME)

                    instance = if (context.getDatabasePath(DATABASE_NAME).exists())
                        builder.addMigrations(MIGRATION_1_2).build()
                    else {
                        val filePath =
                            """${FOLDER_PATH}/${context.resources.getString(R.string.dataBaseFileName)}"""
                        builder.createFromAsset(filePath).addMigrations(MIGRATION_1_2).build()
                    }
                }
            return instance
        }

        // getInstance
        fun getInstance() = this.instance

        // endregion
    }
}

val MIGRATION_1_2 = object : Migration(1,2){
    override fun migrate(database: SupportSQLiteDatabase) {
        database.execSQL("""CREATE TABLE moviedatas_new (
                                id TEXT NOT NULL,
                                maintitle TEXT NOT NULL,
                                subtitle TEXT,
                                furigana TEXT,
                                directorId TEXT,
                                actorId TEXT,
                                genreId TEXT,
                                countryId TEXT,
                                imageUri TEXT,
                                rate REAL NOT NULL,
                                isfavorite INTEGER NOT NULL,
                                FOREIGN KEY(countryId) REFERENCES countrydatas(id) ON UPDATE NO ACTION ON DELETE SET NULL,
                                FOREIGN KEY(actorId) REFERENCES actordatas(id) ON UPDATE NO ACTION ON DELETE SET NULL,
                                FOREIGN KEY(genreId) REFERENCES genredatas(id) ON UPDATE NO ACTION ON DELETE SET NULL,
                                FOREIGN KEY(directorId) REFERENCES directordatas(id) ON UPDATE NO ACTION ON DELETE SET NULL,
                                PRIMARY KEY(id))""")

        database.execSQL("""INSERT INTO moviedatas_new (id, maintitle, subtitle, furigana, directorId, actorId, genreId, countryId, imageUri, rate, isfavorite)
                SELECT id, maintitle, subtitle, furigana, directorId, actorId, genreId, countryId, imageUri, rate, isfavorite
                FROM moviedatas""")

        database.execSQL("DROP TABLE moviedatas")

        database.execSQL("ALTER TABLE moviedatas_new RENAME TO moviedatas")
    }
}