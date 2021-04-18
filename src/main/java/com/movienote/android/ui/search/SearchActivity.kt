package com.movienote.android.ui.search

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.annotation.NonNull
import androidx.appcompat.widget.Toolbar
import com.movienote.android.R
import com.movienote.android.model.repository.*
import com.movienote.android.ui.ViewModelHolder
import com.movienote.android.ui.edit.EditActivity
import com.movienote.android.ui.edit.EditFragment
import com.movienote.android.ui.edit.EditViewModel
import com.movienote.android.ui.util.ActivityUtil

class SearchActivity : AppCompatActivity(), SearchNavigator {

    // region --private propaties--

    // viewModel
    private lateinit var viewModel : SearchViewModel

    // endregion

    // region --private methods--

    // findOrCreateMainTabFragment
    @NonNull
    private fun findOrCreateSearchFragment() : SearchFragment {
        var fragment  = supportFragmentManager.findFragmentById(R.id.contentFrame)
        if (fragment !is SearchFragment) {
            fragment = SearchFragment.newInstance()

            val bundle = Bundle()
            bundle.putString(
                SearchFragment.ARGUMENT_SEARCH_TASK_ID,
                intent.getStringExtra(SearchFragment.ARGUMENT_SEARCH_TASK_ID)
            )
            fragment.arguments = bundle
            ActivityUtil.addFragmentToActivity(supportFragmentManager, fragment, R.id.contentFrame)
        }
        return fragment
    }

    // findOrCreateViewModel
    private fun findOrCreateViewModel() : SearchViewModel {
        val viewModel : SearchViewModel

        val fragment = supportFragmentManager.findFragmentByTag(SEARCH_VIEWMODEL_TAG)
        if((fragment is ViewModelHolder<*>) && (fragment.mViewModel is SearchViewModel)) {
            viewModel = fragment.mViewModel as SearchViewModel
        }else{
            viewModel = SearchViewModel(
                applicationContext,
                RepositoryMovie.getInstance(),
                RepositoryDirector.getInstance(),
                RepositoryActor.getInstance(),
                RepositoryGenre.getInstance(),
                RepositoryCountry.getInstance())

            ActivityUtil.addFragmentToActivity(
                supportFragmentManager, ViewModelHolder.createContainer(viewModel), SEARCH_VIEWMODEL_TAG)
        }
        return viewModel
    }

    // setupToolBar
    private fun setupToolBar() {
        val toolBar = findViewById<Toolbar>(R.id.toolBar)
        setSupportActionBar(toolBar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar?.setTitle(R.string.search_title)
    }

    // endregion

    // region --public methods--

    //onCreate
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        // Setup ToolBar
        this.setupToolBar()

        val searchFragment = this.findOrCreateSearchFragment()
        this.viewModel = this.findOrCreateViewModel()
        this.viewModel.setNavigator(this)

        searchFragment.setViewModel(this.viewModel)
    }

    // onSupportNavigateUp
    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    // onMovieDataSave
    override fun onMovieDataSearch(mainTitle : String?, directorID : String?, actorID : String?,
                                   genreID : String?, countryID : String?, favorite : Boolean, rate : Float) {

        val intent = Intent(this, SearchActivity::class.java)

        mainTitle?.let { intent.putExtra(MAINTITLE_TAG, it)}
        directorID?.let { intent.putExtra(DIRECTOR_TAG, it)}
        actorID?.let { intent.putExtra(ACTOR_TAG, it)}
        genreID?.let { intent.putExtra(GENRE_TAG, it)}
        countryID?.let { intent.putExtra(COUNTRY_TAG, it)}

        if(favorite)
            intent.putExtra(FAVORITE_TAG, favorite)

        intent.putExtra(RATE_TAG, rate)

        setResult(SEARCH_RESULT_OK, intent)
        finish()
    }

    // endregion

    // region --companion object

    companion object{

        // SEARCH_VIEWMODEL_TAG
        const val SEARCH_VIEWMODEL_TAG = "SEARCH_VIEWMODEL_TAG"

        // SEARCH_RESULT_OK
        const val SEARCH_RESULT_OK = RESULT_FIRST_USER + 1

        // REQUEST_CODE
        const val REQUEST_CODE = 2

        // DATA_TAG
        const val MAINTITLE_TAG = "MAINTITLE_TAG"
        const val DIRECTOR_TAG = "DIRECTOR_TAG"
        const val ACTOR_TAG = "ACTOR_TAG"
        const val GENRE_TAG = "GENRE_TAG"
        const val COUNTRY_TAG = "COUNTRY_TAG"
        const val FAVORITE_TAG = "FAVORITE_TAG"
        const val RATE_TAG = "RATE_TAG"

    }

    // endregion
}
