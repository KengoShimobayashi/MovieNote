package com.movienote.android.misc

import android.content.Context
import androidx.annotation.StringRes

interface StringResource {
    fun apply(context : Context) : String

    companion object{
        fun from(@StringRes resID : Int, vararg formatArgs : Any) = object : StringResource{
            override fun apply(context: Context) = context.getString(resID, formatArgs)
        }
    }
}