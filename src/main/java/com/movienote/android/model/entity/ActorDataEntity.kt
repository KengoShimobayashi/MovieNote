package com.movienote.android.model.entity

import androidx.annotation.NonNull
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity(tableName = "actordatas")
class ActorDataEntity(
    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "id")
    val _id : String,

    @NonNull
    @ColumnInfo(name = "actorName")
    val _actorName : String
){
    constructor(actroName : String) : this(UUID.randomUUID().toString(), actroName)
}