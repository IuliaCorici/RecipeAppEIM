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
class SavedRecipeDetails : AppCompatActivity() {
    lateinit var labelText : TextView
    lateinit var scrollView : ScrollView
    lateinit var ingredientText : TextView
    lateinit var deleteButton : Button

    // Recipe info
    var url : String? = ""
    var title : String? = ""
    var ingredients : MutableList<String>? = null
    var savedRecipes: ArrayList<RecipeSpoonacular> = ArrayList()



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_saved_recipe_details)
        loadData()

        labelText = findViewById(R.id.labelText)
        scrollView = findViewById(R.id.scrollView)
        ingredientText = scrollView.findViewById(R.id.ingredientText)
        deleteButton = findViewById(R.id.deleteButton)

        val intent = intent
        val extras = intent.extras
        if (extras != null) {
            title = extras.getString("title")
            ingredients =  extras.getString("ingredientLines")?.split("NEW")?.toMutableList()

            ingredientText.text = ingredients?.joinToString("\n\n")
            labelText.text = title

            deleteButton.setOnClickListener() {
                deleteRecipe(it)
            }
        }
    }

    private fun deleteRecipe(button: View) {
        val newSavedRecipes : ArrayList<RecipeSpoonacular> = ArrayList()
        savedRecipes.filterTo(newSavedRecipes, {it.title != title})
        savedRecipes = newSavedRecipes
        saveData()
        this.finish()
    }

    private fun saveData() {
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

    override fun onRestart() {
        super.onRestart()
        this.finish()
    }
}