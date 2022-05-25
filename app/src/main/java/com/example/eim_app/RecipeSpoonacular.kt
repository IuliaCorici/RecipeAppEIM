package com.example.eim_app

import com.fasterxml.jackson.annotation.JsonIgnoreProperties

@JsonIgnoreProperties(ignoreUnknown = true)
data class RecipeSpoonacular(var title: String? = null, var readyInMinutes: Int? = null,
                             var sourceUrl: String? = null, var summary: String?, var image: String? = null,
                             var extendedIngredients: MutableList<IngredientSpoonacular>? = null) {

    override fun toString() : String {
        return "$title"
    }

    fun SpoonecularIngredientToString() : String {
        var str = ""
        if (extendedIngredients != null) {
            for(line in this.extendedIngredients!!) {
                str += line.toString() + "NEW"
            }
        }
        return str
    }
}