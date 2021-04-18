package com.movienote.android.ui.util

import com.movienote.android.misc.Standards
import com.movienote.android.model.entity.*
import com.movienote.android.ui.main.ViewItem
import org.koin.standalone.StandAloneContext
import java.util.*

class ViewItemListUtil {

    companion object{

        // region --private propaties--

        // Character
        private const val aiueo = "あいうえお"
        private const val kg_aiueo = "かきくけこがぎぐげご"
        private const val sz_aiueo = "さしすせそざじずぜぞ"
        private const val td_aiueo = "たちつてとだぢづでど"
        private const val n_aiueo = "なにぬねの"
        private const val hbp_aiueo = "はひふへほばびぶべぼぱぴぷぺぽ"
        private const val m_aiueo = "まみむめも"
        private const val y_aiueo = "やゆよ"
        private const val l_aiueo = "らりるれろ"
        private const val w_aiueo = "わをん"

        // label
        private const val numIndex = "No."
        private const val etcIndex = "etc"

        // endregion

        // region --private methods--

        // getInitial
        private fun getInitial(str: String?): String {
            return if (!str.isNullOrEmpty()) when (true) {
                str[0] in aiueo -> aiueo[0].toString()
                str[0] in kg_aiueo -> kg_aiueo[0].toString()
                str[0] in sz_aiueo -> sz_aiueo[0].toString()
                str[0] in td_aiueo -> td_aiueo[0].toString()
                str[0] in n_aiueo -> n_aiueo[0].toString()
                str[0] in hbp_aiueo -> hbp_aiueo[0].toString()
                str[0] in m_aiueo -> m_aiueo[0].toString()
                str[0] in y_aiueo -> y_aiueo[0].toString()
                str[0] in l_aiueo -> l_aiueo[0].toString()
                str[0] in w_aiueo -> w_aiueo[0].toString()
                str[0] in 'A'..'z' -> str[0].toString().toUpperCase(Locale.ROOT)
                str[0] in '0'..'9' -> numIndex
                else -> etcIndex
            } else etcIndex
        }

        // sortByFurigana
        private fun sortByFurigana(list : List<CommonEntity>) =
            list.sortedWith(compareBy(nullsLast()) {it.furigana})

        // sortByMain
        private fun sortByMain(list : List<CommonEntity>) =
            list.sortedWith(compareBy(nullsLast()){it.main})

        // createViewItemList
        private fun createViewItemList(list : List<CommonEntity>) : MutableList<ViewItem>{
            val sortedList = sortByFurigana(list)
            val viewItemList : MutableList<ViewItem> = mutableListOf()
            var currentHeaderItem : ViewItem.HeaderItem? = null

            sortedList.forEach { it ->

                // Add HeaderItem
                val initial = getInitial(it.furigana)
                if(initial.isNotBlank()){
                    if(currentHeaderItem == null || currentHeaderItem!!.mainValue != initial) {
                        currentHeaderItem = ViewItem.HeaderItem("id-${initial}", initial)
                        viewItemList.add(currentHeaderItem!!)
                    }
                }

                // Add DataItem
                val dataItem = ViewItem.DataItem(it.id, it.main, it.sub)
                viewItemList.add(dataItem)
                currentHeaderItem?.dataList?.add(dataItem)
            }

            return viewItemList
        }

        // createViewItemList
        private fun createViewItemListWithoutIndex(list : List<CommonEntity>) : MutableList<ViewItem>{
            val sortedList = sortByMain(list)
            val viewItemList : MutableList<ViewItem> = mutableListOf()

            sortedList.forEach {
                viewItemList.add(ViewItem.DataItem(it.id, it.main, it.sub))
            }
            return viewItemList
        }

        // endregion

        // region --public methods--

        // createViewItemList
        fun createViewItemListByMovie(list: List<MovieDataEntity>) : MutableList<ViewItem>{
            val commonList = mutableListOf<CommonEntity>()
            list.forEach {
                commonList.add(CommonEntity(it._id, it._mainTitle, it._subTitle, it._furigana))
            }

            return createViewItemList(commonList)
        }
        fun createViewItemListByDirector(list: List<DirectorDataEntity>) : MutableList<ViewItem>{
            val commonList = mutableListOf<CommonEntity>()
            list.forEach {
                commonList.add(CommonEntity(it._id, it._director))
            }

            return createViewItemListWithoutIndex(commonList)
        }
        fun createViewItemListByActor(list: List<ActorDataEntity>) : MutableList<ViewItem>{
            val commonList = mutableListOf<CommonEntity>()
            list.forEach {
                commonList.add(CommonEntity(it._id, it._actorName))
            }

            return createViewItemListWithoutIndex(commonList)
        }
        fun createViewItemListByGenre(list: List<GenreDataEntity>) : MutableList<ViewItem>{
            val commonList = mutableListOf<CommonEntity>()
            list.forEach {
                commonList.add(CommonEntity(it._id, it._genre))
            }

            return createViewItemListWithoutIndex(commonList)
        }
        fun createViewItemListByzCountry(list: List<CountryDataEntity>) : MutableList<ViewItem>{
            val commonList = mutableListOf<CommonEntity>()
            list.forEach {
                commonList.add(CommonEntity(it._id, it._country))
            }

            return createViewItemListWithoutIndex(commonList)
        }

        // endregion
    }
}