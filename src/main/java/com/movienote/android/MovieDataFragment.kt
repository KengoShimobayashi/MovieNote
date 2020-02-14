package com.movienote.android

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView

class MovieDataFragment : Fragment(){

    private var movieTitle: String? = null
    private var director: String? = null
    private var actor: String? = null
    private var genre: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        movieTitle = arguments?.getString("movieTitle")
        director = arguments?.getString("director")
        actor = arguments?.getString("actor")
        genre = arguments?.getString("genre")
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_moviedata, container, false)

        val movieTitleText = view.findViewById<TextView>(R.id.txt_movieDataTitle)

        val directorText = view.findViewById<TextView>(R.id.txt_movieDataDirector)

        val actorText = view.findViewById<TextView>(R.id.txt_movieDataActor)

        val genreText = view.findViewById<TextView>(R.id.txt_movieDataGenre)


        movieTitleText.text = movieTitle
        directorText.text = director
        actorText.text = actor
        genreText.text = genre

        return view
    }
}

fun newMoviewDataFragment(movieTitle: String, director: String, actor: String, genre: String) : Fragment{
    val fragment = MovieDataFragment()
    val bundle = Bundle()

    bundle.putString("movieTitle", movieTitle)
    bundle.putString("director", director)
    bundle.putString("actor", actor)
    bundle.putString("genre", genre)

    fragment.arguments = bundle

    return fragment
}