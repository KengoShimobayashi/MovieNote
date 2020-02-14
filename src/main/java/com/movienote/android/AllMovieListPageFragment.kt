package com.movienote.android

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.ColorFilter
import android.net.Uri
import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.support.v4.app.Fragment
import android.view.*
import android.widget.Button
import java.lang.RuntimeException

var Listupdate = false

class AllMovieListPageFragment : Fragment(){
    private var mListener : OnFragmentInteractionListener? = null

    private lateinit var fragment : MovieListFragment
    private lateinit var addButton: Button
    private lateinit var searchButon: Button

    internal interface OnFragmentInteractionListener{
        fun onFragmentInteraction(uri: Uri)
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)

        if(context is OnFragmentInteractionListener)
            mListener = context
        else
            throw RuntimeException()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //setHasOptionsMenu(true)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        val view = inflater.inflate(R.layout.fragment_listpage, container, false)

        addButton = view.findViewById(R.id.btn_add)
        searchButon = view.findViewById(R.id.btn_search)
        addButton.setOnClickListener {
            val intent = Intent(context, RegisterActivity::class.java)
            startActivityForResult(intent, RegisterMode.ADD.rawValue)
        }
        searchButon.setOnClickListener {
            val intent = Intent(context, SearchActivity::class.java)
            startActivityForResult(intent, RegisterMode.SEARCH.rawValue)
        }

        val transaction = childFragmentManager.beginTransaction()
        fragment = MovieListFragment()
        transaction.add(R.id.allMovieList, fragment, "child").commit()

        return view
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(resultCode == Activity.RESULT_OK){
                Listupdate = true
                fragment.onActivityResult(requestCode, resultCode, data)
        }
    }

    /*
    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater?.inflate(R.menu.optionmenu, menu)
    }
    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when(item?.itemId){
            R.id.action_serach -> {

            }
            else -> {
                // do nothing
            }
        }

        return super.onOptionsItemSelected(item)
    }
    */

    companion object{
        //var updated = false

        fun newInstance() : AllMovieListPageFragment {
            val fragment = AllMovieListPageFragment()
            return fragment
        }
    }
}