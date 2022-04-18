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
    lateinit var sourceButton : Button

    // Recipe info
    var url : String? = ""
    var label : String? = ""
    var ingredients : MutableList<String>? = null
    var source : String? = ""
    var savedRecipes: ArrayList<Recipe> = ArrayList()



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_saved_recipe_details)
        Log.d("SavedRecipeDetails", "onCreate()")

        loadData()

        labelText = findViewById(R.id.labelText)
        scrollView = findViewById(R.id.scrollView)
        ingredientText = scrollView.findViewById(R.id.ingredientText)
        sourceButton = findViewById(R.id.sourceButton)
        deleteButton = findViewById(R.id.deleteButton)

        val intent = intent
        val extras = intent.extras
        if (extras != null) {
            label = extras.getString("label")
            source = extras.getString("source")
            url = extras.getString("sourceUrl").toString()
            ingredients =  extras.getString("ingredientLines")?.split("#")?.toMutableList()

            ingredientText.text = ingredients?.joinToString("\n\n")
            labelText.text = label
            sourceButton.text = source

            sourceButton.setOnClickListener() {
                sourceButtonClicked(it)
            }

            deleteButton.setOnClickListener() {
                deleteRecipe(it)
            }
        }
    }

    private fun deleteRecipe(button: View) {
        val newSavedRecipes : ArrayList<Recipe> = ArrayList()
        savedRecipes.filterTo(newSavedRecipes, {it.label != label})
        savedRecipes = newSavedRecipes
        saveData()
        this.finish()
    }

    // Add new recipe to savedRecipes and save savedRecipes
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
        val collectionType = object : TypeToken<ArrayList<Recipe>>() {}.type
        savedRecipes = Gson().fromJson(json, collectionType)
        Log.d("GOT RECIPES", savedRecipes.toString())
    }

    // Start activity on browser with url
    private fun sourceButtonClicked(button: View) {
        val intent = Intent(Intent.ACTION_VIEW)
        intent.data = Uri.parse(url)
        startActivity(intent)
    }

    override fun onRestart() {
        super.onRestart()
        this.finish()
    }
}