package com.movienote.android.model.repository

import com.movienote.android.R
import com.movienote.android.model.entity.CountryDataEntity
import com.movienote.android.model.entity.DirectorDataEntity
import com.movienote.android.model.entity.GenreDataEntity
import com.movienote.android.model.entity.MovieDataEntity
import com.movienote.android.ui.main.ViewItem
import com.movienote.android.ui.util.ViewItemListUtil
import kotlinx.coroutines.*

class RepositoryMovie  private constructor(db : MovieDataBase){

    // region --private propaties--

    // Dao
    private val dao = db.movieDataDao()

    // cach
    private var cach : MutableList<MovieDataEntity> = mutableListOf()

    // endregion

    // region --private methods--

    // delete
    private fun deleteFromCachByID(id : String){
        this.cach.removeIf { it._id == id }
    }
    private suspend fun deleteFromDataBaseByID(id : String){
        this.dao.deleteMovieDataById(id)
    }

    // endregion

    // region --public methods--

    // init
    init {
        runBlocking {
            withContext(Dispatchers.Default) {
                loadAllDatas()
            }
        }
    }

    // getCount
    fun getCount() = this.cach.count()

    // getViewItemList
    fun getViewItemList() = ViewItemListUtil.createViewItemListByMovie(this.cach)

    // getFilteredViewItemList
    fun getFilteredViewItemList(mainTitle : String?, directorID : String?, actorID : String?,
                                genreID : String?, countryID : String?, favorite : Boolean, rate : Float) : MutableList<ViewItem>{

        val list : MutableList<MovieDataEntity> = mutableListOf()

        for(entity in this.cach){
            var result = true

            if(result)
                mainTitle?.let{result = entity._mainTitle.indexOf(mainTitle) > -1}

            if(result)
                directorID?.let { result = entity._directorId == directorID }

            if(result)
                actorID?.let { result = entity._actorId == actorID }

            if(result)
                genreID?.let { result = entity._genreId == genreID }

            if(result)
                countryID?.let { result = entity._countryId == countryID }

            if(result)
                countryID?.let { result = entity._countryId == countryID }

            if(result && favorite)
                result = entity._isFavorite

            if(result)
                result = entity._rate >= rate

            if(result)
                list.add(entity)
        }

        return ViewItemListUtil.createViewItemListByMovie(list)
    }

    // getDataByValue
    fun getDataByValue(value : String) : MovieDataEntity?{
        val datas = this.cach.filter { it._mainTitle == value }
        return if(datas.isNotEmpty()) datas[0] else null
    }

    // getDataByID
    fun getDataByID(id : String) : MovieDataEntity?{
        val datas = this.cach.filter { it._id == id }
        return if(datas.isNotEmpty()) datas[0] else null
    }

    // getDatasByDirectorID
    fun getDatasByDirectorID(directorId : String) : List<MovieDataEntity>{
        val datas = this.cach.filter { it._directorId == directorId }
        return if(datas.isNotEmpty()) datas else mutableListOf()
    }

    // getDatasByActorID
    fun getDatasByActorID(actorId : String) : List<MovieDataEntity>{
        val datas = this.cach.filter { it._actorId == actorId }
        return if(datas.isNotEmpty()) datas else mutableListOf()
    }

    // getDatasByGenreID
    fun getDatasByGenreID(genreId : String) : List<MovieDataEntity>{
        val datas = this.cach.filter { it._genreId == genreId }
        return if(datas.isNotEmpty()) datas else mutableListOf()
    }

    // getDatasByCountryID
    fun getDatasByCountryID(countryId : String) : List<MovieDataEntity>{
        val datas = this.cach.filter { it._countryId == countryId }
        return if(datas.isNotEmpty()) datas else mutableListOf()
    }

    // loadAllDatas
    suspend fun loadAllDatas() {
        this.cach = this.dao.loadAllMovieData() ?: mutableListOf()
    }

    // addData
    fun addData(data : MovieDataEntity){
        this.cach.add(data)
        CoroutineScope(Dispatchers.Main).launch {
            withContext(Dispatchers.Default) {
                saveData(data)
            }
        }
    }

    // update
    fun update(data : MovieDataEntity){
        this.cach.removeIf {
            it._id == data._id
        }
        this.cach.add(data)
        CoroutineScope(Dispatchers.Main).launch {
            withContext(Dispatchers.Default) {
                dao.update(data)
            }
        }
    }

    // saveData
    suspend fun saveData(data : MovieDataEntity) {
        this.dao.saveMovieData(data)
    }

    // deleteDataByID
    suspend fun deleteDataByID(id : String) {
        this.deleteFromCachByID(id)
        this.deleteFromDataBaseByID(id)
    }

    // deleteDatasByID
    suspend fun deleteDatasByID(ids : List<String>){
        ids.forEach { this.deleteDataByID(it) }
    }

    // endregion

    // region --companion object--

    companion object{

        // region --private propaties--

        // instance
        private var instance : RepositoryMovie? = null

        // endregion

        // region --public methods--

        // getInstance
        fun getInstance() = instance ?: RepositoryMovie(MovieDataBase.getInstance()).also{ instance = it}

        // endregion
    }

    // endregion
}