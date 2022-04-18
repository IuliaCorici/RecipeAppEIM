package com.example.eim_app

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import java.lang.Math.round
import java.math.RoundingMode
import java.text.DecimalFormat

@JsonIgnoreProperties(ignoreUnknown = true)
data class Hit(var recipe: Recipe? = null) {
    override fun toString() : String{
        return recipe.toString()
    }
}
