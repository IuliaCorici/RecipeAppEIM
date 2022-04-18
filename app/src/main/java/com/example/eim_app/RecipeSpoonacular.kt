package com.example.eim_app

import com.fasterxml.jackson.annotation.JsonIgnoreProperties

@JsonIgnoreProperties(ignoreUnknown = true)
data class RecipeSpoonacular(var id: Int? = null, var title: String? = null, var image: String? = null, var missedIngredients: MutableList<IngredientSpoonacular>? = null) {

    override fun toString() : String {
        return "$title"
    }

    fun SpoonecularIngredientToString() : String {
        var str = ""
        if (missedIngredients != null) {
            for(line in this!!.missedIngredients!!) {
                str += line.toString() + "NEW"
            }
        }
        return str
    }
}