package com.movienote.android.model.repository

import com.movienote.android.model.entity.CountryDataEntity
import com.movienote.android.model.entity.DirectorDataEntity
import com.movienote.android.model.entity.GenreDataEntity
import com.movienote.android.model.entity.MovieDataEntity
import com.movienote.android.ui.util.ViewItemListUtil
import kotlinx.coroutines.*

class RepositoryCountry private constructor(db : MovieDataBase){

    // region --private propaties--

    // Dao
    private val dao = db.countryDataDao()

    // cach
    var cach : MutableList<CountryDataEntity> = mutableListOf()

    // endregion

    // region --private methods--

    // delete
    private fun deleteFromCachByID(id : String){
        this.cach.removeIf { it._id == id }
    }
    private suspend fun deleteFromDataBaseByID(id : String){
        this.dao.deleteCountryDataById(id)
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

    // getViewItemList
    fun getViewItemList() = ViewItemListUtil.createViewItemListByzCountry(this.cach)

    // getDataByValue
    fun getDataByValue(value : String) : CountryDataEntity?{
        val datas = this.cach.filter { it._country == value }
        return if(datas.isNotEmpty()) datas[0] else null
    }

    // getDataByID
    fun getDataByID(id : String) : CountryDataEntity?{
        val datas = this.cach.filter { it._id == id }
        return if(datas.isNotEmpty()) datas[0] else null
    }

    // loadAllDatas
    suspend fun loadAllDatas() {
        this.cach = this.dao.loadAllCountryData() ?: mutableListOf()
    }

    // addData
    fun addData(data : CountryDataEntity){
        this.cach.add(data)
        CoroutineScope(Dispatchers.Main).launch {
            withContext(Dispatchers.Default) {
                saveData(data)
            }
        }
    }

    // update
    fun update(data : CountryDataEntity){
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
    suspend fun saveData(data : CountryDataEntity) {
        this.dao.saveCountryData(data)
    }

    // deleteDataByID
    suspend fun deleteDataByID(id : String) {
        this.deleteFromCachByID(id)
        this.deleteFromDataBaseByID(id)
    }

    // deleteData
    suspend fun deleteDatas(ids : List<String>) {
        ids.forEach { this.dao.deleteCountryDataById(it) }
    }

    // deleteDataByIDs
    fun deleteDataByIDs(ids : List<String>){
        CoroutineScope(Dispatchers.Main).launch {
            withContext(Dispatchers.Default) {
                deleteDatas(ids)
            }
        }
        ids.forEach { this.getDataByID(it)?.let { data -> this.cach.remove(data) } }
    }

    // endregion

    // region --companion object--

    companion object{

        // region --private propaties--

        // instance
        private var instance : RepositoryCountry? = null

        // endregion

        // region --public methods--

        // getInstance
        fun getInstance() = instance ?: RepositoryCountry(MovieDataBase.getInstance()).also{ instance = it}

        // endregion
    }

    // endregion
}