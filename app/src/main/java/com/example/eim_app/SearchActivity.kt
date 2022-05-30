package com.example.eim_app

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.fasterxml.jackson.databind.ObjectMapper
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import org.json.JSONObject


class SearchActivity : AppCompatActivity() {
    lateinit var listView : ListView
    var listRecipeIds: ArrayList<RecipeId> = ArrayList()
    var listItems :  ArrayList<RecipeSpoonacular> = ArrayList()
    lateinit var adapter : ArrayAdapter<String>
    lateinit var searchBar : EditText
    lateinit var searchButton : Button
    lateinit var innerLayout : LinearLayout


    // How many items are showed to user. Default is 0-10.
    lateinit var numberItems : TextView
    var searchWord : String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.search_activity)
        listView = findViewById(R.id.listView)
        innerLayout = findViewById(R.id.innerLayout)
        searchBar = innerLayout.findViewById(R.id.searchBar)
        searchButton = innerLayout.findViewById(R.id.searchButton)
        searchButton.setOnClickListener() {
            searchClicked(it)
        }

        numberItems = findViewById(R.id.howManyItems)
        adapter = ArrayAdapter(this, R.layout.item, R.id.myTextView, ArrayList<String>())
        listView.adapter = adapter

        listView.setOnItemClickListener{ parent, _, position, _ ->
            Toast.makeText(this, "" + parent.getItemAtPosition(position), Toast.LENGTH_SHORT).show()
            for (recipe in listItems) {
                if (recipe.title.toString() === parent.getItemAtPosition(position).toString()) {
                    val intent = Intent(this, RecipeDetailsActivity::class.java)
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

    private fun searchClicked(button: View) {
        numberItems.text = "Showing results"
        searchWord = searchBar.text.toString()
        searchRecipes(searchWord)
    }


    fun searchRecipes(searchWord: String) {
        val url =
            "https://api.spoonacular.com/recipes/findByIngredients?ingredients=${searchWord}&number=10&apiKey=00aa3e5f53d94aacb817e1cb5f2578c9"

        downloadUrlAsync(this, url) { it ->
            // Clear existing lists
            adapter.clear()
            listItems.clear()

            val gson = Gson()
            val arrayTutorialType = object : TypeToken<Array<RecipeId>>() {}.type
            val recipesSpoonacular: Array<RecipeId> = gson.fromJson<Array<RecipeId>>(it, arrayTutorialType)

            recipesSpoonacular.forEach() {
                listRecipeIds.add(it)
            }

            searchRecipeById()
        }
    }

    fun searchRecipeById() {
        for (recipeId in listRecipeIds) {
            val url =
                "https://api.spoonacular.com/recipes/$recipeId/information?apiKey=00aa3e5f53d94aacb817e1cb5f2578c9&includeNutrition=true"

            downloadUrlAsync(this, url) { it ->

                val gson = Gson()
                val tutorialType = object : TypeToken<RecipeSpoonacular>() {}.type
                val recipesSpoonacular: RecipeSpoonacular = gson.fromJson(it, tutorialType)
                adapter.add(recipesSpoonacular.toString())
                listItems.add(recipesSpoonacular)

            }
        }
    }
}