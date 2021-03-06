package com.movienote.android.ui.search

import android.os.Bundle
import android.view.*
import android.widget.*
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.movienote.android.R
import com.movienote.android.databinding.FragmentEditBinding
import com.movienote.android.databinding.FragmentSearchBinding
import com.movienote.android.ui.edit.EditFragment
import com.movienote.android.ui.edit.EditViewModel
import com.movienote.android.ui.main.MainFragment

private var initTitle = ""
private var initDirectorIndex = 0
private var initActorIndex = 0
private var initGenreIndex = 0
private var initRating = 0

class SearchFragment : Fragment() {

    // region --private propaties--

    // viewModel
    private lateinit var viewModel: SearchViewModel

    // searchBinding
    private lateinit var searchBinding : FragmentSearchBinding

    // endregion

    // region --private methods--
    // endregion

    // region --public methods--

    // onCreateView
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {

        this.searchBinding = FragmentSearchBinding.inflate(inflater, container, false)
        this.searchBinding.view = this
        this.searchBinding.viewModle = this.viewModel

        setHasOptionsMenu(true)

        return this.searchBinding.root
    }

    // onCreateOptionsMenu
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.savemenu, menu)
    }

    // onOptionsItemSelected
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.menu_search -> this.viewModel.search()
        }
        return super.onOptionsItemSelected(item)
    }

    // setViewModel
    fun setViewModel(viewModel: SearchViewModel){
        this.viewModel = viewModel
    }

    // endregion

    // region --companion object--

    companion object {

        // ARGUMENT_SEARCH_TASK_ID
        const val ARGUMENT_SEARCH_TASK_ID = "SEARCH_TASK_ID"

        // newInstance
        fun newInstance() : SearchFragment {
            return SearchFragment()
        }
    }

    // endregion
}


/*
private lateinit var title: EditText
private lateinit var directorSP: Spinner
private lateinit var actorSP: Spinner
private lateinit var genreSP: Spinner
private lateinit var ratingBar: RatingBar
private lateinit var favorite: ImageView
private lateinit var searchButton: Button
private lateinit var clearButton: Button

private var directorQueryList = mutableListOf<CategoryData>()
private var actorQueryLsit = mutableListOf<CategoryData>()
private var genreQueryList = mutableListOf<CategoryData>()

private lateinit var listener: OnAttachListener
private lateinit var parentActivity: Activity

interface OnAttachListener{
    fun onAttachListener() : Activity
}

override fun onAttach(context: Context?) {
    super.onAttach(context)

    if(context is OnAttachListener){
        listener = activity as OnAttachListener
        parentActivity = listener.onAttachListener()
    }else{
        throw RuntimeException()
    }
}

override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
    super.onCreateView(inflater, container, savedInstanceState)

    val view = inflater.inflate(R.layout.fragment_search, container, false)

    title = view.findViewById(R.id.search_pltxt_mainTitle)

    directorSP = view.findViewById(R.id.search_sp_director)

    actorSP = view.findViewById(R.id.search_sp_actor)

    genreSP = view.findViewById(R.id.search_sp_genre)

    ratingBar = view.findViewById(R.id.search_ratingBar)

    favorite = view.findViewById(R.id.search_favorite)
    favorite.setOnClickListener {
        favoriteImage = when(favoriteImage){
            IsFavorite.UNSELECTED -> IsFavorite.SELECTED
            else -> IsFavorite.UNSELECTED
        }
        favorite.setImageResource(favoriteImage.rawValue)
    }

    searchButton = view.findViewById(R.id.btn_search)
    searchButton.setOnClickListener {
        val intent = Intent()
        intent.putExtra("title", title.text.toString())
        intent.putExtra("director", directorQueryList[directorSP.selectedItemPosition].id)
        intent.putExtra("actor", actorQueryLsit[actorSP.selectedItemPosition].id)
        intent.putExtra("genre", genreQueryList[genreSP.selectedItemPosition].id)
        intent.putExtra("rating", ratingBar.rating.toInt())
        intent.putExtra("favorite", favoriteImage.rawValue)

        initTitle = title.text.toString()
        initDirectorIndex = directorSP.selectedItemPosition
        initActorIndex = actorSP.selectedItemPosition
        initGenreIndex = genreSP.selectedItemPosition
        initRating = ratingBar.rating.toInt()
        initFavorite = favoriteImage.rawValue

        parentActivity.setResult(Activity.RESULT_OK, intent)
        parentActivity.finish()
    }

    clearButton = view.findViewById(R.id.btn_clear)
    clearButton.setOnClickListener {
        initTitle = ""
        initDirectorIndex = 0
        initActorIndex = 0
        initGenreIndex = 0
        initRating = 0
        favoriteImage = IsFavorite.UNSELECTED
        initFavorite = favoriteImage.rawValue

        val intent = Intent()
        intent.putExtra("title", initTitle)
        intent.putExtra("director", directorQueryList[initDirectorIndex].id)
        intent.putExtra("actor", actorQueryLsit[initActorIndex].id)
        intent.putExtra("genre", genreQueryList[initGenreIndex].id)
        intent.putExtra("rating", initRating)
        intent.putExtra("favorite", initFavorite)

        parentActivity.setResult(Activity.RESULT_OK, intent)
        parentActivity.finish()
    }

    if(context != null){
        directorQueryList = MainActivity.directorList
        directorQueryList.add(0, CategoryData(-1, "---", 0))

        actorQueryLsit = MainActivity.actorList
        actorQueryLsit.add(0, CategoryData(-1, "---", 0))

        genreQueryList = MainActivity.genreList
        genreQueryList.add(0, CategoryData(-1, "---", 0))

        val directorList = getStringList(directorQueryList)
        val actorList = getStringList(actorQueryLsit)
        val genreList = getStringList(genreQueryList)

        val directorAdapter =
            ArrayAdapter(context, R.layout.spinner_item, directorList)
                .also { adapter -> adapter.setDropDownViewResource(R.layout.spinner_drop_down_item) }
        directorSP.adapter = directorAdapter

        val dactorAdapter =
            ArrayAdapter(context, R.layout.spinner_item, actorList)
                .also { adapter -> adapter.setDropDownViewResource(R.layout.spinner_drop_down_item) }
        actorSP.adapter = dactorAdapter

        val genreAdapter =
            ArrayAdapter(context, R.layout.spinner_item, genreList)
                .also { adapter -> adapter.setDropDownViewResource(R.layout.spinner_drop_down_item) }
        genreSP.adapter = genreAdapter
    }

    title.text = SpannableStringBuilder(initTitle)
    directorSP.setSelection(initDirectorIndex)
    actorSP.setSelection(initActorIndex)
    genreSP.setSelection(initGenreIndex)
    ratingBar.rating = initRating.toFloat()
    favorite.setImageResource(initFavorite)

    return view
}

private fun getStringList(list: MutableList<CategoryData>) : MutableList<String>{
    val result = mutableListOf<String>()
    list.forEach {
        result.add(it.item)
    }

    return result
}
}

*/