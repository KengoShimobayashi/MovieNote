package com.movienote.android.ui.datadetail

import android.content.Context
import androidx.databinding.*
import com.movienote.android.R
import com.movienote.android.misc.Standards
import com.movienote.android.model.repository.*

class DataDetailViewModel(private val movieRepository: RepositoryMovie,
                          private val directorRepository : RepositoryDirector,
                          private val actorRepository : RepositoryActor,
                          private val genreRepository : RepositoryGenre,
                          private val countryRepository: RepositoryCountry) : BaseObservable(){

    // region --private propaties--

    // entityId
    private lateinit var entityId: String

    // menuId
    private var menuId = 0

    // endregion

    // region --public propaties--

    // mainTitle
    val mainTitle : ObservableField<String> = ObservableField()

    // subTitle
    val subTitle : ObservableField<String> = ObservableField()

    // director
    val director : ObservableField<String> = ObservableField(Standards.NoChoose)

    // actor
    val actor : ObservableField<String> = ObservableField(Standards.NoChoose)

    // genre
    val genre : ObservableField<String> = ObservableField(Standards.NoChoose)

    // country
    val country : ObservableField<String> = ObservableField(Standards.NoChoose)

    // rate
    val rate : ObservableFloat = ObservableFloat(0.0F)

    // favorite
    val favorite : ObservableBoolean = ObservableBoolean(false)

    // value
    val value : ObservableField<String> = ObservableField()

    // isMovieData
    val isMovieData : ObservableBoolean = ObservableBoolean(false)

    // endregion

    // region --private methods--

    // setupAsMovie
    private fun setupAsMovie(entityId : String){
        this.isMovieData.set(true)

        this.movieRepository.getDataByID(entityId)?.let{

            // set title
            this.mainTitle.set(it._mainTitle)
            this.subTitle.set(it._subTitle)

            // set director
            it._directorId?.let { id ->
                this.setupAsDirector(id)
            }

            // set actor
            it._actorId?.let { id ->
                this.setupAsActor(id)
            }

            // set genre
            it._genreId?.let { id ->
                this.setupAsGenre(id)
            }

            // set country
            it._countryId?.let { id ->
                this.setupAsCountry(id)
            }

            this.favorite.set(it._isFavorite)
            this.rate.set(it._rate)
        }
    }

    // setupAsDirector
    private fun setupAsDirector(entityId : String){
        this.directorRepository.getDataByID(entityId)?.let {
            this.director.set(it._director) }
    }

    // setupAsActor
    private fun setupAsActor(entityId : String){
        this.actorRepository.getDataByID(entityId)?.let {
            this.actor.set(it._actorName) }
    }

    // setupAsGenre
    private fun setupAsGenre(entityId : String){
        this.genreRepository.getDataByID(entityId)?.let {
            this.genre.set(it._genre) }
    }

    // setupAsCountry
    private fun setupAsCountry(entityId : String){
        this.countryRepository.getDataByID(entityId)?.let {
            this.country.set(it._country) }
    }

    // setupAsValue
    private fun setupAsValue(entityId : String, menuId : Int){
        val value = when(menuId) {
            R.id.menu_director -> {
                setupAsDirector(entityId)
                this.director.get()
            }
            R.id.menu_actor -> {
                setupAsActor(entityId)
                this.actor.get()
            }
            R.id.menu_genre -> {
                setupAsGenre(entityId)
                this.genre.get()
            }
            /* R.id.menu_country  -> {
                setupAsDirector(entityId)
                this.country.get()
            } */
            else -> {Standards.NoChoose}
        }

        this.value.set(value)
    }

    // endregion

    // region --public methods--

    // start
    fun start(entityId : String, menuId : Int){
        this.entityId = entityId
        this.menuId = menuId

        when(menuId){
            R.id.menu_moviedata -> this.setupAsMovie(entityId)
            else                -> this.setupAsValue(entityId, menuId)
        }
    }

    // getRateAsString
    @Bindable
    fun getRateAsString() = this.rate.get().toString()

    // getHasRate
    @Bindable
    fun getHasRate() = this.rate.get() > 0

    // getSubTitle
    @Bindable
    fun getIsSubTitleEnable() = !this.subTitle.get().isNullOrBlank()

    // countryAndGenre
    @Bindable
    fun getCountryAndGenre() = "${this.country.get() ?: Standards.NoChoose}\\${this.genre.get() ?: Standards.NoChoose}"

    // endregion
}