package com.movienote.android.model.entity

import androidx.annotation.NonNull
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity(tableName = "directordatas")
class DirectorDataEntity (
    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "id")
    val _id : String,

    @NonNull
    @ColumnInfo(name = "directorName")
    val _director : String
) {
    constructor(directorName : String) : this(UUID.randomUUID().toString(), directorName)
}