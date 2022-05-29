package com.example.eim_app

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class FirstFragment : Fragment() {

    lateinit var labelText : EditText
    lateinit var addPhotoButton : Button
    lateinit var image : ImageView
    lateinit var summaryText: EditText
    lateinit var timeText: EditText
    private var imageUri: Uri? = null
    lateinit var innerLayout : LinearLayout
    lateinit var nextPage: Button

    // Recipe info
    private var title : String = ""
    var ingredients : MutableList<String>? = null
    var imageUriString : String? = ""
    var summary: String? = ""
    var time: Int? = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_first, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        innerLayout = view.findViewById(R.id.innerLayoutNewRecipe)
        labelText = innerLayout.findViewById(R.id.title_recipe)
        summaryText = innerLayout.findViewById(R.id.summary_new_recipe)
        timeText = innerLayout.findViewById(R.id.ready_in_minutes)
        addPhotoButton = innerLayout.findViewById(R.id.addPhotoButton)
        nextPage = innerLayout.findViewById(R.id.button_first)
        image = innerLayout.findViewById(R.id.add_recipe_image)



        addPhotoButton.setOnClickListener() {
            val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(intent, 3);
        }


        nextPage.setOnClickListener() {
            title = labelText.text.toString()
            summary = summaryText.text.toString()
            time = timeText.text.toString().toInt()
            val action = imageUriString?.let { it1 ->
                FirstFragmentDirections.actionFirstFragmentToSecondFragment(title,
                    summary!!, time!!, it1
                )
            }

            if (action != null) {
                findNavController().navigate(action)
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == AppCompatActivity.RESULT_OK && data != null) {
            imageUri = data.data
            Glide.with(this).load(imageUri).into(image)
            imageUriString = imageUri.toString()
            Log.d("Uri ", imageUriString)
        }
    }
}