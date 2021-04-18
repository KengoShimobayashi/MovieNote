package com.movienote.android.model.dao

import androidx.room.*
import com.movienote.android.model.entity.MovieDataEntity

@Dao
interface MovieDataDao {

    @Query("SELECT * FROM moviedatas")
    fun loadAllMovieData() : MutableList<MovieDataEntity>?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun saveMovieData(movieData: MovieDataEntity)

    @Update
    fun update(movieData: MovieDataEntity)

    @Query("SELECT * FROM moviedatas WHERE directorId = :directorId")
    fun getDatasByDirectorID(directorId : String) : MutableList<MovieDataEntity>?

    @Query("DELETE FROM moviedatas WHERE id = :id")
    fun deleteMovieDataById(id: String?): Int
}