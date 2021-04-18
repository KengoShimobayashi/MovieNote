package com.movienote.android.ui

import com.movienote.android.model.repository.MovieDataBase

class Application : android.app.Application() {

    override fun onCreate() {
        super.onCreate()

        MovieDataBase.init(this)
    }
}