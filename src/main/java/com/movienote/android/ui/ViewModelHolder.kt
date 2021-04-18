package com.movienote.android.ui

import android.os.Bundle
import androidx.annotation.NonNull
import androidx.annotation.Nullable
import androidx.fragment.app.Fragment

class ViewModelHolder<VM : Any> : Fragment() {

    // region --private propaties--

    // mViewModel
    @Nullable
    var mViewModel : VM? = null

    // endregion

    // region --public methods--

    // onCreate
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        retainInstance = true;
    }

    // endregion

    // region --companion object--

    companion object{

        // createContainer
        fun <M : Any> createContainer(@NonNull viewModel : M) : ViewModelHolder<M>{
            val viewModelContainer : ViewModelHolder<M> = ViewModelHolder()
            viewModelContainer.mViewModel = viewModel
            return viewModelContainer
        }

    }

    // endregion
}