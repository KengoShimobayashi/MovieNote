package com.movienote.android

data class MovieData(val _id: Int, val mainTitle: String, val subTitle: String, val furigana: String,
                     val director_ID: Int, val actor_ID: Int, val genre_ID: Int, val country_ID: Int,
                     val imageUri: String, val rating: Int, val favorite: Int)