package com.movienote.android

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.DialogInterface
import android.graphics.Color
import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText

class CategoryPageFragment : Fragment(), CategoryNavigationViewFragment.OnNavigationClickListener , CategoryListFragment.OnCateCategoryListFragment{

    private lateinit var genreAddButton: FloatingActionButton
    private lateinit var categoryListFragment: CategoryListFragment
    private lateinit var categoryNavigationViewFragment: CategoryNavigationViewFragment
    var bnvNum = 0

    @SuppressLint("RestrictedApi")
    override fun onNavigationClick(category: Int) {
        bnvNum = category
        categoryListFragment.show(getList(category))
        hideFloatButton(category)
    }

    override fun getbnvNum(): Int {
        return bnvNum
    }

    @SuppressLint("RestrictedApi")
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        val view = inflater.inflate(R.layout.fragment_categorypage, container, false)

        val transaction = childFragmentManager.beginTransaction()



        genreAddButton = view.findViewById(R.id.addGenreButton)
        genreAddButton.visibility = View.INVISIBLE
        genreAddButton.setColorFilter(Color.WHITE)

        genreAddButton.setOnClickListener  {
            AlertDialog.Builder(context).apply {
                setMessage(R.string.addGenreMessage)
                val dialogView = inflater.inflate(R.layout.dialog_modify, container, false)
                val et = dialogView.findViewById<EditText>(R.id.modifyText)
                et.hint = context.resources.getString(R.string.genreName)
                setView(dialogView)
                setPositiveButton(R.string.postiveButtonText, DialogInterface.OnClickListener { _, _ ->
                    val genreEditText = dialogView.findViewById<EditText>(R.id.modifyText)
                    val genreText = genreEditText.text.toString()
                    if(genreText.isNotEmpty()) {
                        GenreDataBase.insert(context, genreText)
                        MainActivity.updateGenreList(context)
                    }
                    categoryListFragment.show(MainActivity.genreList)
                })
                setNegativeButton(R.string.negativeButtonText, null)
                show()
            }
        }

        // Add categoryList
        categoryListFragment = CategoryListFragment()
        transaction.add(R.id.frameLayout_list, categoryListFragment, "categoryListFragment")

        // Add categoryNavigationView
        categoryNavigationViewFragment = CategoryNavigationViewFragment()
        transaction.add(R.id.frameLayout_navigationView, categoryNavigationViewFragment, "categoryNavigationViewFragment").commit()


        return view
    }

    private fun getList(category: Int) : MutableList<CategoryData>{
        return when(category) {
            0 -> MainActivity.directorList
            1 -> MainActivity.actorList
            2 -> MainActivity.genreList
            else -> mutableListOf()
        }
    }

    private fun hideFloatButton(category: Int){
        when(category){
            2 -> genreAddButton.show()
            else -> {
                genreAddButton.hide()
            }
        }
    }

    @SuppressLint("RestrictedApi")
    fun updateList(){
        categoryListFragment.show(getList(bnvNum))

        if(bnvNum == 2){
            genreAddButton.show()
        }
    }

    companion object{
        fun newInstance() : CategoryPageFragment {
            val fragment = CategoryPageFragment()
            return fragment
        }
    }
}