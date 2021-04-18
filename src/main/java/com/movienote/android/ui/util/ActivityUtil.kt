package com.movienote.android.ui.util

import androidx.annotation.NonNull
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager

class ActivityUtil {

    companion object {

        // addFragmentToActivity
        fun addFragmentToActivity(@NonNull fragmentManager : FragmentManager, @NonNull fragment : Fragment, frameId : Int){
            checkNotNull(fragmentManager)
            checkNotNull(fragment)

            fragmentManager.beginTransaction()
                .add(frameId, fragment)
                .commit()
        }

        // addFragmentToActivity
        fun addFragmentToActivity(@NonNull fragmentManager : FragmentManager, @NonNull fragment : Fragment, frameTag :String){
            checkNotNull(fragmentManager)
            checkNotNull(fragment)

            fragmentManager.beginTransaction()
                .add(fragment, frameTag)
                .commit()
        }
    }
}