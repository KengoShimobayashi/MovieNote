package com.movienote.android.model.dao

import androidx.room.*
import com.movienote.android.model.entity.DirectorDataEntity
import com.movienote.android.model.entity.MovieDataEntity

@Dao
interface DirectorDataDao {

    @Query("SELECT * FROM directordatas ORDER BY directorName ASC")
    fun loadAllDirectorData() : MutableList<DirectorDataEntity>?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun saveDirectorData(directorData : DirectorDataEntity)

    @Update
    fun update(directorData: DirectorDataEntity)

    @Query("DELETE FROM directordatas WHERE id = :id")
    fun deleteDirectorDataById(id: String?): Int
}