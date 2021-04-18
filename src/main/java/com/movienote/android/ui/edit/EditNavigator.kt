package com.movienote.android.ui.edit

import com.movienote.android.ui.common.EntityType

interface EditNavigator {

    fun showNewDialog(entitytype : EntityType)

    fun onMovieDataSave()
}