package com.movienote.android

import android.app.AlertDialog
import android.content.ContentValues
import android.content.Context
import android.content.DialogInterface
import android.net.Uri
import android.provider.MediaStore
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import java.io.IOException

class MovieListAdapter(
    private val context: Context,
    private val movieDatas: MutableList<MovieData>,
    private val movieDataClick: (MovieData) -> Unit
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val inflater = LayoutInflater.from(context)
    private val itemList = mutableListOf<ItemData>()
    private val checkList = mutableListOf<CheckData>()
    private val longClickItems = context.resources.getStringArray(R.array.listlongClick)

    companion object {
        // ViewType
        private const val ONLY_MAINTITLE = 0
        private const val WITH_SUBTITLE = 1
        private const val INDEX = 2

        // Limit Character
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
    }

    class OnlyMainTitleViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val mainTitle = view.findViewById<TextView>(R.id.item_onlymain)
    }

    class MainAndSubTitleViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val mainTitle = view.findViewById<TextView>(R.id.item_main)
        val mainSub = view.findViewById<TextView>(R.id.item_sub)
    }

    class IndexViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val index = view.findViewById<TextView>(R.id.item_index)
    }

    class ErrorViewHolder(view: View) : RecyclerView.ViewHolder(view)

    // ItemData
    data class ItemData(
        val id: Int,
        val mainTitle: String,
        val subTitle: String?,
        val furigana: String?,
        val directorID: Int,
        val actorID: Int,
        val viewType: Int
    )

    // checkList
    data class CheckData(val initial: String, var count: Int)

    // constructor
    init {
        movieDatas.forEach {
            val initial = getInitial(it.furigana)
            var itemdata: ItemData

            // Make Index
            if (getIndex(checkList, initial) == -1) {
                itemdata = ItemData(0, initial, null, null, -1, -1, INDEX)
                itemList.add(itemdata)
                checkList.add(CheckData(initial, 1))
            } else
                checkList[checkList.count() - 1].count += 1

            val viewType = when (it.subTitle.isEmpty()) {
                true -> ONLY_MAINTITLE
                false -> WITH_SUBTITLE
            }
            itemdata = ItemData(
                it._id,
                it.mainTitle,
                it.subTitle,
                it.furigana,
                it.director_ID,
                it.actor_ID,
                viewType
            )
            itemList.add(itemdata)
        }
    }

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
            str[0] in 'A'..'z' -> str[0].toString().toUpperCase()
            str[0] in '0'..'9' -> context.resources.getString(R.string.IndexNumber)
            else -> context.resources.getString(R.string.IndexException)
        } else context.resources.getString(R.string.IndexEtc)
    }

    // removeItem
    private fun removeItem(position: Int) {

        var itemCount = 1
        var deletePos = position
        val initial = getInitial(itemList[position].furigana)

        // Remove from itemList
        itemList.removeAt(position)

        val index = getIndex(checkList, initial)
        if (index != -1) {
            if (checkList[index].count == 1) {
                itemList.removeAt(position - 1)
                checkList.removeAt(getIndex(checkList, initial))
                itemCount = 2
                deletePos -= 1
            } else
                checkList[index].count -= 1
        }
        notifyDataSetChanged()
        notifyItemRangeChanged(deletePos, itemCount)
    }

    // getIndex
    private fun getIndex(list: MutableList<CheckData>, initial: String): Int {
        var index = -1

        for (i: Int in 0 until list.count()) {
            if (list[i].initial == initial) {
                index = i
                break
            }
        }
        return index
    }

    // getItemViewType
    override fun getItemViewType(position: Int): Int {
        return itemList[position].viewType
    }

    // onCreateViewHolder
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        val holder: RecyclerView.ViewHolder
        val view: View

        when (viewType) {
            INDEX -> {
                view = inflater.inflate(R.layout.list_index_row, parent, false)
                holder = IndexViewHolder(view)
            }
            else -> {
                when (viewType) {
                    ONLY_MAINTITLE -> {
                        view = inflater.inflate(R.layout.list_one_item_row, parent, false)
                        holder = OnlyMainTitleViewHolder(view)
                    }
                    WITH_SUBTITLE -> {
                        view = inflater.inflate(R.layout.list_few_item_row, parent, false)
                        holder = MainAndSubTitleViewHolder(view)
                    }
                    else -> {
                        view = View(parent.context)
                        holder = ErrorViewHolder(view)
                    }
                }
                // Long Click Listener
                view.setOnLongClickListener {
                    val position = holder.layoutPosition
                    val item = itemList[position]

                    AlertDialog.Builder(context).apply {

                        setItems(longClickItems, DialogInterface.OnClickListener { _, which ->
                            when (which) {
                                0 -> {
                                    val movieData = MainActivity.movieList.filter { it._id == item.id }[0]
                                    movieDataClick(movieData)
                                }
                                1 -> {
                                    AlertDialog.Builder(context).apply {
                                        setTitle(R.string.deleteTitle)
                                        setMessage(R.string.deleteMessage)
                                        setPositiveButton(
                                            R.string.postiveButtonText,
                                            DialogInterface.OnClickListener { _, _ ->

                                                // Delete from database
                                                delete(context, item.id)

                                                // removeItem
                                                removeItem(position)

                                                if(item.directorID != -1)
                                                    deleteDirector(item)

                                                if(item.actorID != -1)
                                                    deleteActor(item)
                                            })

                                        setNegativeButton(R.string.negativeButtonText, null)
                                        show()
                                    }
                                }
                            }
                        })
                        show()
                    }


                    true
                }

                // Click Listener
                view.setOnClickListener {
                    val position = holder.layoutPosition
                    val movieData = queryOneMovieData(context, itemList[position].id)

                    AlertDialog.Builder(context).apply {
                        val dialogView =
                            inflater.inflate(R.layout.moviedata_dialog_layout, parent, false)

                        val showMainTitle = dialogView.findViewById<TextView>(R.id.showMainTitle)
                        val showImageView = dialogView.findViewById<ImageView>(R.id.showImageView)
                        val showRatingBar = dialogView.findViewById<RatingBar>(R.id.showRatingBar)
                        val showDirector = dialogView.findViewById<TextView>(R.id.showDirector)
                        val showActor = dialogView.findViewById<TextView>(R.id.showActor)
                        val showGenre = dialogView.findViewById<TextView>(R.id.showGenre)
                        val showFavorite = dialogView.findViewById<ImageView>(R.id.showFavorite)

                        // Set MainTitle
                        showMainTitle.text = movieData.mainTitle

                        // Set SubTitle
                        val wc = LinearLayout.LayoutParams.WRAP_CONTENT
                        val param = LinearLayout.LayoutParams(wc, wc)
                        if (movieData.subTitle.isNotEmpty()) {
                            val subTitle = TextView(context)
                            subTitle.setTextColor(
                                context.resources.getColor(
                                    R.color.titleTextColor,
                                    null
                                )
                            )

                            val padding = 11
                            val scale = context.resources.displayMetrics.density
                            val paddingPx = (padding * scale).toInt()
                            subTitle.setPaddingRelative(paddingPx, 0, paddingPx, 0)
                            subTitle.text = movieData.subTitle

                            param.setMargins(0, 0, 0, 30)
                            subTitle.layoutParams = param

                            val layout = dialogView.findViewById<LinearLayout>(R.id.linearLayout)
                            layout.addView(subTitle)
                        } else {
                            param.setMargins(0, 30, 0, 30)
                            showMainTitle.layoutParams = param
                        }

                        // Set Director
                        if (movieData.director_ID != -1)
                            showDirector.text = MainActivity.directorList.filter { it.id == movieData.director_ID }[0].item

                        // Set actor
                        if (movieData.actor_ID != -1)
                            showActor.text = MainActivity.actorList.filter { it.id == movieData.actor_ID }[0].item

                        // Set Genre and Country
                        val countryList = context.resources.getStringArray(R.array.countryType)
                        val genreText = when(movieData.genre_ID != -1){
                            true -> MainActivity.genreList.filter { it.id == movieData.genre_ID }[0].item
                            false -> "---"
                        }
                        showGenre.text = "$genreText/${countryList[movieData.country_ID]}"

                        // Make bitmap
                        var uri: Uri?
                        if (movieData.imageUri.isNotEmpty()) {
                            uri = Uri.parse(movieData.imageUri)

                            try {
                                val bitmap =
                                    MediaStore.Images.Media.getBitmap(context?.contentResolver, uri)
                                showImageView.setImageBitmap(bitmap)
                            } catch (e: IOException) {
                                e.printStackTrace()
                            }
                        }

                        // Set rating
                        showRatingBar.rating = movieData.rating.toFloat()

                        // Set favorite
                        if (movieData.favorite == IsFavorite.SELECTED.rawValue)
                            showFavorite.setImageResource(movieData.favorite)

                        setView(dialogView)
                        show()
                    }
                }
            }
        }
        return holder
    }

    // onBindVIewHolder
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (itemList[position].viewType) {
            ONLY_MAINTITLE -> {
                val viewHolder = holder as OnlyMainTitleViewHolder
                viewHolder.mainTitle.text = itemList[position].mainTitle
            }
            WITH_SUBTITLE -> {
                val viewHolder = holder as MainAndSubTitleViewHolder
                viewHolder.mainTitle.text = itemList[position].mainTitle
                viewHolder.mainSub.text = itemList[position].subTitle
            }
            INDEX -> {
                val viewHolder = holder as IndexViewHolder
                viewHolder.index.text = itemList[position].mainTitle
            }
        }
    }

    // getItemCount
    override fun getItemCount() = itemList.size

    private fun deleteDirector(item: ItemData) {
        val directorData = MainActivity.directorList.filter { it.id == item.directorID }
        if (directorData[0].ItemNum == 1) {
            DirectorDataBase.delete(context, item.directorID)
            Listupdate = true
        }else{
            val cv = ContentValues()
            cv.put(DirectorDataBase.DIRECTOR_NUM, directorData[0].ItemNum - 1)
        }
        MainActivity.updateDirectorList(context)
    }

    private fun deleteActor(item: ItemData) {
        val actorData = MainActivity.actorList.filter { it.id == item.actorID }
        if (actorData[0].ItemNum == 1) {
            ActorDataBase.delete(context, item.actorID)
            Listupdate = true
        }else{
            val cv = ContentValues()
            cv.put(ActorDataBase.ACTOR_NUM, actorData[0].ItemNum - 1)
        }
        MainActivity.updateActorList(context)
    }
}