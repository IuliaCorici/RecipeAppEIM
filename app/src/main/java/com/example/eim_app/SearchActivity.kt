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
    var listItems :  ArrayList<RecipeSpoonacular> = ArrayList()

    lateinit var adapter : ArrayAdapter<String>


    lateinit var searchBar : EditText
    lateinit var searchButton : Button
    lateinit var innerLayout : LinearLayout


    // How many items are showed to user. Default is 0-10.
    lateinit var numberItems : TextView

    private var searchWord : String = ""

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
                    startActivity(intent)
                }
            }
        }
    }

    private fun searchClicked(button: View) {
        numberItems.text = "Showing results"
        searchWord = searchBar.text.toString()
        searchRecipes()
    }


    private fun searchRecipes() {
        val url = "https://api.spoonacular.com/recipes/findByIngredients?ingredients=${searchWord}&number=10&apiKey=7ade310266e44c14990e59f6a235a038"

        downloadUrlAsync(this, url) { it ->
            // Clear existing lists
            adapter.clear()
            listItems.clear()

            Log.d("GOT RECIPES", it)

            val gson = Gson()
            val arrayTutorialType = object : TypeToken<Array<RecipeSpoonacular>>() {}.type
            var recipesSpoonacular: Array<RecipeSpoonacular> = gson.fromJson(it, arrayTutorialType)
            recipesSpoonacular.forEachIndexed  { idx, tut -> Log.d("Recipe","> Item ${idx}:\n${tut}") }


            recipesSpoonacular?.forEach() {
                adapter.add(it.toString())
                listItems.add(it)
            }
        }


    }
}