package com.example.eim_app

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.net.toUri
import com.bumptech.glide.Glide
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken


@SuppressLint("Registered")
class SavedRecipeDetails : AppCompatActivity() {
    lateinit var labelText : TextView
    lateinit var scrollView : ScrollView
    lateinit var ingredientText : TextView
    lateinit var deleteButton : Button
    lateinit var sourceButton : Button
    lateinit var timeButton : Button
    lateinit var image : ImageView
    lateinit var summaryText: TextView

    // Recipe info
    var title : String? = ""
    var ingredients : MutableList<String>? = null
    var savedRecipes: ArrayList<RecipeSpoonacular> = ArrayList()
    var source : String? = ""
    var summary: String? = ""
    var time: Int? = 0
    var imageUrl: String? = ""
    var imageUri: Uri? = null



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_saved_recipe_details)
        loadData()

        labelText = findViewById(R.id.labelText)
        scrollView = findViewById(R.id.scrollView)
        ingredientText = scrollView.findViewById(R.id.ingredientText)
        deleteButton = findViewById(R.id.deleteButton)
        sourceButton = findViewById(R.id.sourceButton)
        timeButton = findViewById(R.id.timeButton)
        image = findViewById(R.id.imageView)
        summaryText = scrollView.findViewById(R.id.summaryText)

        val intent = intent
        val extras = intent.extras
        if (extras != null) {
            title = extras.getString("title")
            ingredients =  extras.getString("ingredientLines")?.split("NEW")?.toMutableList()

            ingredientText.text = ingredients?.joinToString("\n\n")
            labelText.text = title
            summary = extras.getString("summary")
            source = extras.getString("sourceUrl")
            val content = ingredients?.joinToString("\n\n")
            ingredientText.text = content
            summaryText.text = summary
            labelText.text = title
            time = extras.getString("time")?.toInt()
            timeButton.text = extras.getString("time")
            imageUrl = extras.getString("image")

            if (imageUrl == "" || imageUrl == null) {
                imageUri = Uri.parse(extras.getString("imageUri"))
                Glide.with(this).load(imageUri).into(image);

            } else {
                Glide.with(this).load(imageUrl).into(image);
            }

            deleteButton.setOnClickListener() {
                deleteRecipe(it)
            }

            timeButton.setOnClickListener() {
                val intent = Intent(this, Timer::class.java)
                intent.putExtra("time", time)

                startActivity(intent)
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