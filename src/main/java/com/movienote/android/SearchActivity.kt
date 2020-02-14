package com.movienote.android

import android.app.Activity
import android.support.v7.app.AppCompatActivity
import android.os.Bundle

class SearchActivity : AppCompatActivity(), SearchFragment.OnAttachListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)
    }

    override fun onAttachListener() : Activity {
        return this
    }
}
