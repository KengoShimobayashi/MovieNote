package com.movienote.android.model.repository

import android.view.View
import com.movienote.android.model.dao.DirectorDataDao
import com.movienote.android.model.entity.CommonEntity
import com.movienote.android.model.entity.DirectorDataEntity
import com.movienote.android.model.entity.MovieDataEntity
import com.movienote.android.ui.main.ViewItem
import com.movienote.android.ui.util.ViewItemListUtil
import kotlinx.coroutines.*

class RepositoryDirector private constructor(db : MovieDataBase){

    // region --private propaties--

    // Dao
    private val dao = db.directorDataDao()

    // cach
    var cach : MutableList<DirectorDataEntity> = mutableListOf()

    // endregion

    // region --private methods--

    // delete
    private fun deleteFromCachByID(id : String){
        this.cach.removeIf { it._id == id }
    }
    private suspend fun deleteFromDataBaseByID(id : String){
        this.dao.deleteDirectorDataById(id)
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
    fun getViewItemList() = ViewItemListUtil.createViewItemListByDirector(this.cach)

    // getDataByValue
    fun getDataByValue(value : String) : DirectorDataEntity?{
        val datas = this.cach.filter { it._director == value }
        return if(datas.isNotEmpty()) datas[0] else null
    }

    // getDataByID
    fun getDataByID(id : String) : DirectorDataEntity?{
        val datas = this.cach.filter { it._id == id }
        return if(datas.isNotEmpty()) datas[0] else null
    }

    // loadAllDatas
    suspend fun loadAllDatas() {
        this.cach = this.dao.loadAllDirectorData() ?: mutableListOf()
    }

    // addData
    fun addData(data : DirectorDataEntity){
        this.cach.add(data)
        CoroutineScope(Dispatchers.Main).launch {
            withContext(Dispatchers.Default) {
                saveData(data)
            }
        }
    }

    // update
    fun update(data : DirectorDataEntity){
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
    suspend fun saveData(data : DirectorDataEntity) {
        this.dao.saveDirectorData(data)
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
        private var instance : RepositoryDirector? = null

        // endregion

        // region --public methods--

        // getInstance
        fun getInstance() = instance ?: RepositoryDirector(MovieDataBase.getInstance()).also{ instance = it}

        // endregion
    }

    // endregion
}