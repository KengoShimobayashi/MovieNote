package com.movienote.android

import android.app.AlertDialog
import android.content.ContentValues
import android.content.Context
import android.content.DialogInterface
import android.support.v7.widget.RecyclerView
import android.text.SpannableStringBuilder
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView

class CategoryListAdapter(
    private val context: Context,
    private var categoryList: MutableList<CategoryData>,
    private val bnvNum: Int
) : RecyclerView.Adapter<CategoryListAdapter.CategoryViewHolder>() {

    private val inflater = LayoutInflater.from(context)

    class CategoryViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val category: TextView = view.findViewById(R.id.item_onlymain)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {

        val view = inflater.inflate(R.layout.list_one_item_row, parent, false)
        val holder = CategoryViewHolder(view)

        view.setOnLongClickListener {
            val position = holder.layoutPosition
            val id = categoryList[position].id
            AlertDialog.Builder(context).apply {
                val clickItem = context.resources.getStringArray(R.array.listlongClick)

                setItems(clickItem, DialogInterface.OnClickListener { _, which ->
                    when (which) {
                        0 -> {
                            AlertDialog.Builder(context).apply {
                                setTitle(R.string.modify)
                                val dialogView =
                                    inflater.inflate(R.layout.dialog_modify, parent, false)
                                val editText = dialogView.findViewById<EditText>(R.id.modifyText)
                                editText.text = SpannableStringBuilder(categoryList[position].item)
                                editText.hint = getHintType(bnvNum)

                                setView(dialogView)
                                setPositiveButton(
                                    R.string.postiveButtonText,
                                    DialogInterface.OnClickListener { _, _ ->
                                        val text = editText.text.toString()
                                        updateCategoryList(bnvNum, id, text)
                                    })
                                setNegativeButton(R.string.negativeButtonText, null)
                                show()
                            }
                        }
                        1 -> {
                            AlertDialog.Builder(context).apply {
                                setTitle(R.string.deleteTitle)
                                setMessage(R.string.deleteMessage)
                                setPositiveButton(
                                    R.string.postiveButtonText,
                                    DialogInterface.OnClickListener { _, _ ->

                                        // Delete from database
                                        deleteCategory(bnvNum, id)
                                        updateCategory(bnvNum)
                                        updateCategory(context, id, -1, getNameString(bnvNum))

                                        MainActivity.updateMovieList(context)

                                        // removeItem
                                        categoryList = getList(bnvNum)
                                        notifyDataSetChanged()
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

        return holder
    }

    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
        holder.category.text = categoryList[position].item
    }

    override fun getItemCount() = categoryList.size

    private fun updateCategoryList(bottomNavigationType: Int, oldID: Int, newText: String) {
        if (newText.isNotEmpty()) {
            var itemID = oldID

            val list = getList(bottomNavigationType)
            val newItem = list.filter { it.item == newText }
            val oldItem = list.filter { it.id == oldID }
            val isConflict = (newItem.count() != 0)

            val cv = ContentValues()
            if (isConflict) {
                deleteCategory(bottomNavigationType, oldID)
                cv.put(getNumString(bottomNavigationType), newItem[0].ItemNum + oldItem[0].ItemNum)
                itemID = newItem[0].id

                updateCategory(context, oldID, itemID, getNameString(bottomNavigationType))
            } else
                cv.put(getNameString(bottomNavigationType), newText)

            updateDatabase(bottomNavigationType, itemID, cv)
            updateCategory(bottomNavigationType)

            categoryList = getList(bnvNum)
            notifyDataSetChanged()
        }
    }

    private fun getNumString(bottomNavigationType: Int): String {
        return when (bottomNavigationType) {
            0 -> DirectorDataBase.DIRECTOR_NUM
            1 -> ActorDataBase.ACTOR_NUM
            2 -> GenreDataBase.GENRE_NUM
            else -> ""
        }
    }

    private fun getNameString(bottomNavigationType: Int): String {
        return when (bottomNavigationType) {
            0 -> DirectorDataBase.DIRECTOR_NAME
            1 -> ActorDataBase.ACTOR_NAME
            2 -> GenreDataBase.GENRE_NAME
            else -> ""
        }
    }

    private fun updateDatabase(bottomNavigationType: Int, id: Int, cv: ContentValues) {
        MainActivity.updateMovieList(context)

        return when (bottomNavigationType) {
            0 -> DirectorDataBase.update(context, id, cv)
            1 -> ActorDataBase.update(context, id, cv)
            2 -> GenreDataBase.update(context, id, cv)
            else -> {
            }
        }
    }

    private fun getHintType(bottomNavigationType: Int): String {
        val type: Int? = when (bottomNavigationType) {
            0 -> R.string.director
            1 -> R.string.actor
            2 -> R.string.genreName
            else -> null
        }
        return if (type != null) context.resources.getString(type) else ""
    }

    private fun updateCategory(bottomNavigationType: Int) {
        when (bottomNavigationType) {
            0 -> MainActivity.updateDirectorList(context)
            1 -> MainActivity.updateActorList(context)
            2 -> MainActivity.updateGenreList(context)
            else -> {
            }
        }
    }

    private fun deleteCategory(bottomNavigationType: Int, id: Int) {
        when (bottomNavigationType) {
            0 -> DirectorDataBase.delete(context, id)
            1 -> ActorDataBase.delete(context, id)
            2 -> GenreDataBase.delete(context, id)
            else -> {
            }
        }
    }

    private fun getList(category: Int): MutableList<CategoryData> {
        return when (category) {
            0 -> MainActivity.directorList
            1 -> MainActivity.actorList
            2 -> MainActivity.genreList
            else -> mutableListOf()
        }
    }
}