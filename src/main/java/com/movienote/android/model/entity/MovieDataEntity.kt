package com.movienote.android.model.entity

import androidx.annotation.NonNull
import androidx.databinding.ObservableFloat
import androidx.room.*
import java.util.*


@Entity(tableName = "moviedatas",
    foreignKeys =
    [ForeignKey(entity = DirectorDataEntity::class, parentColumns = ["id"], childColumns = ["directorId"], onDelete = ForeignKey.SET_NULL),
        ForeignKey(entity = ActorDataEntity::class, parentColumns = ["id"], childColumns = ["actorId"], onDelete = ForeignKey.SET_NULL),
        ForeignKey(entity = GenreDataEntity::class, parentColumns = ["id"], childColumns = ["genreId"], onDelete = ForeignKey.SET_NULL),
        ForeignKey(entity = CountryDataEntity::class, parentColumns = ["id"], childColumns = ["countryId"], onDelete = ForeignKey.SET_NULL)])

class MovieDataEntity(
    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "id")
    val _id: String,

    @NonNull
    @ColumnInfo(name = "maintitle")
    val _mainTitle: String,

    @ColumnInfo(name = "subtitle")
    val _subTitle: String?,

    @ColumnInfo(name = "furigana")
    val _furigana: String?,

    @ColumnInfo(name = "directorId")
    val _directorId: String?,

    @ColumnInfo(name = "actorId")
    val _actorId: String?,

    @ColumnInfo(name = "genreId")
    val _genreId: String?,

    @ColumnInfo(name = "countryId")
    val _countryId: String?,

    @ColumnInfo(name = "imageUri")
    val _imageUri: String?,

    @NonNull
    @ColumnInfo(name = "rate")
    val _rate: Float,

    @NonNull
    @ColumnInfo(name = "isfavorite")
    val _isFavorite: Boolean){

    constructor(mainTitle : String, subTitle : String?, furigana : String?, directorID : String?, actorID : String?,
    genreID : String?, countryID : String?, imageURI : String?, rate : Float, isFavorite : Boolean) :
            this(UUID.randomUUID().toString(), mainTitle, subTitle, furigana, directorID, actorID, genreID, countryID, imageURI, rate, isFavorite)
}