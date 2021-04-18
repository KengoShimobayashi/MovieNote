package com.movienote.android.ui.search

interface SearchNavigator {

    // region --public methods--

    // onMovieDataSearch
    fun onMovieDataSearch(mainTitle : String?, directorID : String?, actorID : String?,
                          genreID : String?, countryID : String?, favorite : Boolean, rate : Float)

    // endregion

}