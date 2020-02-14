package com.movienote.android

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter

class MainTabAdapter (fm: FragmentManager?) : FragmentPagerAdapter(fm){

    private val fragmentList: ArrayList<Fragment> = ArrayList()

    override fun getCount(): Int {
        return fragmentList.size
    }

    override fun getItem(position: Int): Fragment {
        return fragmentList[position]
    }

    /*
    override fun getPageTitle(position: Int): CharSequence? {
        return titles[position]
    }
    */

    fun add(fragment: Fragment){
        fragmentList.add(fragment)
    }
}