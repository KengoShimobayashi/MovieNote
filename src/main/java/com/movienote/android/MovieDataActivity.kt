package com.movienote.android

import android.support.v7.app.AppCompatActivity
import android.os.Bundle

class MovieDataActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_movie_data)
        val movieTitle = intent.getStringExtra("mainTitle")
        val director = intent.getStringExtra("director")
        val actor = intent.getStringExtra("actor")
        val genre = intent.getStringExtra("genre")

        if(supportFragmentManager.findFragmentByTag("MovieDataFragment") == null) {
            supportFragmentManager.beginTransaction()
                .add(R.id.movieDatalayout,
                    newMoviewDataFragment(movieTitle, director, actor, genre), "MovieDataFragment").commit()
        }
    }
}
