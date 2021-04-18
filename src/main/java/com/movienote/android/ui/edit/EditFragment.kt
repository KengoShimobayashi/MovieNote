package com.movienote.android.ui.edit

import android.os.Bundle
import android.view.*
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.movienote.android.R
import com.movienote.android.databinding.FragmentEditBinding

// RequestCode
private const val CHOSEN_IMAGE = 1000

class EditFragment : Fragment(){

    // region --private propaties--

    // mEditViewModel
    private lateinit var mEditViewModel : EditViewModel

    // mEditBinding
    private lateinit var mEditBinding : FragmentEditBinding

    // endregion

    // region --private methoda--

    // setupActionBar
    private fun setupActionBar() {
        val actionBar: ActionBar = (activity as AppCompatActivity?)?.supportActionBar ?: return
        if (arguments!![ARGUMENT_EDIT_TASK_ID] != null) {
            actionBar.setTitle(R.string.actionBar_title_edit)
        } else {
            actionBar.setTitle(R.string.actionBar_title_add)
        }
    }

    // endregion

    // region --public methods--

    // onActivityCreated
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        this.setupActionBar()
    }

    // onCreateView
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {

        this.mEditBinding = FragmentEditBinding.inflate(inflater, container, false)
        this.mEditBinding.view = this
        this.mEditBinding.editViewModel = this.mEditViewModel

        setHasOptionsMenu(true)

        return this.mEditBinding.root
    }

    // onResume
    override fun onResume() {
        val entityId = arguments?.getString(ARGUMENT_EDIT_TASK_ID)
        val menuId = arguments?.getInt(ARGUMENT_MENU_ID) ?: R.id.menu_moviedata

        this.mEditViewModel.start(entityId, menuId)
        super.onResume()
    }

    // onCreateOptionsMenu
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.optionmenu, menu)
    }

    // onOptionsItemSelected
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.menu_save -> this.mEditViewModel.save()
        }
        return super.onOptionsItemSelected(item)
    }

    // setViewModel
    fun setViewModel(viewModel: EditViewModel){
        this.mEditViewModel = viewModel
    }

    // endregion

    // region --companion object--

    companion object {

        // ARGUMENT_EDIT_TASK_ID
        const val ARGUMENT_EDIT_TASK_ID = "EDIT_TASK_ID"

        // ARGUMENT_MENU_ID
        const val ARGUMENT_MENU_ID = "ARGUMENT_MENU_ID"

        // newInstance
        fun newInstance() : EditFragment {
            val fragment = EditFragment()
            return fragment
        }
    }

    // endregion
}

/*
// ImageViewStatus
enum class ImageViewStatus(val rawValue: Int) {
    NO_IMAGE(0),
    SET_IMAGE(1),
}

class RegisterFragment : Fragment() {

    interface OnRegisterFragmentInteractionListener {
        fun hasPermission(): Boolean
        fun getParentActivity(): Activity
    }

        // imageView ClickListener
        imageView.setOnClickListener {

            // Make List of dialog
            val itemList = when (imageViewStatus) {
                ImageViewStatus.SET_IMAGE.rawValue -> this.resources.getStringArray(R.array.addImage_Set)
                else -> this.resources.getStringArray(R.array.addImage_No)
            }

            if (listener.hasPermission()) {

                AlertDialog.Builder(context).apply {
                    setItems(itemList, dialogClickListener)
                    show()
                }
            }
        }

            val uriStr = parentActivity.intent.getStringExtra("uri")
            if(uriStr.isNotEmpty()){
                var uri: Uri?= Uri.parse(uriStr)
                setImage(uri)
            }
        }
        return view
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, resultData: Intent?) {
        if (requestCode == CHOSEN_IMAGE && resultCode == Activity.RESULT_OK) {
            if (resultData != null)
                setImage(resultData.data)
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        if (requestCode == 1 && grantResults[0] == 0) {
            // Make List of dialog
            val itemList = when (imageViewStatus) {
                ImageViewStatus.SET_IMAGE.rawValue -> this.resources.getStringArray(R.array.addImage_Set)
                else -> this.resources.getStringArray(R.array.addImage_No)
            }

            AlertDialog.Builder(context).apply {
                setItems(itemList, dialogClickListener)
                show()
            }
        }
    }

    private fun setImage(uri: Uri?){
        try {
            val bitmap = MediaStore.Images.Media.getBitmap(context?.contentResolver, uri)
            imageView.background = BitmapDrawable(resources, bitmap)

            val filter = LightingColorFilter(Color.GRAY, 0)
            imageView.background.colorFilter = filter
            imageViewStatus = ImageViewStatus.SET_IMAGE.rawValue
            imageViewUri = uri.toString()
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }
}

 */