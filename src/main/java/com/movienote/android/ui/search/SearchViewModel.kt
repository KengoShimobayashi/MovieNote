package com.movienote.android.ui.search

import android.content.Context
import androidx.databinding.*
import com.movienote.android.misc.Standards
import com.movienote.android.model.repository.*

class SearchViewModel(private val mContext : Context,
                      private val movieRepository: RepositoryMovie,
                      private val directorRepository : RepositoryDirector,
                      private val actorRepository : RepositoryActor,
                      private val genreRepository : RepositoryGenre,
                      private val countryRepository: RepositoryCountry
) : BaseObservable() {

    // region --private properties--

    // listener
    private lateinit var navigator : SearchNavigator

    // endregion

    // region --public propaties--

    // mainTitle
    val mainTitle : ObservableField<String> = ObservableField()

    // director
    val directorList: ObservableArrayList<String> = ObservableArrayList()
    val directorSpinnerPosition : ObservableInt = ObservableInt()

    // actor
    val actorList: ObservableArrayList<String> = ObservableArrayList()
    val actorSpinnerPosition : ObservableInt = ObservableInt()

    // genre
    val genreList: ObservableArrayList<String> = ObservableArrayList()
    val genreSpinnerPosition : ObservableInt = ObservableInt()

    // country
    val countryList: ObservableArrayList<String> = ObservableArrayList()
    val countrySpinnerPosition : ObservableInt = ObservableInt()

    // favorite
    val favorite: ObservableBoolean = ObservableBoolean(false)

    // rating
    val rating: ObservableFloat = ObservableFloat(0f)

    // endregion

    // region --private methods--

    // setupDirectorList
    private fun setupDirectorList(){
        this.directorList.clear()
        this.directorList.add(Standards.NoChoose)
        this.directorRepository.cach.forEach {
            this.directorList.add(it._director)
        }
    }

    // setupActorList
    private fun setupActorList(){
        this.actorList.clear()
        this.actorList.add(Standards.NoChoose)
        this.actorRepository.cach.forEach {
            this.actorList.add(it._actorName)
        }
    }

    // setupGenreList
    private fun setupGenreList(){
        this.genreList.clear()
        this.genreList.add(Standards.NoChoose)
        this.genreRepository.cach.forEach {
            this.genreList.add(it._genre)
        }
    }

    // setupCountryList
    private fun setupCountryList(){
        this.countryList.clear()
        this.countryList.add(Standards.NoChoose)
        this.countryRepository.cach.forEach {
            this.countryList.add(it._country)
        }
    }

    // endregion

    // region --public methods--

    // init
    init{
        this.setupActorList()
        this.setupDirectorList()
        this.setupGenreList()
        this.setupCountryList()
    }

    // setNavigator
    fun setNavigator(navigator: SearchNavigator){
        this.navigator = navigator
    }

    // search
    fun search(){
        val director = this.directorList[this.directorSpinnerPosition.get()]
        val directorID = this.directorRepository.getDataByValue(director)?._id

        val actor = this.actorList[this.actorSpinnerPosition.get()]
        val actorID = this.actorRepository.getDataByValue(actor)?._id

        val genre = this.genreList[this.genreSpinnerPosition.get()]
        val genreID = this.genreRepository.getDataByValue(genre)?._id

        val country = this.countryList[this.countrySpinnerPosition.get()]
        val countryID = this.countryRepository.getDataByValue(country)?._id

        this.navigator.onMovieDataSearch(this.mainTitle.get(), directorID, actorID, genreID, countryID, this.favorite.get(), this.rating.get())
    }

    // onClickFavorite
    fun onClickFavorite() {
        this.favorite.set(!this.favorite.get())
    }

    // endregion
}