package com.movienote.android.model.entity

import androidx.annotation.NonNull
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity(tableName = "genredatas")
class GenreDataEntity (
    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "id")
    val _id : String,

    @NonNull
    @ColumnInfo(name = "genre")
    val _genre : String
){
    constructor(genre : String) : this(UUID.randomUUID().toString(), genre)
}