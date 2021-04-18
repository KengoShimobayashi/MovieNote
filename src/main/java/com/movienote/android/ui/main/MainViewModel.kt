package com.movienote.android.ui.main

import androidx.databinding.*
import androidx.lifecycle.*
import com.movienote.android.R
import com.movienote.android.misc.SingleLiveEvent
import com.movienote.android.model.repository.*
import com.movienote.android.ui.edit.EditActivity
import com.movienote.android.ui.search.SearchActivity
import kotlinx.coroutines.*

class MainViewModel(private val movieRepository: RepositoryMovie,
                    private val directorRepository : RepositoryDirector,
                    private val actorRepository : RepositoryActor,
                    private val genreRepository : RepositoryGenre,
                    private val countryRepository: RepositoryCountry) : ViewModel() {

    class Factory constructor(private val movieRepository: RepositoryMovie,
                              private val directorRepository : RepositoryDirector,
                              private val actorRepository : RepositoryActor,
                              private val genreRepository : RepositoryGenre,
                              private val countryRepository: RepositoryCountry): ViewModelProvider.Factory {

        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            @Suppress("UNCHECKED_CAST")
            return MainViewModel(this.movieRepository, this.directorRepository, this.actorRepository, this.genreRepository, this.countryRepository) as T
        }
    }

    // region --private propaties--

    // mCurrentListDataType
    private var mCurrentListDataType: Int = R.id.menu_moviedata

    // endregion

    // region --public propaties--

    // items
    val items: ObservableList<ViewItem> = ObservableArrayList()

    // count
    val count : ObservableField<String> = ObservableField()

    // noDatasLabel
    val noDatasLabel : ObservableField<Int> = ObservableField()

    // tasksAddViewVisible
    val tasksAddViewVisible: ObservableBoolean = ObservableBoolean()

    // mode
    val mode : ObservableField<Mode> = ObservableField(Mode.Usual)
    private val modeChangedCallBack = object : Observable.OnPropertyChangedCallback() {
        override fun onPropertyChanged(sender: Observable?, propertyId: Int) {
            _onModeChanged.call()
        }
    }

    // region --public properties--

    // onStartAddMovieData
    private val _onStartAddMovieData = SingleLiveEvent<Any>()
    val onStartAddMovieData : LiveData<Any>
        get() = this._onStartAddMovieData

    // onModeChanged
    private val _onModeChanged = SingleLiveEvent<Any>()
    val onModeChanged : LiveData<Any>
        get() = this._onModeChanged

    // openDetailDialog
    private val _openDetailDialog = SingleLiveEvent<String>()
    val openDetailDialog : LiveData<String>
        get() = this._openDetailDialog

    // onStartSearch
    private val _onStartSearch = SingleLiveEvent<Any>()
    val onStartSearch : LiveData<Any>
        get() = this._onStartSearch

    // onSearchResult
    private val _onSearchResult = SingleLiveEvent<Any>()
    val onSearchResult : LiveData<Any>
        get() = this._onSearchResult

    // endregion

    // endregion

    // region --private methods--

    // changeDataList
    private fun updateList(id: Int) {
        this.mCurrentListDataType = id
        var list: List<ViewItem> = mutableListOf()
        var labelID = 0
        var viewVisible = false

        when (id) {
            R.id.menu_moviedata -> {
                list = this.movieRepository.getViewItemList()
                this.count.set(this.movieRepository.getCount().toString())
                labelID = R.string.txt_noMovieDatas
                viewVisible = true
            }
            R.id.menu_director -> {
                list = this.directorRepository.getViewItemList()
                labelID = R.string.txt_noDirectorDatas
            }
            R.id.menu_actor -> {
                list = this.actorRepository.getViewItemList()
                labelID = R.string.txt_noActorDatas
            }
            R.id.menu_genre -> {
                list = this.genreRepository.getViewItemList()
                labelID = R.string.txt_noGenreDatas
            }
            R.id.menu_country -> {
                list = this.countryRepository.getViewItemList()
                labelID = R.string.txt_noCountryDatas
            }
            else -> {
            }
        }

        this.items.clear()
        this.items.addAll(list)
        this.noDatasLabel.set(labelID)
        this.tasksAddViewVisible.set(viewVisible)
        this.mode.set(Mode.Usual)
    }

    // updateListWithFilter
    private fun updateListWithFilter(mainTitle : String?, directorID : String?, actorID : String?,
                                     genreID : String?, countryID : String?, favorite : Boolean, rate : Float){

        this.mode.set(Mode.Usual)
        this.mCurrentListDataType = R.id.menu_moviedata
        val list: List<ViewItem> = this.movieRepository.getFilteredViewItemList(mainTitle, directorID, actorID, genreID, countryID, favorite, rate)

        this.items.clear()
        this.items.addAll(list)
        noDatasLabel.set(R.string.txt_noSearchMovieData)
        tasksAddViewVisible.set(false)
    }

    // endregion

    // region --public methods--

    // start
    fun start() {
        this.updateList(mCurrentListDataType)
        this.mode.addOnPropertyChangedCallback(modeChangedCallBack)
    }

    // addNewMovieData
    fun addNewMovieData() {
        this._onStartAddMovieData.call()
    }

    // drawerItemClicked
    fun drawerItemClicked(id: Int){
        this.updateList(id)
    }

    // handleActivityResultFromEdit
    fun handleActivityResultFromEdit(resultCode: Int) {
        if (resultCode == EditActivity.ADD_EDIT_RESULT_OK)
            this.updateList(this.mCurrentListDataType)
    }

    // handleActivityResultFromSearch
    fun handleActivityResultFromSearch(resultCode: Int, mainTitle : String?,
                                       directorID : String?, actorID : String?, genreID : String?,
                                       countryID : String?, favorite : Boolean, rate : Float) {
        if (resultCode == SearchActivity.SEARCH_RESULT_OK) {
            this._onSearchResult.call()
            this.updateListWithFilter(mainTitle, directorID, actorID, genreID, countryID, favorite, rate)
        }
    }

    // Delete
    fun deleteItems() {
        val delteItemsID = mutableListOf<String>()
        this.items.filter {it.deleteFlag()}.forEach {
            this.items.remove(it)
            if (it is ViewItem.DataItem)
                delteItemsID.add(it.id)
        }

        CoroutineScope(Dispatchers.Main).launch {
            withContext(Dispatchers.Default) {
                when (mCurrentListDataType) {
                    R.id.menu_moviedata -> {
                        movieRepository.deleteDatasByID(delteItemsID)
                        count.set(movieRepository.getCount().toString())
                    }
                    R.id.menu_director -> directorRepository.deleteDatasByID(delteItemsID)
                    R.id.menu_actor -> actorRepository.deleteDatasByID(delteItemsID)
                    R.id.menu_genre -> genreRepository.deleteDatasByID(delteItemsID)
                    else -> { }
                }
            }
        }
    }

    // openDetailDialog
    fun openDetailDialog(entityId : String){
        this._openDetailDialog.value = entityId
    }

    // onStartSearch
    fun onStartSearch(){
        this._onStartSearch.call()
    }

    // endregion

}
