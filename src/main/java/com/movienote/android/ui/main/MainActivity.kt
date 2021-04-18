package com.movienote.android.ui.main

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.annotation.NonNull
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.databinding.DataBindingUtil
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.*
import com.google.android.material.navigation.NavigationView
import com.movienote.android.R
import com.movienote.android.databinding.ActivityMainBinding
import com.movienote.android.databinding.DrawerHeaderBinding
import com.movienote.android.model.repository.*
import com.movienote.android.ui.ViewModelHolder
import com.movienote.android.ui.datadetail.DataDetailFragment
import com.movienote.android.ui.edit.EditActivity
import com.movienote.android.ui.edit.EditFragment
import com.movienote.android.ui.search.SearchActivity
import com.movienote.android.ui.util.ActivityUtil
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(),
    DataDetailFragment.DataDetailDialogListener {

    /*
        // mDrawerLayout
    private lateinit var mDrawerLayout: DrawerLayout

        // findOrCreateMainTabFragment
    @NonNull
    private fun findOrCreateMainFragment(): MainFragment {
        var fragment = supportFragmentManager.findFragmentById(R.id.contentFrame)
        if (fragment !is MainFragment) {
            fragment = MainFragment.newInstance()
            ActivityUtil.addFragmentToActivity(supportFragmentManager, fragment, R.id.contentFrame)
        }
        return fragment
    }

    // findOrCreateViewModel
    private fun findOrCreateViewModel(): MainViewModel {
        val viewModel: MainViewModel
        val fragment = supportFragmentManager.findFragmentByTag(MAIN_VIEWMODEL_TAG)

        if ((fragment != null) && (fragment is ViewModelHolder<*>) && (fragment.mViewModel is MainViewModel)) {
            viewModel = fragment.mViewModel as MainViewModel
        } else {
            viewModel = MainViewModel(applicationContext,
                        RepositoryMovie.getInstance(),
                        RepositoryDirector.getInstance(),
                        RepositoryActor.getInstance(),
                        RepositoryGenre.getInstance(),
                        RepositoryCountry.getInstance())

            ActivityUtil.addFragmentToActivity(
                supportFragmentManager, ViewModelHolder.createContainer(
                    viewModel
                ), MAIN_VIEWMODEL_TAG
            )
        }
        return viewModel
    }

    // setupToolBar
    private fun setupToolBar() {
        setSupportActionBar(toolBar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_menu)
    }

       // setupNavigationDrawer
    private fun setupNavigationDrawer() {
        mDrawerLayout = findViewById(R.id.drawerLayout)
        mDrawerLayout.setStatusBarBackground(R.color.colorPrimaryDark)
        val navigationView = findViewById<NavigationView>(R.id.nav_view)
        if (navigationView != null) {
            //setupDrawerContent(navigationView)
        }
    }

    // setupDrawerContent
    private fun setupDrawerContent(navigationView: NavigationView) {
        navigationView.setNavigationItemSelectedListener { menuItem ->
            mMainViewModel.drawerItemClicked(menuItem.itemId)
            mDrawerLayout.closeDrawers()
            true
        }
    }
     */

    // region --private propaties--

    // mMainViewModel
    private val mMainViewModel: MainViewModel by viewModels()

    // navController
    private lateinit var navController : NavController

    // endregion

    // region --private methods--

    // setupNavigationUI
    private fun setupNavigationUI(){
        setSupportActionBar(toolBar)
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        this.navController = navHostFragment.navController
        val appBarConfiguration = AppBarConfiguration(setOf(R.id.menu_moviedata, R.id.menu_director, R.id.menu_actor, R.id.menu_genre, R.id.menu_country), this.drawerLayout)
        nav_view.setupWithNavController(this.navController)
        toolBar.setupWithNavController(this.navController, appBarConfiguration)

        this.navController.addOnDestinationChangedListener { _, destination, _ ->
            when(destination.id){
                R.id.menu_help                 -> {}
                R.id.menu_setting              -> {}
                R.id.menu_appinfo              -> {}
                R.id.filteredmovieDataFragment -> {}
                else                           -> this.mMainViewModel.drawerItemClicked(destination.id)
            }
        }
    }

    // openEditActivity
    private fun openEditActivity(entityId : String, menuId: Int){
        val intent = Intent(this, EditActivity::class.java)
        intent.putExtra(EditFragment.ARGUMENT_EDIT_TASK_ID, entityId)
        intent.putExtra(EditFragment.ARGUMENT_MENU_ID, menuId)

        startActivityForResult(intent, EditActivity.REQUEST_CODE)
    }

    // endregion

    // region --public methods--

    // onCreate
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Setup viewModel
        this.mMainViewModel.start()

        // serup NavigationUI
        this.setupNavigationUI()

        // setup liveData
        this.mMainViewModel.onStartAddMovieData.observe(this, Observer {
            val intent = Intent(this, EditActivity::class.java)
            startActivityForResult(intent, EditActivity.REQUEST_CODE)
        })
        this.mMainViewModel.onModeChanged.observe(this, Observer {
            this.invalidateOptionsMenu()
        })
        this.mMainViewModel.openDetailDialog.observe(this, Observer {entityId ->
            val dialog = this.navController.currentDestination?.let { DataDetailFragment.showDataDetailDialog(entityId, it.id) }
            dialog?.show(supportFragmentManager, "detailDialog")
        })
        this.mMainViewModel.onStartSearch.observe(this, Observer {
            val intent = Intent(this, SearchActivity::class.java)
            startActivityForResult(intent, SearchActivity.REQUEST_CODE)
        })
        this.mMainViewModel.onSearchResult.observe(this, Observer {
            if(this.navController.currentDestination?.id != R.id.filteredmovieDataFragment)
                this.navController.navigate(R.id.filteredmovieDataFragment)
        })
    }

    // onActivityResult
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        when(requestCode){
            EditActivity.REQUEST_CODE   -> this.mMainViewModel.handleActivityResultFromEdit(resultCode)
            SearchActivity.REQUEST_CODE -> {
                val mainTitle = data?.getStringExtra(SearchActivity.MAINTITLE_TAG)
                val directorID = data?.getStringExtra(SearchActivity.DIRECTOR_TAG)
                val actorID = data?.getStringExtra(SearchActivity.ACTOR_TAG)
                val genreID = data?.getStringExtra(SearchActivity.GENRE_TAG)
                val countryID = data?.getStringExtra(SearchActivity.COUNTRY_TAG)
                val favorite = data?.getBooleanExtra(SearchActivity.FAVORITE_TAG, false) ?: false
                val rate = data?.getFloatExtra(SearchActivity.RATE_TAG, 0f) ?: 0f
                this.mMainViewModel.handleActivityResultFromSearch(resultCode, mainTitle, directorID, actorID, genreID, countryID, favorite, rate)
            }
        }
    }

    // onOptionsItemSelected
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return item.onNavDestinationSelected(this.navController) || super.onOptionsItemSelected(item)
    }

    // getDefaultViewModelProviderFactory
    override fun getDefaultViewModelProviderFactory(): ViewModelProvider.Factory {
        return MainViewModel.Factory(
            movieRepository = RepositoryMovie.getInstance(),
            directorRepository = RepositoryDirector.getInstance(),
            actorRepository = RepositoryActor.getInstance(),
            genreRepository = RepositoryGenre.getInstance(),
            countryRepository = RepositoryCountry.getInstance())
    }

    // onBackPressed
    override fun onBackPressed() {
        if(this.drawerLayout.isDrawerOpen(GravityCompat.START))
            this.drawerLayout.closeDrawers()
        else
            super.onBackPressed()
    }

    // onEditButtonClick
    override fun onEditButtonClick(entityId : String, menuId: Int) {
        this.openEditActivity(entityId, menuId)
    }

    // endregion
}

enum class Mode{
    Invalid,
    Usual,
    Select
}