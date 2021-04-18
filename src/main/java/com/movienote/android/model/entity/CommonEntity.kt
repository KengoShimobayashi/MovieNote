package com.movienote.android.model.entity

import com.movienote.android.misc.Standards

data class CommonEntity(
    val id : String,
    val main : String,
    val sub : String?,
    val furigana : String?
){

    // region --public methods--

    // constructor
    constructor(id : String, main : String) : this(id, main, null, null)

    // endregion
}