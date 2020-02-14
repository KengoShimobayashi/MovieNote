package com.movienote.android

import android.app.AlertDialog
import android.content.DialogInterface
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.text.SpannableStringBuilder
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import java.util.zip.Inflater

class CategoryListFragment : Fragment(){

    private lateinit var recyclerView: RecyclerView
    private lateinit var categoryList: MutableList<CategoryData>

    interface OnCateCategoryListFragment {
        fun getbnvNum(): Int
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_list, container, false)
        //categoryList = queryDirectorList(context)

        recyclerView = view.findViewById(R.id.list)

        recyclerView.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)

        val itemDecoration = DividerItemDecoration(context, DividerItemDecoration.VERTICAL)
        recyclerView.addItemDecoration(itemDecoration)

        show(MainActivity.directorList)

        return view
    }

    fun show(categoryList: MutableList<CategoryData>) {
        val ctx = context ?: return
        val listener = parentFragment as OnCateCategoryListFragment
        val adapter = CategoryListAdapter(ctx, categoryList, listener.getbnvNum())

        recyclerView.adapter = adapter
    }
}