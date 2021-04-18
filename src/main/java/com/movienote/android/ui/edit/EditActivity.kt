package com.movienote.android.ui.edit

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.annotation.NonNull
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import com.movienote.android.R
import com.movienote.android.model.repository.*
import com.movienote.android.ui.ViewModelHolder
import com.movienote.android.ui.common.AddNewSingleItemFragment
import com.movienote.android.ui.common.EntityType
import com.movienote.android.ui.util.ActivityUtil

class EditActivity : AppCompatActivity(), EditNavigator, AddNewSingleItemFragment.NoticeDialogListener {

    // region --private propaties--

    // mEditViewModel
    private lateinit var mEditViewModel : EditViewModel

    // endregion

    // region --private methods--

    // findOrCreateMainTabFragment
    @NonNull
    private fun findOrCreateEditFragment() : EditFragment {
        var fragment  = supportFragmentManager.findFragmentById(R.id.contentFrame)
        if (fragment !is EditFragment) {
            fragment = EditFragment.newInstance()

            val bundle = Bundle()
            bundle.putString(
                EditFragment.ARGUMENT_EDIT_TASK_ID,
                intent.getStringExtra(EditFragment.ARGUMENT_EDIT_TASK_ID)
            )
            bundle.putInt(
                EditFragment.ARGUMENT_MENU_ID,
                intent.getIntExtra(EditFragment.ARGUMENT_MENU_ID, R.id.menu_moviedata)
            )
            fragment.arguments = bundle
            ActivityUtil.addFragmentToActivity(supportFragmentManager, fragment, R.id.contentFrame)
        }
        return fragment
    }

    // findOrCreateViewModel
    private fun findOrCreateViewModel() : EditViewModel {
        val viewModel : EditViewModel

        val fragment = supportFragmentManager.findFragmentByTag(EDIT_VIEWMODEL_TAG)
        if((fragment is ViewModelHolder<*>) && (fragment.mViewModel is EditViewModel)) {
            viewModel = fragment.mViewModel as EditViewModel
        }else{
            viewModel = EditViewModel(
                applicationContext,
                RepositoryMovie.getInstance(),
                RepositoryDirector.getInstance(),
                RepositoryActor.getInstance(),
                RepositoryGenre.getInstance(),
                RepositoryCountry.getInstance())

            ActivityUtil.addFragmentToActivity(
                supportFragmentManager, ViewModelHolder.createContainer(
                    viewModel
                ), EDIT_VIEWMODEL_TAG
            )
        }
        return viewModel
    }

    // setupToolBar
    private fun setupToolBar() {
        val toolBar = findViewById<Toolbar>(R.id.toolBar)
        setSupportActionBar(toolBar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
    }

    // endregion

    // region --public mehtods--

    //onCreate
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit)

        // Setup ToolBar
        this.setupToolBar()

        val editFragment = this.findOrCreateEditFragment()
        this.mEditViewModel = this.findOrCreateViewModel()

        this.mEditViewModel.setNavigator(this)

        editFragment.setViewModel(this.mEditViewModel)
    }

    // onSupportNavigateUp
    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    // showNewDialog
    override fun showNewDialog(entitytype : EntityType) {
        val dialog = AddNewSingleItemFragment(entitytype)
        dialog.show(supportFragmentManager, "newItem")
    }

    // onDialogPositiveClick
    override fun onDialogPositiveClick(dialog : DialogFragment, entityType : EntityType, result : String){
        this.mEditViewModel.addNewItem(entityType, result)
    }

    // onMovieDataSave
    override fun onMovieDataSave() {
        setResult(ADD_EDIT_RESULT_OK)
        finish()
    }

    // endregion

    // region --companion object

    companion object{

        // EDIT_VIEWMODEL_TAG
        const val EDIT_VIEWMODEL_TAG = "EDIT_VIEWMODEL_TAG"

        // ADD_EDIT_RESULT_OK
        const val ADD_EDIT_RESULT_OK = RESULT_FIRST_USER + 1

        // REQUEST_CODE
        const val REQUEST_CODE = 1
    }

    // endregion
}

/*

    override fun hasPermission() : Boolean {
        // Check permission is granted or not
        if (ContextCompat.checkSelfPermission(
                this, Manifest.permission.READ_EXTERNAL_STORAGE) !=
                            PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                this, arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE), 1)
            return false
        }
        return true
    }

    override fun getParentActivity(): Activity {
        return this
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        fragment.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(requestCode == 1000 && resultCode == Activity.RESULT_OK){
            fragment.onActivityResult(requestCode, resultCode, data)
        }
    }
}

 */
