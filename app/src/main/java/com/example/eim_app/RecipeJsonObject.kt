package com.example.eim_app

import com.fasterxml.jackson.annotation.JsonIgnoreProperties

@JsonIgnoreProperties(ignoreUnknown = true)
data class RecipeJsonObject(var hits: MutableList<Hit>? = null)
