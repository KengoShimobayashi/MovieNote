package com.movienote.android

import android.annotation.SuppressLint
import android.support.design.internal.BottomNavigationItemView
import android.support.design.internal.BottomNavigationMenuView
import android.support.design.widget.BottomNavigationView
import android.util.Log

class BottomNavigationViewHelper {

    @SuppressLint("RestrictedApi")
    fun disableShiftMode(view: BottomNavigationView){
        val menuView = view.getChildAt(0) as BottomNavigationMenuView

        // tryは必須
        try {
            // アニメーションをなくす
            val mode = menuView.javaClass.getDeclaredField("mShiftingMode")
            //mode.isAccessible = true
            mode.setBoolean(menuView, false)
            mode.isAccessible = false

            for (i in 0 until menuView.childCount - 1) {
                val itemView = menuView.getChildAt(i) as BottomNavigationItemView
                itemView.setShifting(false)
                itemView.setChecked(itemView.itemData.isChecked)
            }
        }catch (e: NoSuchFieldException){
            Log.e("BNVHelper",
                "Unable to get shift mode field", e)
        }catch (e: IllegalAccessException) {
            Log.e("BNVHelper",
                "Unable to change value of shift mode", e)
        }
    }
}