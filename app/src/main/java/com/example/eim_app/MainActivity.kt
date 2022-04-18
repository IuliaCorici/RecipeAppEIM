package com.example.eim_app

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import com.example.eim_app.R.id.SearchActivityButton

class MainActivity : AppCompatActivity() {
    private lateinit var myRecipes : Button
    private lateinit var searchActivity : Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        myRecipes = findViewById(R.id.my_recipes_button)
        myRecipes.setOnClickListener() {
            changeActivityToMyRecipes(it)
        }

        searchActivity = findViewById(SearchActivityButton)
        searchActivity.setOnClickListener() {
            changeActivityToSearchRecipes(it)
        }
    }

    private fun changeActivityToMyRecipes(button: View) {
        val intent = Intent(this, MyRecipes::class.java)
        startActivity(intent)
    }

    private fun changeActivityToSearchRecipes(button: View) {
        val intent = Intent(this, SearchActivity::class.java)
        startActivity(intent)
    }
}