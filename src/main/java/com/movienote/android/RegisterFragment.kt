package com.movienote.android

import android.app.Activity
import android.content.Intent
import android.graphics.*
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.Fragment
import android.text.*
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import kotlin.experimental.and
import kotlin.experimental.inv
import kotlin.experimental.or
import android.provider.MediaStore
import java.io.IOException
import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import java.lang.RuntimeException


// Regex for furigana
private const val MATCH_HIRAGANA = "\\u3040-\\u309F"
private const val MATCH_ASCII = "\\u0020-\\u007E"
private const val MATCH_SYMBOL = "\\u30FB-\\u30FC"

// RequestCode
private const val CHOSEN_IMAGE = 1000

// ErrorType
enum class ErrorType(val rawValue: Byte) {
    NO_ERROR(0x00),
    NO_TITLE(0x01),
    LIMIT_CHAR(0x10);
}

// ImageViewStatus
enum class ImageViewStatus(val rawValue: Int) {
    NO_IMAGE(0),
    SET_IMAGE(1),
}

enum class IsFavorite(val rawValue: Int){
    UNSELECTED(R.drawable.ic_favorite_unselected_30dp),
    SELECTED(R.drawable.ic_favorite_selected_30dp),
}

class RegisterFragment : Fragment() {

    interface OnRegisterFragmentInteractionListener {
        fun hasPermission(): Boolean
        fun getParentActivity(): Activity
    }

    private lateinit var parentActivity: Activity
    private lateinit var listener: OnRegisterFragmentInteractionListener
    private lateinit var imageView: ImageView

    private var error = ErrorType.NO_ERROR.rawValue
    private var imageViewStatus = ImageViewStatus.NO_IMAGE.rawValue
    private var imageViewUri = ""
    private var isFavorite = IsFavorite.UNSELECTED
    private val dialogClickListener = DialogInterface.OnClickListener { _, which ->
        when (which) {
            0 -> {
                val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                intent.type = "image/*"
                startActivityForResult(intent, CHOSEN_IMAGE)
            }
            1 -> {
                imageView.background = context?.getDrawable(R.drawable.imageview_noimage)
                imageViewStatus = ImageViewStatus.NO_IMAGE.rawValue
                imageViewUri = ""
            }
        }
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)

        if (context is OnRegisterFragmentInteractionListener) {
            listener = activity as OnRegisterFragmentInteractionListener
            parentActivity = listener.getParentActivity()
        }
        else
            throw RuntimeException()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        val view = inflater.inflate(R.layout.fragment_register, container, false)

        // scrollView
        val scrollView = view.findViewById<ScrollView>(R.id.scrollview)

        // mainTitle
        val mainTitle = view.findViewById<EditText>(R.id.pltxt_mainTitle)

        // mainTitle.addTextChangeListener
        mainTitle.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

            override fun afterTextChanged(s: Editable?) {
                mainTitle.background = resources.getDrawable(R.drawable.edittext_custom, null)
            }
        })

        // subTitle
        val subTitle = view.findViewById<EditText>(R.id.pltxt_subTitle)

        // furigana
        val furigana = view.findViewById<EditText>(R.id.pltxt_furigana)

        // furigana addTextCHangeListener
        furigana.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

            override fun afterTextChanged(s: Editable?) {
                val input = s ?: ""
                val editType: Int
                if (!input.matches(toMatchRegex(MATCH_SYMBOL + MATCH_ASCII + MATCH_HIRAGANA)) && input.isNotEmpty()) {
                    error = error or ErrorType.LIMIT_CHAR.rawValue
                    furigana.error = resources.getString(R.string.errorLimitChar)
                    editType = R.drawable.edittext_error
                } else {
                    error = error and ErrorType.LIMIT_CHAR.rawValue.inv()
                    editType = R.drawable.edittext_custom
                }
                furigana.background = resources.getDrawable(editType, null)
            }

            fun toMatchRegex(codes: String): Regex {
                return Regex("^[$codes]+$")
            }
        })

        // directorName
        val directorName = view.findViewById<EditText>(R.id.pltxt_director)

        // actorName
        val actorName = view.findViewById<EditText>(R.id.pltxt_actor)

        // genre
        val genre = view.findViewById<Spinner>(R.id.sp_genre)
        val genreList = mutableListOf<String>()
        MainActivity.genreList.forEach {
            genreList.add(it.item)
        }

        // country
        val country = view.findViewById<Spinner>(R.id.sp_country)
        val countryList = this.resources.getStringArray(R.array.countryType)

        if (context != null) {
            val genreAdapter =
                ArrayAdapter(context, R.layout.spinner_item, genreList)
                    .also { adapter -> adapter.setDropDownViewResource(R.layout.spinner_drop_down_item) }
            genre.adapter = genreAdapter

            val countryAdapter =
                ArrayAdapter(context, R.layout.spinner_item, countryList)
                    .also { adapter -> adapter.setDropDownViewResource(R.layout.spinner_drop_down_item) }
            country.adapter = countryAdapter
        }

        // imageView
        imageView = view.findViewById(R.id.imageView)

        // rateBar
        val rateBar = view.findViewById<RatingBar>(R.id.ratingBar)

        // favorite
        val favorite = view.findViewById<ImageView>(R.id.favorite)
        favorite.setOnClickListener {
            isFavorite = when(isFavorite){
                IsFavorite.UNSELECTED -> IsFavorite.SELECTED
                else -> IsFavorite.UNSELECTED
            }
            favorite.setImageResource(isFavorite.rawValue)
        }

        // saveButton
        val saveButton = view.findViewById<Button>(R.id.btn_save)

        // saveButton ClickListener
        saveButton.setOnClickListener {

            // Check Title EditText is empty or not
            error = when (mainTitle.text.toString().isEmpty()) {
                true -> error or ErrorType.NO_TITLE.rawValue
                false -> error and ErrorType.NO_TITLE.rawValue.inv()
            }

            when (error) {
                // No error
                ErrorType.NO_ERROR.rawValue -> {
                    val intent = Intent()
                    intent.putExtra("id", parentActivity.intent.getIntExtra("id", 0))
                    intent.putExtra("mainTitle", mainTitle.text.toString().trim())
                    intent.putExtra("subTitle", subTitle.text.toString().trim())
                    intent.putExtra("furigana", furigana.text.toString().trim())
                    intent.putExtra("director", directorName.text.toString().trim())
                    intent.putExtra("actor", actorName.text.toString().trim())
                    intent.putExtra("genre", genre.selectedItem.toString())
                    intent.putExtra("country", country.selectedItemPosition)
                    intent.putExtra("uri", imageViewUri)
                    intent.putExtra("rate", rateBar.rating.toInt())
                    intent.putExtra("favorite", isFavorite.rawValue)

                    parentActivity.setResult(Activity.RESULT_OK, intent)
                    parentActivity.finish()
                }
                // Error
                else -> {
                    // If no title was input
                    if (error and ErrorType.NO_TITLE.rawValue == ErrorType.NO_TITLE.rawValue) {
                        mainTitle.background =
                            resources.getDrawable(R.drawable.edittext_error, null)
                        mainTitle.error = resources.getString(R.string.errorNoTitle)
                    }

                    // If inappropriate char was input
                    if (error and ErrorType.LIMIT_CHAR.rawValue == ErrorType.LIMIT_CHAR.rawValue)
                        furigana.error = resources.getString(R.string.errorLimitChar)

                    // Scroll to top
                    scrollView.scrollTo(0, scrollView.top)
                }
            }
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

        if (parentActivity.intent.getIntExtra("flag", -1) == RegisterMode.MODIFY.rawValue) {
            mainTitle.text = SpannableStringBuilder(parentActivity.intent.getStringExtra("mainTitle"))
            subTitle.text = SpannableStringBuilder(parentActivity.intent.getStringExtra("subTitle"))
            furigana.text = SpannableStringBuilder(parentActivity.intent.getStringExtra("furigana"))

            val directorID = parentActivity.intent.getIntExtra("director", 0)
            directorName.text = SpannableStringBuilder(when(directorID != -1 ) {
                true -> MainActivity.directorList.filter { it.id == directorID }[0].item
                false -> ""
            })

            val actorID = parentActivity.intent.getIntExtra("actor", 0)
            actorName.text = SpannableStringBuilder(when(actorID != -1){
                true -> MainActivity.actorList.filter { it.id == actorID}[0].item
                false -> ""
            })


            val genreList = MainActivity.genreList
            val genreID = parentActivity.intent.getIntExtra("genre", 0)
            var index = 0
            for(i:Int in 0 until genreList.count()){
                if(genreList[i].id == genreID){
                    index = i
                    break
                }
            }
            genre.setSelection(index)

            country.setSelection(parentActivity.intent.getIntExtra("country", 0))
            rateBar.rating = parentActivity.intent.getIntExtra("rate", 0).toFloat()
            favorite.setImageResource(parentActivity.intent.getIntExtra("favorite", IsFavorite.UNSELECTED.rawValue))

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