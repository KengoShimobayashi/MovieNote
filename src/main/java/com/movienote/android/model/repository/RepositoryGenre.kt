package com.movienote.android.model.repository

import com.movienote.android.model.entity.ActorDataEntity
import com.movienote.android.model.entity.DirectorDataEntity
import com.movienote.android.model.entity.GenreDataEntity
import com.movienote.android.model.entity.MovieDataEntity
import com.movienote.android.ui.util.ViewItemListUtil
import kotlinx.coroutines.*

class RepositoryGenre private constructor(db : MovieDataBase){

    // region --private propaties--

    // Dao
    private val dao = db.genreDataDao()

    // cach
    var cach : MutableList<GenreDataEntity> = mutableListOf()

    // endregion

    // region --private methods--

    // delete
    private fun deleteFromCachByID(id : String){
        this.cach.removeIf { it._id == id }
    }
    private suspend fun deleteFromDataBaseByID(id : String){
        this.dao.deleteGenreDataById(id)
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
    fun getViewItemList() = ViewItemListUtil.createViewItemListByGenre(this.cach)

    // getDataByValue
    fun getDataByValue(value : String) : GenreDataEntity?{
        val datas = this.cach.filter { it._genre == value }
        return if(datas.isNotEmpty()) datas[0] else null
    }

    // getDataByID
    fun getDataByID(id : String) : GenreDataEntity?{
        val datas = this.cach.filter { it._id == id }
        return if(datas.isNotEmpty()) datas[0] else null
    }

    // loadAllDatas
    suspend fun loadAllDatas() {
        this.cach = this.dao.loadAllGenreData() ?: mutableListOf()
    }

    // addData
    fun addData(data : GenreDataEntity){
        this.cach.add(data)
        CoroutineScope(Dispatchers.Main).launch {
            withContext(Dispatchers.Default) {
                saveData(data)
            }
        }
    }

    // update
    fun update(data : GenreDataEntity){
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

    // deleteDataByID
    suspend fun deleteDataByID(id : String) {
        this.deleteFromCachByID(id)
        this.deleteFromDataBaseByID(id)
    }

    // saveData
    suspend fun saveData(data : GenreDataEntity) {
        this.dao.saveGenreData(data)
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
        private var instance : RepositoryGenre? = null

        // endregion

        // region --public methods--

        // getInstance
        fun getInstance() = instance ?: RepositoryGenre(MovieDataBase.getInstance()).also{ instance = it}

        // endregion
    }

    // endregion
}