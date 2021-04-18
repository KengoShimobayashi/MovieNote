package com.movienote.android.ui.common

import android.annotation.SuppressLint
import androidx.appcompat.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import androidx.core.content.res.ResourcesCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.movienote.android.R
import com.movienote.android.databinding.FragmentAddNewSingleItemBinding
import com.movienote.android.misc.Standards
import kotlinx.android.synthetic.main.fragment_add_new_single_item.view.*


enum class EntityType{
    GENRE,
    COUNTRY
}

class AddNewSingleItemFragment(private val entitytype: EntityType) : DialogFragment() {

    // region --listener--

    interface NoticeDialogListener{
        fun onDialogPositiveClick(dialog: DialogFragment, entityType: EntityType, result: String)
    }

    // endregion

    // region --private propaties--

    // mListener
    private var mListener: NoticeDialogListener? = null

    // endregion

    // region --private methods--

    private fun getMessage() : String{
        return when(entitytype){
            EntityType.GENRE -> resources.getString(R.string.addGenreMessage)
            EntityType.COUNTRY -> resources.getString(R.string.addCountryMessage)
        }
    }

    // endregion

    // region --public methods--

    // onAttach
    override fun onAttach(context: Context) {
        super.onAttach(context)

        try {
            this.mListener = context as NoticeDialogListener
        } catch (e: ClassCastException) {
            throw ClassCastException("${context.toString()} must implement NoticeDialogListener")
        }
    }

    // onCreate
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        retainInstance = true
    }

    // onCreateDialog
    @SuppressLint("ResourceType")
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder = MaterialAlertDialogBuilder(activity)
        val binding = DataBindingUtil.inflate<FragmentAddNewSingleItemBinding>(LayoutInflater.from(activity),
            R.layout.fragment_add_new_single_item, null, false)
        val view = binding.root

        builder.setView(view)
            .setMessage(this.getMessage())
            .setPositiveButton(resources.getString(R.string.postiveButtonText)) { _, _ ->
                this.mListener?.onDialogPositiveClick(this, entitytype, binding.text ?: Standards.Empty)
            }
            .setNegativeButton(resources.getString(R.string.negativeButtonText), null)

        val dialog =  builder.create()

        dialog.setOnShowListener {
            dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(ResourcesCompat.getColorStateList(resources, R.color.dialog_color_selector, null))
            dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(ResourcesCompat.getColorStateList(resources, R.color.dialog_color_selector, null))
            dialog.getButton(AlertDialog.BUTTON_POSITIVE).isEnabled = false
        }
        view.modifyText.addTextChangedListener(object : TextWatcher{
            override fun afterTextChanged(s: Editable?) {}
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                dialog.getButton(AlertDialog.BUTTON_POSITIVE).isEnabled = !s.isNullOrBlank()
            }
        })

        return dialog
    }

    // onDetach
    override fun onDetach() {
        super.onDetach()
        this.mListener = null
    }

    // endregion
}
