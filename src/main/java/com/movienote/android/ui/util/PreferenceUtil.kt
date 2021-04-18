package com.movienote.android.ui.util

import android.content.Context
import android.content.Context.MODE_PRIVATE

class PreferenceUtil {

    companion object{

        //region --private propaties--

        // FISRT_ACTIVATE
        private const val FISRT_ACTIVATE = "First_Activate"

        // PREFERENCE_NAME
        private const val PREFERENCE_NAME = "Preference_Name"

        //endregion

        // region --public methods--

        // isFirstActivate
        fun isFirstActivate(context : Context) : Boolean{
            return context.getSharedPreferences(PREFERENCE_NAME,  MODE_PRIVATE).getBoolean(FISRT_ACTIVATE, true)
        }

        // saveFirstActivate
        fun saveFirstActivate(context : Context){
            val editor = context.getSharedPreferences(PREFERENCE_NAME,  MODE_PRIVATE).edit()
            editor.putBoolean(FISRT_ACTIVATE, false)
            editor.apply()
        }

        // endregion
    }
}
