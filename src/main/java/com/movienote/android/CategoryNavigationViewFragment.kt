package com.movienote.android

import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.support.v4.app.Fragment
import android.view.*

class CategoryNavigationViewFragment : Fragment(){

    interface OnNavigationClickListener{
        fun onNavigationClick(category: Int)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_buttomnavigationbar, container, false)

        val navigationBar = view.findViewById<BottomNavigationView>(R.id.navigationBar)
        navigationBar.inflateMenu(R.menu.movie_bottom_navigation_item)

        navigationBar.setOnNavigationItemSelectedListener {

            val listener = parentFragment as? OnNavigationClickListener
            when (it.itemId) {
                R.id.nav_director -> {
                    listener?.onNavigationClick(0)
                    true
                }
                R.id.nav_actor -> {
                    listener?.onNavigationClick(1)
                    true
                }
                R.id.nav_genre -> {
                    listener?.onNavigationClick(2)
                    true
                }
                else -> false
            }
        }
        return view
    }
}