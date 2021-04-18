package com.movienote.android.ui.datadetail

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.provider.ContactsContract
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.core.content.res.ResourcesCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.movienote.android.R
import com.movienote.android.databinding.FragmentAddNewSingleItemBinding
import com.movienote.android.databinding.FragmentDatadetailBinding
import com.movienote.android.generated.callback.OnClickListener
import com.movienote.android.misc.Standards
import com.movienote.android.model.entity.CountryDataEntity
import com.movienote.android.model.repository.*
import com.movienote.android.ui.common.AddNewSingleItemFragment
import com.movienote.android.ui.common.EntityType
import com.movienote.android.ui.main.MainActivity
import kotlinx.android.synthetic.main.fragment_add_new_single_item.view.*
import kotlinx.android.synthetic.main.fragment_datadetail.view.*

class DataDetailFragment(private val entityId : String, private val menuId: Int) : DialogFragment(){

    // region --listener--

    interface DataDetailDialogListener{
        fun onEditButtonClick(entityId : String, menuId: Int)
    }

    // endregion

    // region --private propaties--

    // viewModel
    private lateinit var viewModel : DataDetailViewModel

    // mListener
    private var mListener: DataDetailDialogListener? = null

    // endregion

    // region --public methods--

    // onAttach
    override fun onAttach(context: Context) {
        super.onAttach(context)

        try {
            this.mListener = context as DataDetailDialogListener
        } catch (e: ClassCastException) {
            throw ClassCastException("${context.toString()} must implement NoticeDialogListener")
        }
    }

    // onCreate
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        retainInstance = true

        // setup viewModel
        this.viewModel = DataDetailViewModel(
            RepositoryMovie.getInstance(),
            RepositoryDirector.getInstance(),
            RepositoryActor.getInstance(),
            RepositoryGenre.getInstance(),
            RepositoryCountry.getInstance())

            activity?.let{this.mListener = it as DataDetailDialogListener}
    }

    // onCreateDialog
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder = MaterialAlertDialogBuilder(activity, R.style.Theme_DataDetail_Dialog)
        val binding = DataBindingUtil.inflate<FragmentDatadetailBinding>(LayoutInflater.from(activity),
            R.layout.fragment_datadetail, null, false)

        this.viewModel.start(this.entityId, this.menuId)
        binding.viewModel = this.viewModel

        val view = binding.root
        view.bt_edit.setOnClickListener {
            this.dismiss()
            this.mListener?.onEditButtonClick(this.entityId, this.menuId)
        }
        return builder.setView(view).create()
    }

    // endregion

    // region companion object

    companion object{

        // showDataDetailDialog
        fun showDataDetailDialog(entityId : String, menuId : Int) : DataDetailFragment{
            return DataDetailFragment(entityId, menuId)
        }
    }

    // endregion
}