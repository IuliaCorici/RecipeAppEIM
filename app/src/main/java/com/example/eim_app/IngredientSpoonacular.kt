package com.example.eim_app

import com.fasterxml.jackson.annotation.JsonIgnoreProperties

@JsonIgnoreProperties(ignoreUnknown = true)
data class IngredientSpoonacular(var originalName: String? = null) {
    override fun toString() : String {
        return "$originalName"
    }

}
