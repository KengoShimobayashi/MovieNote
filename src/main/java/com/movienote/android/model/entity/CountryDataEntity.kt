package com.movienote.android.model.entity

import androidx.annotation.NonNull
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity(tableName = "countrydatas")
class CountryDataEntity (
    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "id")
    val _id : String,

    @NonNull
    @ColumnInfo(name = "country")
    val _country : String
){
    constructor(country : String) : this(UUID.randomUUID().toString(), country)
}