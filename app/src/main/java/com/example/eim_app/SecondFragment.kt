package com.example.eim_app

import android.content.Context
import android.graphics.Typeface
import android.icu.util.Measure
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

/**
 * A simple [Fragment] subclass as the second destination in the navigation.
 */
class SecondFragment : Fragment() {
    lateinit var saveRecipeButton: Button
    lateinit var prev: Button
    lateinit var innerLayout: LinearLayout
    lateinit var ingredientsEditText: EditText


    val args: SecondFragmentArgs by navArgs()
    private var title : String = ""
    var ingredients : MutableList<String>? = null
    var spoonacularIngredients: ArrayList<IngredientSpoonacular> = ArrayList()
    var savedRecipes: ArrayList<RecipeSpoonacular> = ArrayList()
    var imageUriString : String? = ""
    var summary: String? = ""
    var time: Int? = 0
    var ingredientsLines: List<String>? = null
    var ingredientsList: Array<Array<String>>? = null


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_second, container, false)
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        title = args.title
        summary = args.summary
        time = args.time
        imageUriString = args.imageUriString

        innerLayout = view.findViewById(R.id.innerLayoutSaveNewRecipe)
        saveRecipeButton = innerLayout.findViewById(R.id.saveRecipeButton)
        prev = innerLayout.findViewById(R.id.button_second)
        ingredientsEditText = innerLayout.findViewById(R.id.ingredients_new_recipe)

        saveRecipeButton.setOnClickListener() {
            ingredientsLines = ingredientsEditText.text.toString().split("\n")
            for (line in ingredientsLines!!) {
                val ingrdientLine = line.split(" ")
                spoonacularIngredients.add(IngredientSpoonacular(ingrdientLine[2],
                    MeasureSpoonacular(MetricSpoonacular(ingrdientLine[0].toFloatOrNull(), ingrdientLine[1]))))

            }
            Log.d("GOT Ingredients", spoonacularIngredients[0].toString())
            loadData()
            if (!savedRecipes.any{ recipe -> recipe.title == title}) {
                Toast.makeText(this.activity, "Recipe saved", Toast.LENGTH_SHORT).show()
                saveData()
            } else {
                Toast.makeText(this.activity, "Already saved", Toast.LENGTH_SHORT).show()
            }
        }

        prev.setOnClickListener {
            findNavController().navigate(R.id.action_SecondFragment_to_FirstFragment)
        }
    }

    // Add new recipe to savedRecipes and save savedRecipes
    private fun saveData() {
        val myRecipeObject = RecipeSpoonacular(title = title, summary = summary,
            readyInMinutes = time, imageUri = imageUriString, extendedIngredients = spoonacularIngredients)
        savedRecipes.add(myRecipeObject)
        val sharedPreferences = this.activity?.getSharedPreferences("shared preferences", Context.MODE_PRIVATE)
        val editor = sharedPreferences?.edit()
        val json = Gson().toJson(savedRecipes)
        Log.d("GOT RECIPES", json.toString())
        editor?.putString("recipeList", json)
        editor?.apply()
    }

    private fun loadData() {
        val sharedPreferences = this.activity?.getSharedPreferences("shared preferences", Context.MODE_PRIVATE)
        val json = sharedPreferences?.getString("recipeList", ArrayList<String>().toString())
        val collectionType = object : TypeToken<ArrayList<RecipeSpoonacular>>() {}.type
        savedRecipes = Gson().fromJson(json, collectionType)
        Log.d("GOT RECIPES", savedRecipes.toString())
    }
}