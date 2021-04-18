package com.movienote.android.ui.edit

import android.content.Context
import android.text.Editable
import androidx.databinding.*
import com.movienote.android.R
import com.movienote.android.misc.Standards
import com.movienote.android.model.entity.*
import com.movienote.android.model.repository.*
import com.movienote.android.ui.common.EntityType
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class EditViewModel(private val mContext : Context,
                    private val movieRepository: RepositoryMovie,
                    private val directorRepository : RepositoryDirector,
                    private val actorRepository : RepositoryActor,
                    private val genreRepository : RepositoryGenre,
                    private val countryRepository: RepositoryCountry
) : BaseObservable() {

    // region --private propaties--

    // mNavigator
    private lateinit var mNaviagator: EditNavigator

    // entityId
    private var entityId : String? = null

    // menuId
    private var menuId : Int = 0

    // isNewMovieData
    private val isNewMovieData : Boolean
    get() { return this.entityId == null}

    // regex
    private val regex = Regex("^[$MATCH_HIRAGANA$MATCH_LOWERCASE$MATCH_UPPERCASE$MATCH_NUMBER$MATCH_SYMBOL]+$")

    // endregion

    // region --public propaties--

    // MainTilte
    val mMainTitle: ObservableField<String> = ObservableField()
    val mMainTitleError: ObservableBoolean = ObservableBoolean(false)

    // SubTitle
    val mSubTitle: ObservableField<String> = ObservableField()

    // Furigana
    val mFurigana: ObservableField<String> = ObservableField()
    val mFuriganaError: ObservableBoolean = ObservableBoolean(false)

    // Director
    val mDirector: ObservableField<String> = ObservableField()

    // Actor
    val mActor: ObservableField<String> = ObservableField()

    // Genre
    val mGenreList: ObservableArrayList<String> = ObservableArrayList()
    val mGenreSpinnerPosition : ObservableInt = ObservableInt()

    // Country
    val mCountryList: ObservableArrayList<String> = ObservableArrayList()
    val mCountrySpinnerPosition : ObservableInt = ObservableInt()

    // Image
    val mImageUri: ObservableField<String> = ObservableField()

    // Favorite
    val mFavorite: ObservableBoolean = ObservableBoolean()

    // Rating
    val mRating: ObservableFloat = ObservableFloat()

    // value
    val value : ObservableField<String> = ObservableField()
    val valueError : ObservableBoolean = ObservableBoolean(false)
    val valueErrorMessage : ObservableField<String> = ObservableField()
    val label : ObservableField<String> = ObservableField()

    // endregion

    // region --private methods--

    // addNewMovieData
    private fun addNewMovieData() {

        val directorId = this.getIDByDirector(this.mDirector.get())
        val actorId = this.getIDByActor(this.mActor.get())
        val genreId = this.getIDByGenre(this.mGenreList[this.mGenreSpinnerPosition.get()])
        val countryId = this.getIDByCountry(this.mCountryList[this.mCountrySpinnerPosition.get()])

        val movieData = MovieDataEntity(
            this.mMainTitle.get()!!,
            this.mSubTitle.get(),
            this.mFurigana.get(),
            directorId,
            actorId,
            genreId,
            countryId,
            this.mImageUri.get(),
            this.mRating.get(),
            this.mFavorite.get()
        )

        this.movieRepository.addData(movieData)
    }

    // updateMovieData
    private fun updateMovieData(){

        val directorId = this.getIDByDirector(this.mDirector.get())
        val actorId = this.getIDByActor(this.mActor.get())
        val genreId = this.getIDByGenre(this.mGenreList[this.mGenreSpinnerPosition.get()])
        val countryId = this.getIDByCountry(this.mCountryList[this.mCountrySpinnerPosition.get()])

        if(!this.entityId.isNullOrBlank()) {
            val movieData = MovieDataEntity(
                this.entityId!!,
                this.mMainTitle.get()!!,
                this.mSubTitle.get(),
                this.mFurigana.get(),
                directorId,
                actorId,
                genreId,
                countryId,
                this.mImageUri.get(),
                this.mRating.get(),
                this.mFavorite.get()
            )
            this.movieRepository.update(movieData)
        }
    }

    // onMovieDataSave
    private fun onMovieDataSave(){
        this.mNaviagator.onMovieDataSave()
    }

    // setupListForGenreSpinner
    private fun setupListForGenreSpinner(){
        this.mGenreList.clear()
        this.mGenreList.add(Standards.NoChoose)
        this.genreRepository.cach.forEach {
            this.mGenreList.add(it._genre)
        }
    }

    // setupListForCountrySpinner
    private fun setupListForCountrySpinner(){
        this.mCountryList.clear()
        this.mCountryList.add(Standards.NoChoose)
        this.countryRepository.cach.forEach {
            this.mCountryList.add(it._country)
        }
    }

    // getIDByDirector
    private fun getIDByDirector(value : String?) : String? {
        return when(value.isNullOrBlank()){
            true -> null
            false -> {
                this.directorRepository.getDataByValue(value)?._id ?:
                DirectorDataEntity(value).also {this.directorRepository.addData(it) }._id
            }
        }
    }

    // getIDByActor
    private fun getIDByActor(value : String?) : String? {
        return when(value.isNullOrBlank()){
            true -> null
            false ->{
                this.actorRepository.getDataByValue(value)?._id ?:
                ActorDataEntity(value).also {this.actorRepository.addData(it) }._id
            }
        }
    }

    // getIDByGenre
    private fun getIDByGenre(value : String) : String? {
        return when(Standards.isNoChoose(value)){
            true -> null
            false ->{
                this.genreRepository.getDataByValue(value)?._id ?:
                GenreDataEntity(value).also {this.genreRepository.addData(it) }._id
            }
        }
    }

    // getIDByCountry
    private fun getIDByCountry(value : String) : String? {
        return when(Standards.isNoChoose(value)){
            true -> null
            false ->{
                this.countryRepository.getDataByValue(value)?._id ?:
                CountryDataEntity(value).also {this.countryRepository.addData(it) }._id
            }
        }
    }

    // saveAsMovieData
    private fun saveAsMovieData() : Boolean {
        val result = this.mMainTitle.get().isNullOrBlank()

        if(result)
            this.mMainTitleError.set(result)
        else {
            if (this.isNewMovieData)
                this.addNewMovieData()
            else
                this.updateMovieData()
        }

        return !result
    }

    // saveAsDirector
    private fun saveAsDirector() : Boolean{
        val valueStr = this.value.get()
        var result = false

        if(valueStr.isNullOrBlank())
            this.valueError.set(true)
        else{
            val director = this.directorRepository.getDataByValue(valueStr)
            if(director == null){
                this.directorRepository.update(DirectorDataEntity(this.entityId!!, valueStr))
            }else{
                val datas = this.movieRepository.getDatasByDirectorID(this.entityId!!)
                datas.forEach {
                    val movieData = MovieDataEntity(it._id, it._mainTitle, it._subTitle, it._furigana, director._id, it._actorId, it._genreId, it._countryId, it._imageUri, it._rate, it._isFavorite)
                    this.movieRepository.update(movieData)
                    CoroutineScope(Dispatchers.Main).launch {
                        withContext(Dispatchers.Default) {
                            directorRepository.deleteDataByID(entityId!!)
                        }
                    }
                }
            }
            result = true
        }
        return result
    }

    // saveAsActor
    private fun saveAsActor() : Boolean{
        val valueStr = this.value.get()
        var result = false

        if(valueStr.isNullOrBlank())
            this.valueError.set(true)
        else{
            val actor = this.actorRepository.getDataByValue(valueStr)
            if(actor == null){
                this.actorRepository.update(ActorDataEntity(this.entityId!!, valueStr))
            }else{
                val datas = this.movieRepository.getDatasByActorID(this.entityId!!)
                datas.forEach {
                    val movieData = MovieDataEntity(it._id, it._mainTitle, it._subTitle, it._furigana, it._directorId, actor._id, it._genreId, it._countryId, it._imageUri, it._rate, it._isFavorite)
                    this.movieRepository.update(movieData)
                    CoroutineScope(Dispatchers.Main).launch {
                        withContext(Dispatchers.Default) {
                            actorRepository.deleteDataByID(entityId!!)
                        }
                    }
                }
            }

            result = true
        }
        return result
    }

    // saveAsGenre
    private fun saveAsGenre() : Boolean{
        val valueStr = this.value.get()
        var result = false

        if(valueStr.isNullOrBlank())
            this.valueError.set(true)
        else{
            val genre = this.genreRepository.getDataByValue(valueStr)
            if(genre == null){
                this.genreRepository.update(GenreDataEntity(this.entityId!!, valueStr))
            }else{
                val datas = this.movieRepository.getDatasByGenreID(this.entityId!!)
                datas.forEach {
                    val movieData = MovieDataEntity(it._id, it._mainTitle, it._subTitle, it._furigana, it._directorId, it._actorId, genre._id, it._countryId, it._imageUri, it._rate, it._isFavorite)
                    this.movieRepository.update(movieData)
                    CoroutineScope(Dispatchers.Main).launch {
                        withContext(Dispatchers.Default) {
                            genreRepository.deleteDataByID(entityId!!)
                        }
                    }
                }
            }

            result = true
        }
        return result
    }

    // saveAsCountry
    private fun saveAsCountry() : Boolean{
        val valueStr = this.value.get()
        var result = false

        if(valueStr.isNullOrBlank())
            this.valueError.set(true)
        else{
            val country = this.countryRepository.getDataByValue(valueStr)
            if(country == null){
                this.genreRepository.update(GenreDataEntity(this.entityId!!, valueStr))
            }else{
                val datas = this.movieRepository.getDatasByCountryID(this.entityId!!)
                datas.forEach {
                    val movieData = MovieDataEntity(it._id, it._mainTitle, it._subTitle, it._furigana, it._directorId, it._actorId, it._genreId, country._id, it._imageUri, it._rate, it._isFavorite)
                    this.movieRepository.update(movieData)
                    CoroutineScope(Dispatchers.Main).launch {
                        withContext(Dispatchers.Default) {
                            countryRepository.deleteDataByID(entityId!!)
                        }
                    }
                }
            }

            result = true
        }
        return result
    }

    // endregion

    // region --public methods--

    // init
    init{
        this.mFavorite.set(false)
        this.setupListForGenreSpinner()
        this.setupListForCountrySpinner()
    }

    // setNavigator
    fun setNavigator(navigator: EditNavigator) {
        this.mNaviagator = navigator
    }

    // start
    fun start(entityId : String?, menuId : Int) {
        this.entityId = entityId
        this.menuId = menuId

        this.entityId?.let{
            when(this.menuId) {
                R.id.menu_moviedata -> this.movieRepository.getDataByID(it)?.let { entity ->
                    this.mMainTitle.set(entity._mainTitle)
                    this.mSubTitle.set(entity._subTitle)
                    entity._furigana?.let { furigana -> this.mFurigana.set(furigana) }
                    entity._directorId?.let { directorId ->
                        this.mDirector.set(this.directorRepository.getDataByID(directorId)?._director)
                    }
                    entity._actorId?.let { actorId ->
                        this.mActor.set(this.actorRepository.getDataByID(actorId)?._actorName)
                    }
                    this.mRating.set(entity._rate)
                    this.mFavorite.set(entity._isFavorite)
                    entity._genreId?.let { genreId ->
                        this.mGenreSpinnerPosition.set(
                            this.mGenreList.indexOf(
                                this.genreRepository.getDataByID(
                                    genreId
                                )?._genre ?: 0
                            )
                        )
                    }
                    entity._countryId?.let { countryId ->
                        this.mCountrySpinnerPosition.set(
                            this.mCountryList.indexOf(
                                this.countryRepository.getDataByID(
                                    countryId
                                )?._country ?: 0
                            )
                        )
                    }
                }
                R.id.menu_director  -> {
                    this.directorRepository.getDataByID(it)?.let { entity ->
                        this.value.set(entity._director)
                    }
                    this.label.set(mContext.resources.getString(R.string.director))
                    this.valueErrorMessage.set(this.mContext.resources.getString(R.string.errorNoDirector))
                }
                R.id.menu_actor     -> {
                    this.actorRepository.getDataByID(it)?.let{entity ->
                    this.value.set(entity._actorName)
                }
                    this.label.set(mContext.resources.getString(R.string.actor))
                    this.valueErrorMessage.set(this.mContext.resources.getString(R.string.errorNoActor))
                }
                R.id.menu_genre     -> {
                    this.genreRepository.getDataByID(it)?.let { entity -> this.value.set(entity._genre) }
                    this.label.set(mContext.resources.getString(R.string.genre))
                    this.valueErrorMessage.set(this.mContext.resources.getString(R.string.errorNoGenre))
                }
                else                -> {}
            }
        }
    }

    // save
    fun save() {
        if(when(this.menuId){
            R.id.menu_moviedata -> this.saveAsMovieData()
            R.id.menu_director  -> this.saveAsDirector()
            R.id.menu_actor     -> this.saveAsActor()
            R.id.menu_genre     -> this.saveAsGenre()
            else                -> false
        })
            this.onMovieDataSave()
    }

    // showNewDialog
    fun showNewDialog(type : EntityType) {
        this.mNaviagator.showNewDialog(type)
    }

    // onClickFavorite
    fun onClickFavorite() {
        this.mFavorite.set(!this.mFavorite.get())
    }

    // onFuriganaTextChanged
    fun onFuriganaTextChanged(text : Editable?) {
        if (!text.isNullOrBlank() && !text.matches(regex)) {
            if (!this.mFuriganaError.get())
                this.mFuriganaError.set(true)
        } else {
            if (this.mFuriganaError.get())
                this.mFuriganaError.set(false)
        }
    }

    // onMainTitleTextChanged
    fun onMainTitleTextChanged() {
        if(this.mMainTitleError.get())
            this.mMainTitleError.set(false)
    }

    // onValueTextChanged
    fun onValueTextChanged() {
        if(this.valueError.get())
            this.valueError.set(false)
    }

    // addNewItem
    fun addNewItem(entityType: EntityType, result : String) {
        when (entityType) {
            EntityType.GENRE -> {
                if(!this.mGenreList.contains(result))
                    this.mGenreList.add(result)

                this.mGenreSpinnerPosition.set(mGenreList.indexOf(result))
            }
            EntityType.COUNTRY -> {
                if(!this.mCountryList.contains(result))
                    this.mCountryList.add(result)

                this.mCountrySpinnerPosition.set(mCountryList.indexOf(result))
            }
        }
    }

    // getIsMovieData
    @Bindable
    fun getIsMovieData() = this.menuId == R.id.menu_moviedata

    // endregion

    // region --companion object--

    companion object{

        // Regex for furigana
        private const val MATCH_HIRAGANA = "\\u3040-\\u3095"
        private const val MATCH_NUMBER = "\\u0030-\\u003A"
        private const val MATCH_UPPERCASE = "\\u0041-\\u005B"
        private const val MATCH_LOWERCASE = "\\u0061-\\u007B"
        private const val MATCH_SYMBOL = "\\u30FB-\\u30FD"
    }

    // endregion

}