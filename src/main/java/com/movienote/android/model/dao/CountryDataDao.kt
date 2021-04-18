package com.movienote.android.model.dao

import androidx.room.*
import com.movienote.android.model.entity.CountryDataEntity
import com.movienote.android.model.entity.MovieDataEntity

@Dao
interface CountryDataDao {

    @Query("SELECT * FROM countrydatas")
    fun loadAllCountryData() : MutableList<CountryDataEntity>?

    @Query("SELECT id FROM countrydatas WHERE country == :country")
    fun getIDByCountry(country : String) : Int

    @Update
    fun update(countryData: CountryDataEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun saveCountryData(countryData : CountryDataEntity)

    @Query("DELETE FROM countrydatas WHERE id = :id")
    fun deleteCountryDataById(id: String?): Int
}