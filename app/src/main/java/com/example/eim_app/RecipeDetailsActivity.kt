package com.example.eim_app

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
import android.widget.ScrollView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken


@SuppressLint("Registered")
class RecipeDetailsActivity : AppCompatActivity() {
    lateinit var labelText : TextView
    lateinit var scrollView : ScrollView
    lateinit var ingredientText : TextView
    lateinit var saveButton : Button
    lateinit var sourceButton : Button

    // Recipe info
    var url : String? = ""
    var title : String? = ""
    var ingredients : MutableList<String>? = null
    var savedRecipes: ArrayList<RecipeSpoonacular> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recipe_details)
        Log.d("RecipeDetailsActivity", "onCreate()")

        labelText = findViewById(R.id.labelText)
        scrollView = findViewById(R.id.scrollView)
        ingredientText = scrollView.findViewById(R.id.ingredientText)
        saveButton = findViewById(R.id.saveButton)

        // LoadData and see it this recipe is already saved. Call saveData()
        saveButton.setOnClickListener() {
            loadData()
            if (!savedRecipes.any{ recipe -> recipe.title == title}) {
                Toast.makeText(this@RecipeDetailsActivity, "Recipe saved", Toast.LENGTH_SHORT).show()
                saveData()
            } else {
                Toast.makeText(this@RecipeDetailsActivity, "Already saved", Toast.LENGTH_SHORT).show()
            }
        }

        val intent = intent
        val extras = intent.extras
        if (extras != null) {
            title = extras.getString("title")
            ingredients =  extras.getString("ingredientLines")?.split("NEW")?.toMutableList()

            ingredientText.text = ingredients?.joinToString("\n\n")
            labelText.text = title
        }
    }

    // Add new recipe to savedRecipes and save savedRecipes
    private fun saveData() {
        val myRecipeObject = RecipeSpoonacular(title = title)
        savedRecipes.add(myRecipeObject)
        val sharedPreferences = getSharedPreferences("shared preferences", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        val json = Gson().toJson(savedRecipes)
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

    override fun onBackPressed() {
        val intent = Intent()
        setResult(RESULT_OK, intent)
        super.onBackPressed()
    }
}