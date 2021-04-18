package com.movienote.android.ui.main

//import com.movienote.android.ui.edit.IsFavorite

/*

class MovieListFragment : Fragment(){

    private lateinit var recyclerView: RecyclerView
    private lateinit var movieDatas: MutableList<MovieData>
    private lateinit var adapter: MovieListAdapter

    private fun show(){
        val ctx = context ?: return
        adapter = MovieListAdapter(ctx, MainActivity.movieList){ movieData ->
            val intent = Intent(ctx, EditActivity::class.java)
            intent.putExtra("id", movieData._id)
            intent.putExtra("mainTitle", movieData.mainTitle)
            intent.putExtra("subTitle", movieData.subTitle)
            intent.putExtra("furigana", movieData.furigana)
            intent.putExtra("director", movieData.director_ID)
            intent.putExtra("actor", movieData.actor_ID)
            intent.putExtra("genre", movieData.genre_ID)
            intent.putExtra("country", movieData.country_ID)
            intent.putExtra("uri", movieData.imageUri)
            intent.putExtra("rate", movieData.rating)
            intent.putExtra("favorite", movieData.favorite)
            intent.putExtra("flag", RegisterMode.MODIFY.rawValue)
            startActivityForResult(intent, RegisterMode.MODIFY.rawValue)
        }
        recyclerView.adapter = adapter
    }

    private fun getDirectorID(directorName: String) : Int{
        var id = -1

        if (directorName.isNotEmpty()) {
            var directorlist = DirectorDataBase.queryList(context, null, "${DirectorDataBase.DIRECTOR_NAME}=?", arrayOf(directorName),
                null, null, null)

            if(directorlist.count() == 0) {
                Listupdate = true
                DirectorDataBase.insert(context, directorName)
                directorlist = DirectorDataBase.queryList(context, null, "${DirectorDataBase.DIRECTOR_NAME}=?", arrayOf(directorName),
                    null, null, null)
            }else{
                val cv = ContentValues()
                cv.put(DirectorDataBase.DIRECTOR_NUM, directorlist[0].ItemNum + 1)
                DirectorDataBase.update(context, directorlist[0].id, cv)
            }
            MainActivity.updateDirectorList(context)
            id = directorlist[0].id
        }
        return id
    }

    private fun getActorID(actorName: String) : Int{
        var id = -1

        if (actorName.isNotEmpty()) {
            var actorList = ActorDataBase.queryList(context, null, "${ActorDataBase.ACTOR_NAME}=?", arrayOf(actorName),
                null, null, null)

            if(actorList.count() == 0) {
                Listupdate = true
                ActorDataBase.insert(context, actorName)
                actorList = ActorDataBase.queryList(context, null, "${ActorDataBase.ACTOR_NAME}=?", arrayOf(actorName),
                    null, null, null)
            }else{
                val cv = ContentValues()
                cv.put(ActorDataBase.ACTOR_NUM, actorList[0].ItemNum + 1)
                ActorDataBase.update(context, actorList[0].id, cv)
            }
            MainActivity.updateActorList(context)
            id = actorList[0].id
        }
        return id
    }

    private fun getGenreID(genrerName: String) : Int{
        var id = -1

        if (genrerName.isNotEmpty()) {
            var genreList = GenreDataBase.queryList(context, null, "${GenreDataBase.GENRE_NAME}=?", arrayOf(genrerName),
                null, null, null)

            if(genreList.count() == 0) {
                GenreDataBase.insert(context, genrerName)
                genreList = GenreDataBase.queryList(context, null, "${GenreDataBase.GENRE_NAME}=?", arrayOf(genrerName),
                    null, null, null)
            }else{
                val cv = ContentValues()
                cv.put(GenreDataBase.GENRE_NUM, genreList[0].ItemNum + 1)
                GenreDataBase.update(context, genreList[0].id, cv)
            }
            MainActivity.updateGenreList(context)
            id = genreList[0].id
        }
        return id
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_list, container, false)
        //movieDatas = queryMovieData(context)
        MainActivity.updateMovieList(context)

        recyclerView = view.findViewById(R.id.list)
        recyclerView.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)

        val itemDecoration = DividerItemDecoration(context, DividerItemDecoration.VERTICAL)
        recyclerView.addItemDecoration(itemDecoration)

        show()

        return view
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(resultCode == Activity.RESULT_OK){

            if(requestCode == RegisterMode.SEARCH.rawValue) {

                val title = data?.getStringExtra("title") ?: ""
                val director = data?.getIntExtra("director", -1) ?: -1
                val actor = data?.getIntExtra("actor", -1) ?: -1
                val genre = data?.getIntExtra("genre", -1) ?: -1
                val rating = data?.getIntExtra("rating", 0) ?: 0
                val favorite = data?.getIntExtra("favorite", IsFavorite.UNSELECTED.rawValue) ?: IsFavorite.UNSELECTED.rawValue

               // movieDatas = queryForSearch(context, title, director, actor, genre, rating, favorite)
                MainActivity.movieList = queryForSearch(context, title, director, actor, genre, rating, favorite)

            }else {

                // Add to DirectorDatabase
                val directorName = data?.getStringExtra("director") ?: ""
                var directorID = getDirectorID(directorName)

                // Add to ActorDatabase
                val actorName = data?.getStringExtra("actor") ?: ""
                var actorID = getActorID(actorName)

                // Get Genre ID
                val genreName = data?.getStringExtra("genre") ?: ""
                var genreID = getGenreID(genreName)

                val id = data!!.getIntExtra("id", 0)
                val mainTitle = data.getStringExtra("mainTitle")
                val subTitle = data.getStringExtra("subTitle")
                val furigana = data.getStringExtra("furigana")
                val countryID = data.getIntExtra("country", 0)
                val uri = data.getStringExtra("uri")
                val rate = data.getIntExtra("rate", 0)
                val favorite = data.getIntExtra("favorite", IsFavorite.UNSELECTED.rawValue)

                val md = MovieData(id, mainTitle, subTitle, furigana, directorID,
                    actorID, genreID, countryID, uri, rate, favorite)

                when (requestCode) {
                    RegisterMode.ADD.rawValue -> {
                        insertMovieData(context, md)
                    }
                    RegisterMode.MODIFY.rawValue -> {
                        update(context, md)
                    }
                }
                //movieDatas = queryMovieData(context)
                MainActivity.updateAllList(context)
            }
            show()
        }
    }
}

 */