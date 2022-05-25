package com.example.eim_app

import com.fasterxml.jackson.annotation.JsonIgnoreProperties

@JsonIgnoreProperties(ignoreUnknown = true)
data class MetricSpoonacular(var amount: Float? = null, var unitShort: String? = null) {
    override fun toString() : String {
        return "$amount  $unitShort"
    }
}

@JsonIgnoreProperties(ignoreUnknown = true)
data class MeasureSpoonacular(var metric: MetricSpoonacular? = null) {
    override fun toString() : String {
        return metric.toString()
    }
}

@JsonIgnoreProperties(ignoreUnknown = true)
data class IngredientSpoonacular(var name: String? = null, var measures: MeasureSpoonacular? = null) {
    override fun toString() : String {
        return "${measures.toString()} $name"
    }
}

