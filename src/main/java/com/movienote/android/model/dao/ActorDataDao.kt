package com.movienote.android.model.dao

import androidx.room.*
import com.movienote.android.model.entity.ActorDataEntity
import com.movienote.android.model.entity.MovieDataEntity

@Dao
interface ActorDataDao {

    @Query("SELECT * FROM actordatas ORDER BY actorName ASC")
    fun loadAllActorData() : MutableList<ActorDataEntity>?

    @Query("SELECT id FROM actordatas WHERE actorName == :actor")
    fun getIDByActor(actor : String) : Int

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun saveActorData(actroData : ActorDataEntity)

    @Update
    fun update(actorData: ActorDataEntity)

    @Query("DELETE FROM actordatas WHERE id = :id")
    fun deleteActorDataById(id: String?): Int
}