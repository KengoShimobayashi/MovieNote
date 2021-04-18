package com.movienote.android.misc

class Standards {

    // region --companion object--

    companion object{

        // region --public propaties--

        // Empty
        const val Empty = ""

        // noChoose
        const val NoChoose = "---"

        // endregion

        // region --public methods--

        infix fun isNoChoose(value : String?) : Boolean{
            return value == NoChoose
        }

        // endregion

    }

    // endregion
}