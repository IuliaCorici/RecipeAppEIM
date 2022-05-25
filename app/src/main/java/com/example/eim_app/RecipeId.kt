package com.example.eim_app

import com.fasterxml.jackson.annotation.JsonIgnoreProperties

@JsonIgnoreProperties(ignoreUnknown = true)
data class RecipeId(var id: Int? = null) {
    override fun toString() : String {
        return "$id"
    }

}
