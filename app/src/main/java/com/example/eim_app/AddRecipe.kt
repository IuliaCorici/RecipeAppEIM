package com.example.eim_app

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.*
import androidx.core.net.toUri
import com.bumptech.glide.Glide
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.android.synthetic.main.activity_recipe_details.*

class AddRecipe : AppCompatActivity() {
    lateinit var labelText : EditText
    lateinit var scrollView : ScrollView
    lateinit var ingredientText : TextView
    lateinit var addPhotoButton : Button
    lateinit var addRecipeButton: Button
    lateinit var image : ImageView
    lateinit var summaryText: EditText
    lateinit var timeText: EditText
    private var imageUri: Uri? = null
    lateinit var innerLayout : LinearLayout

    // Recipe info
    private var title : String = ""
    var ingredients : MutableList<String>? = null
    var savedRecipes: ArrayList<RecipeSpoonacular> = ArrayList()
    var imageUriString : String? = ""
    var summary: String? = ""
    var time: Int? = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_recipe)

//        innerLayout = findViewById(R.id.innerLayoutNewRecipe)
//        labelText = innerLayout.findViewById(R.id.title_recipe)
//        summaryText = innerLayout.findViewById(R.id.summary_new_recipe)
//        timeText = innerLayout.findViewById(R.id.ready_in_minutes)
//        addRecipeButton = innerLayout.findViewById(R.id.addRecipeButton)
//        addPhotoButton = innerLayout.findViewById(R.id.addPhotoButton)
//        image = innerLayout.findViewById(R.id.add_recipe_image)
//
//        Log.d("add recipe", title)
//        addPhotoButton.setOnClickListener() {
//            val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
//            startActivityForResult(intent, 3);
//        }
//
//        addRecipeButton.setOnClickListener() {
//            loadData()
//            title = labelText.text.toString()
//            summary = summaryText.text.toString()
//            time = timeText.text.toString().toInt()
//
//            if (!savedRecipes.any{ recipe -> recipe.title == title}) {
//                Toast.makeText(this@AddRecipe, "Recipe saved", Toast.LENGTH_SHORT).show()
//                saveData()
//            } else {
//                Toast.makeText(this@AddRecipe, "Already saved", Toast.LENGTH_SHORT).show()
//            }
//        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK && data != null) {
            imageUri = data.data
            Glide.with(this).load(imageUri).into(image)
            imageUriString = imageUri.toString()
            Log.d("Uri ", imageUriString)
        }
    }

    // Add new recipe to savedRecipes and save savedRecipes
    private fun saveData() {
        val myRecipeObject = RecipeSpoonacular(title = title, summary = summary, readyInMinutes = time, imageUri = imageUriString)
        savedRecipes.add(myRecipeObject)
        val sharedPreferences = getSharedPreferences("shared preferences", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        val json = Gson().toJson(savedRecipes)
        Log.d("GOT RECIPES", json.toString())
        editor.putString("recipeList", json)
        editor.apply()
    }

    private fun loadData() {
        val sharedPreferences = getSharedPreferences("shared preferences", Context.MODE_PRIVATE)
        val json = sharedPreferences.getString("recipeList", ArrayList<String>().toString())
        val collectionType = object : TypeToken<ArrayList<RecipeSpoonacular>>() {}.type
        savedRecipes = Gson().fromJson(json, collectionType)
        Log.d("GOT RECIPES", savedRecipes.toString())
    }
}