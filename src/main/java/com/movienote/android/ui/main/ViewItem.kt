package com.movienote.android.ui.main

import androidx.databinding.BaseObservable
import androidx.databinding.Observable
import androidx.databinding.ObservableBoolean

sealed class ViewItem(val id : String, val mainValue : String)  : BaseObservable() {

    class HeaderItem(id : String, index : String) : ViewItem(id, index){

        // region --public propaties--

        // dataList
        val dataList : MutableList<DataItem> = mutableListOf()

        // endregion

        // region --public methods--

        // deleteFlag
        override fun deleteFlag() : Boolean{
            var r = true
            for(i in this.dataList.indices){
                if(!this.dataList[i].isSelected) {
                    r = false
                    break
                }
            }
            return r
        }

        // endregion
    }
    class DataItem(id : String, mainValue : String, val subValue : String?, private val selectStatus : ObservableBoolean) : ViewItem(id, mainValue){

        // region --public propaties--

        // hasSubValue
        val hasSubValue : Boolean
            get(){return !this.subValue.isNullOrBlank()}

        // isSelected
        var isSelected : Boolean
            get() {return this.selectStatus.get()}
            set(value) {this.selectStatus.set(value) }

        // endregion

        // region --public methods--

        // constructor
        constructor(id : String, mainValue : String, subValue : String?) : this(id, mainValue, subValue, ObservableBoolean(false))

        // init
        init{
            this.selectStatus.addOnPropertyChangedCallback(object : Observable.OnPropertyChangedCallback() {
                override fun onPropertyChanged(sender: Observable?, propertyId: Int) {
                    notifyChange()
                }
            })
        }

        // deleteFlag
        override fun deleteFlag() : Boolean{
            return this.isSelected
        }

        // endregion
    }

    // region --public methods--

    // deleteFlag
    abstract fun deleteFlag() : Boolean

    // endregion
}