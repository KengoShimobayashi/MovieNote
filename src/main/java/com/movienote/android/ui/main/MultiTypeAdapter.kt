package com.movienote.android.ui.main

import android.view.*
import androidx.databinding.ObservableList
import androidx.lifecycle.LifecycleObserver
import androidx.recyclerview.widget.RecyclerView
import com.movienote.android.R
import com.movienote.android.databinding.ItemDataBinding
import com.movienote.android.databinding.ItemHeaderBinding

class MultiTypeAdapter(private val viewModle: MainViewModel, private val onLongClick: (viewItem : ViewItem) -> Unit, private val onClick: (viewItem : ViewItem) -> Unit)
    : RecyclerView.Adapter<BindingViewHolder>(){

    // region --private propaties--

    // contents
    private val contents = mutableListOf<ViewItem>()

    // endregion

    // region --public mehtods--

    // getItemCount
    override fun getItemCount() = this.contents.count()

    // getItemViewType
    override fun getItemViewType(position: Int): Int {
        return when(this.contents[position]) {
            is ViewItem.HeaderItem -> VIEW_TYPE_HEADER
            is ViewItem.DataItem -> VIEW_TYPE_DATA
        }
    }

    // onCreateViewHolder
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) : BindingViewHolder {
        return when (viewType) {
            VIEW_TYPE_HEADER -> BindingViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_header, parent, false))
            VIEW_TYPE_DATA -> {
                val view = LayoutInflater.from(parent.context).inflate(R.layout.item_data, parent, false)
                val holder = BindingViewHolder(view)
                view.setOnLongClickListener {
                    this.onLongClick(this.contents[holder.adapterPosition])
                    true
                }
                view.setOnClickListener {
                    this.onClick(this.contents[holder.adapterPosition])
                }
                holder
            }
            else -> throw IllegalArgumentException("Unknown viewType $viewType") // この処理は呼ばれない
        }
    }

    // onBindViewHolder
    override fun onBindViewHolder(holder: BindingViewHolder, position: Int) {
        when(val item = this.contents[position])
        {
            is ViewItem.HeaderItem -> (holder.binding as ItemHeaderBinding).content = item
            is ViewItem.DataItem -> {
                (holder.binding as ItemDataBinding).content = item
                holder.binding.viewModel = this.viewModle
            }
        }
    }

    // setList
    fun setList(list : List<ViewItem>){
        this.contents.clear()
        this.contents.addAll(list)
        this.notifyDataSetChanged()
        //this.notifyItemRangeChanged(0, this.itemCount)
    }

    // clickedItemCount
    fun clickedItemCount() : Int{
        var count = 0
        this.contents.forEach {
            when(it){
                is ViewItem.DataItem -> if(it.isSelected) count++
                else -> {}
            }
        }
        return count
    }

    // endregion

    // region --companion object--

    companion object {

        // region --private propaties--

        // Veiw type ID
        const val VIEW_TYPE_HEADER = 1
        const val VIEW_TYPE_DATA = 2

        // endregion
    }

    // endregion

}

/*
                        // Make bitmap
                        var uri: Uri?
                        if (movieData.imageUri.isNotEmpty()) {
                            uri = Uri.parse(movieData.imageUri)

                            try {
                                val bitmap =
                                    MediaStore.Images.Media.getBitmap(context?.contentResolver, uri)
                                showImageView.setImageBitmap(bitmap)
                            } catch (e: IOException) {
                                e.printStackTrace()
                            }
                        }


 */