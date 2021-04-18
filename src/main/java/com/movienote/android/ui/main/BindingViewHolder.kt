package com.movienote.android.ui.main

import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView

class BindingViewHolder(v : View) : RecyclerView.ViewHolder(v) {

    // region public propaties--

    // binding
    val binding : ViewDataBinding = DataBindingUtil.bind(v)!!

    // endregion
}