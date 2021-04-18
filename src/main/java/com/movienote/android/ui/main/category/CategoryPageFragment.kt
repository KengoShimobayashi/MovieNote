package com.movienote.android.ui.main.category

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.movienote.android.R

class CategoryPageFragment : Fragment(){

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_categorypage, container, false)
    }

    companion object{

        fun Create() : CategoryPageFragment {
            val fragment = CategoryPageFragment()
            return fragment
        }
    }
}

/*
class CategoryPageFragment : Fragment(), CategoryNavigationViewFragment.OnNavigationClickListener, CategoryListFragment.OnCateCategoryListFragment{

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
}

 */