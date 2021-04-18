package com.movienote.android.ui.main

enum class EditType(val rawValue: Int){
    UNKNOWN(-1),
    ADD(0),
    MODIFY(1),
    SEARCH(2),
}

enum class ListDataType(val rawValue: Int){
    UNKNOWN(-1),
    MOVIE(0),
    DIRECTOR(1),
    ACTOR(2),
    GENRE(3)
}