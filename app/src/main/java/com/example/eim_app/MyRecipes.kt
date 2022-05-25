package com.example.eim_app

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken


class MyRecipes : AppCompatActivity() {
    lateinit var listView : ListView
    lateinit var adapter : ArrayAdapter<String>
    var savedRecipes: ArrayList<RecipeSpoonacular> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_recipes)

        // Load data and add it to listView
        loadData()
        listView = findViewById(R.id.listView)
        adapter = ArrayAdapter(this, R.layout.item, R.id.myTextView, ArrayList<String>())
        listView.adapter = adapter
        savedRecipes?.forEach() {
            adapter.add(it.toString())
        }

        listView.setOnItemClickListener{ parent, _, position, _ ->
            Toast.makeText(this, "" + parent.getItemAtPosition(position), Toast.LENGTH_SHORT).show()
            for (recipe in savedRecipes) {
                if (recipe.title.toString() === parent.getItemAtPosition(position).toString()) {
                    val intent = Intent(this, SavedRecipeDetails::class.java)
                    intent.putExtra("title", recipe.title.toString())
                    intent.putExtra("ingredientLines", recipe.SpoonecularIngredientToString())
                    intent.putExtra("time", recipe.readyInMinutes.toString())
                    intent.putExtra("sourceUrl", recipe.sourceUrl)
                    intent.putExtra("summary", recipe.summary)
                    intent.putExtra("image", recipe.image)
                    startActivity(intent)
                }
            }
        }
    }

    override fun onRestart() {
        super.onRestart()
        finish()
        startActivity(intent)
    }

    // get saved recipes from sharedpreferences
    fun loadData() {
        val sharedPreferences = getSharedPreferences("shared preferences", Context.MODE_PRIVATE)
        val json = sharedPreferences.getString("recipeList", ArrayList<String>().toString())
        val collectionType = object : TypeToken<ArrayList<RecipeSpoonacular>>() {}.type
        savedRecipes = Gson().fromJson(json, collectionType)
        Log.d("GOT RECIPES", savedRecipes.toString())

    }
}