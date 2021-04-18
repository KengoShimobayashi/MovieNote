package com.movienote.android.ui.main

import android.os.Bundle
import android.view.*
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.movienote.android.R
import com.movienote.android.databinding.FragmentMainBinding
import com.movienote.android.databinding.ItemHeaderBinding
import kotlinx.android.synthetic.main.fragment_main.*


class MainFragment : Fragment() {

    // region --private propaties--

    // mViewModel
    private val mViewModel : MainViewModel by activityViewModels()

    // mMainBinding
    private lateinit var mMainBinding : FragmentMainBinding

    // mListAdapter
    private lateinit var mListAdapter: MultiTypeAdapter

    // endregion

    // region --private methods--

    // setupFABButton
    private fun setupFAB() {
        val fab = this.fab_addMovieData
        fab?.setImageResource(R.drawable.ic_fab_add)
        fab?.setOnClickListener {
            this.mViewModel.addNewMovieData()
        }
    }

    // setupRecyclerView
    private fun setupRecyclerView(){

        // set divider
        val itemDecoration = DividerItemDecoration(context, DividerItemDecoration.VERTICAL)
        mMainBinding.dataList.addItemDecoration(itemDecoration)

        // set layoutmanager
        mMainBinding.dataList.layoutManager = LinearLayoutManager(context)

        // set adapter
        this.mListAdapter = MultiTypeAdapter(this.mViewModel, {
            this.mViewModel.mode.set(Mode.Select)
            this.clickItem(it)
        }, {
            if(this.mViewModel.mode.get() == Mode.Select){
                this.clickItem(it)
            }else {
                this.mViewModel.openDetailDialog(it.id)
            }
        })
        this.mListAdapter.setList(mutableListOf())

        mMainBinding.dataList.adapter = this.mListAdapter
    }

    // clickItem
    private fun clickItem(view : ViewItem){
        when(view){
            is ViewItem.DataItem -> view.isSelected = !view.isSelected
            else -> {}
        }

        if(this.mListAdapter.clickedItemCount() == 0)
            this.mViewModel.mode.set(Mode.Usual)
    }

    // endregion

    // region --public methods--

    // onCreateView
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {

        this.setHasOptionsMenu(true)

        this.mMainBinding = FragmentMainBinding.inflate(inflater, container, false)

        this.mMainBinding.view = this
        this.mMainBinding.mainViewModel = this.mViewModel

        return this.mMainBinding.root
    }

    // onActivityCreated
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        this.setupFAB()
        this.setupRecyclerView()
    }

    // onCreateOptionsMenu
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.main_option_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    // onPrepareOptionsMenu
    override fun onPrepareOptionsMenu(menu: Menu) {
        when(this.mViewModel.mode.get()){
            Mode.Select -> {
                menu.findItem(R.id.menu_delete)?.let{it.isVisible = true}
                menu.findItem(R.id.menu_search)?.let{it.isVisible = false}
            }
            else        -> {
                menu.findItem(R.id.menu_delete)?.let{it.isVisible = false}
                menu.findItem(R.id.menu_search)?.let{it.isVisible = true}
            }
        }
        return super.onPrepareOptionsMenu(menu)
    }

    // onOptionsItemSelected
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_delete -> {
                this.mViewModel.deleteItems()
                this.mViewModel.mode.set(Mode.Usual)
            }
            R.id.menu_search -> {
                this.mViewModel.onStartSearch()
            }
        }
        return super.onOptionsItemSelected(item);
    }

    // setViewModel
    fun setViewModel(viewModel : MainViewModel){
        //this.mViewModel = viewModel
    }

    // endregion

    // region --companion object--

    companion object {

        // newInstance
        fun newInstance() : MainFragment{
            return MainFragment()
        }
    }

    // endregion
}