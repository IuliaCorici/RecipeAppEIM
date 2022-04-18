package com.example.eim_app

import android.content.Intent
import android.os.AsyncTask
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.*
import com.fasterxml.jackson.databind.ObjectMapper

class SearchActivity : AppCompatActivity() {
    lateinit var listView : ListView
    var listItems :  ArrayList<Recipe> = ArrayList()

    lateinit var adapter : ArrayAdapter<String>


    lateinit var searchBar : EditText
    lateinit var searchButton : Button
    lateinit var innerLayout : LinearLayout

    // How many items are showed to user. Default is 0-15
    lateinit var numberItems : TextView
    private var min : Int = 0
    private var max : Int = 15

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

        // Each item on listview has onclick listener that starts RecipeDetailsActivity
        listView.setOnItemClickListener{ parent, _, position, _ ->
            Toast.makeText(this, "" + parent.getItemAtPosition(position), Toast.LENGTH_SHORT).show()
            for (recipe in listItems) {
                if (recipe.label.toString() === parent.getItemAtPosition(position).toString()) {
                    val intent = Intent(this, RecipeDetailsActivity::class.java)
                    intent.putExtra("label", recipe.label.toString())
                    intent.putExtra("sourceUrl", recipe.url.toString())
                    intent.putExtra("source", recipe.source.toString())
                    intent.putExtra("ingredientLines", recipe.ingredientLinesToString())
                    startActivity(intent)
                }
            }
        }
    }

    private fun searchClicked(button: View) {
        numberItems.text = "Showing results: ${min} - ${max}"
        searchWord = searchBar.text.toString()
        searchRecipes()
    }


    private fun searchRecipes() {
        val yourAppId = "733a1d49"
        val yourAppKey = "7ee8ff5ee0fbcd36346bd22cda06d6ca"
        val url = "https://api.edamam.com/search?q=${searchWord}&app_id=${yourAppId}&app_key=${yourAppKey}&&from=${min}&to=${max}&calories=591-722&health=alcohol-free"

        downloadUrlAsync(this, url) { it ->
            // Clear existing lists
            adapter.clear()
            listItems.clear()

            // Get new recipes
            val mp = ObjectMapper()
            val myObject: RecipeJsonObject
            myObject = mp.readValue(it, RecipeJsonObject::class.java)
            val recipes: MutableList<Hit>?
            recipes = myObject.hits

            // Add new recipes to list
            recipes?.forEach() {
                adapter.add(it.toString())
                it.recipe?.let { it1 -> listItems.add(it1) }
            }
        }
    }
}