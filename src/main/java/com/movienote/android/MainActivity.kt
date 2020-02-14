package com.movienote.android

import android.content.Context
import android.net.Uri
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.design.widget.TabLayout
import android.support.v4.app.AppLaunchChecker
import android.util.Log
import android.view.KeyEvent
import android.view.Menu
import android.view.MenuItem
import android.widget.TabHost
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), AllMovieListPageFragment.OnFragmentInteractionListener{

    companion object{
        var movieList = mutableListOf<MovieData>()
        var directorList = mutableListOf<CategoryData>()
        var actorList = mutableListOf<CategoryData>()
        var genreList = mutableListOf<CategoryData>()

        fun updateMovieList(context: Context?){
            movieList = queryMovieData(context)
        }

        fun updateDirectorList(context: Context?){
            directorList = DirectorDataBase.queryList(context, null, null, null,
                null, null, null)
        }

        fun updateActorList(context: Context?){
            actorList = ActorDataBase.queryList(context, null, null, null,
                null, null, null)
        }

        fun updateGenreList(context: Context?){
            genreList = GenreDataBase.queryList(context, null, null, null,
                null, null, null)
        }

        fun updateAllList(context: Context?){
            updateMovieList(context)
            updateDirectorList(context)
            updateActorList(context)
        }
    }



    // region --Member variables--
    // allMovieListPageFragment
    private lateinit var allMovieListPageFragment: AllMovieListPageFragment

    // categoryPageFragment
    private lateinit var categoryPageFragment: CategoryPageFragment
    // endregion


    // region --Override methods--
    // onCreate
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Check if it is first time to launch
        if(!AppLaunchChecker.hasStartedFromLauncher(this)) {
            // Initialize the Genre Database
            GenreDataBase.initialGenreDatabase(this)

            // Save the first time launch
            AppLaunchChecker.onActivityCreate(this)
        }

        updateAllList(this)
        updateGenreList(this)

        allMovieListPageFragment = AllMovieListPageFragment.newInstance()
        categoryPageFragment = CategoryPageFragment.newInstance()

        setTab()

        MainTab.addOnTabSelectedListener(object: TabLayout.OnTabSelectedListener {
            override fun onTabReselected(tab: TabLayout.Tab?) {
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
            }

            override fun onTabSelected(tab: TabLayout.Tab?) {
                if(tab?.position == 1 && Listupdate) {
                    Listupdate = false
                    categoryPageFragment.updateList()
                }
            }
        })
    }

    // onFragmentInteraction
    override fun onFragmentInteraction(uri: Uri) {
    }
    // endregion

    // region --Member methods--
    // setTab
    private fun setTab(){
        val mainTabAdapter = MainTabAdapter(supportFragmentManager)
        val tabLayoutName = this.resources.getStringArray(R.array.tabLayoutName)

        for(i: Int in 0 until 2) {
            when (i) {
                0 -> mainTabAdapter.add(allMovieListPageFragment)
                1 -> mainTabAdapter.add(categoryPageFragment)
            }
        }
        view_pager.adapter = mainTabAdapter
        MainTab.setupWithViewPager(view_pager)

        for(i: Int in 0 until mainTabAdapter.count){
            val tab = MainTab.getTabAt(i)
            tab?.text = tabLayoutName[i]
        }
    }
    // endregion
}
