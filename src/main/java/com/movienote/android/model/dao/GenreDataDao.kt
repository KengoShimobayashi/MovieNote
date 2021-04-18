package com.movienote.android.model.dao

import androidx.room.*
import com.movienote.android.model.entity.GenreDataEntity
import com.movienote.android.model.entity.MovieDataEntity

@Dao
interface GenreDataDao{

    @Query("SELECT * FROM genredatas ORDER BY genre ASC")
    fun loadAllGenreData() : MutableList<GenreDataEntity>?

    @Query("SELECT id FROM genredatas WHERE genre == :genre")
    fun getIDByGenre(genre : String) : Int

    @Update
    fun update(genreData: GenreDataEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun saveGenreData(genreData : GenreDataEntity)

    @Query("DELETE FROM genredatas WHERE id = :id")
    fun deleteGenreDataById(id: String?): Int
}