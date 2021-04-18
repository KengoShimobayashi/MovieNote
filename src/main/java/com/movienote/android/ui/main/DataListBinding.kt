package com.movienote.android.ui.main

import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView

class DataListBinding {

    companion object {

        @SuppressWarnings("unchecked")
        @BindingAdapter("items")
        @JvmStatic
        fun setItems(view : RecyclerView, list : List<ViewItem>){
            val adapter = view.adapter
            if(adapter is MultiTypeAdapter) {
                adapter.setList(list)
            }
        }
    }
}