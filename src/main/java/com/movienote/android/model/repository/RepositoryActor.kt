package com.movienote.android.model.repository

import android.app.Instrumentation
import com.movienote.android.model.entity.ActorDataEntity
import com.movienote.android.model.entity.DirectorDataEntity
import com.movienote.android.model.entity.MovieDataEntity
import com.movienote.android.ui.util.ViewItemListUtil
import kotlinx.coroutines.*

class RepositoryActor private constructor(db : MovieDataBase){

    // region --private propaties--

    // Dao
    private val dao = db.actorDataDao()

    // cach
    var cach : MutableList<ActorDataEntity> = mutableListOf()

    // endregion

    // region --private methods--

    // delete
    private fun deleteFromCachByID(id : String){
        this.cach.removeIf { it._id == id }
    }
    private suspend fun deleteFromDataBaseByID(id : String){
        this.dao.deleteActorDataById(id)
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
    fun getViewItemList() = ViewItemListUtil.createViewItemListByActor(this.cach)

    // getDataByValue
    fun getDataByValue(value : String) : ActorDataEntity?{
        val datas = this.cach.filter { it._actorName == value }
        return if(datas.isNotEmpty()) datas[0] else null
    }

    // getDataByID
    fun getDataByID(id : String) : ActorDataEntity?{
        val datas = this.cach.filter { it._id == id }
        return if(datas.isNotEmpty()) datas[0] else null
    }

    // loadAllDatas
    suspend fun loadAllDatas() {
        this.cach = this.dao.loadAllActorData() ?: mutableListOf()
    }

    // addData
    fun addData(data : ActorDataEntity){
        this.cach.add(data)
        CoroutineScope(Dispatchers.Main).launch {
            withContext(Dispatchers.Default) {
                saveData(data)
            }
        }
    }

    // update
    fun update(data : ActorDataEntity){
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
    suspend fun saveData(data: ActorDataEntity) {
        this.dao.saveActorData(data)
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

    companion object {

        // region --private propaties--

        // instance
        private var instance: RepositoryActor? = null

        // endregion

        // region --public methods--

        // getInstance
        fun getInstance() =
            instance ?: RepositoryActor(MovieDataBase.getInstance()).also { instance = it }

        // endregion
    }

// endregion
}