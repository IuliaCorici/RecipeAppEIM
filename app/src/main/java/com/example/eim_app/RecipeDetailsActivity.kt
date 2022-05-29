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
import com.bumptech.glide.Glide
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken


@SuppressLint("Registered")
class RecipeDetailsActivity : AppCompatActivity() {
    lateinit var labelText : TextView
    lateinit var scrollView : ScrollView
    lateinit var ingredientText : TextView
    lateinit var saveButton : Button
    lateinit var sourceButton : Button
    lateinit var timeButton : Button
    lateinit var image : ImageView
    lateinit var summaryText: TextView

    // Recipe info
    var source : String? = ""
    var title : String? = ""
    var ingredients : MutableList<String>? = null
    var savedRecipes: ArrayList<RecipeSpoonacular> = ArrayList()
    var summary: String? = ""
    var time: Int? = 0
    var imageUrl: String? = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recipe_details)
        Log.d("RecipeDetailsActivity", "onCreate()")

        labelText = findViewById(R.id.labelText)
        scrollView = findViewById(R.id.scrollView)
        ingredientText = scrollView.findViewById(R.id.ingredientText)
        saveButton = findViewById(R.id.saveButton)
        sourceButton = findViewById(R.id.sourceButton)
        timeButton = findViewById(R.id.timeButton)
        image = findViewById(R.id.imageView)
        summaryText = scrollView.findViewById(R.id.summaryText)

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
            summary = extras.getString("summary")
            source = extras.getString("sourceUrl")
            val content = ingredients?.joinToString("\n\n")
            ingredientText.text = content
            summaryText.text = summary
            labelText.text = title
            time = extras.getString("time")?.toInt()
            timeButton.text = extras.getString("time")
            imageUrl = extras.getString("image")

            Glide.with(this).load(imageUrl).into(image);
            sourceButton.setOnClickListener() {
                sourceButtonClicked(it)
            }
            timeButton.setOnClickListener() {
                val intent = Intent(this, Timer::class.java)
                intent.putExtra("time", time)
                startActivity(intent)
            }
        }
    }

    // Add new recipe to savedRecipes and save savedRecipes
    private fun saveData() {
        val myRecipeObject = RecipeSpoonacular(title = title, summary = summary,
            readyInMinutes = time, sourceUrl = source, image = imageUrl)
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

    // Start activity on browser with url
    private fun sourceButtonClicked(button: View) {
        val intent = Intent(Intent.ACTION_VIEW)
        intent.data = Uri.parse(source)
        startActivity(intent)
    }
}