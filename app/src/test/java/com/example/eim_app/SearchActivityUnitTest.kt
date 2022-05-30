package com.example.eim_app

import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.Matchers
import org.junit.Test

import org.junit.Assert.*
import org.junit.Before
import org.mockito.Mockito


/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class SearchActivityUnitTest {
    lateinit var SUT: SearchActivity
    @Before
    fun setUp() {
        SUT = Mockito.mock(SearchActivity::class.java)
    }

    @Test
    fun searchEmptyIngredient() {
        SUT.searchRecipes("")
        assertThat(SUT.listRecipeIds.size, `is` (0))
    }

    @Test
    fun searchNonEmptyIngredient() {
        SUT.searchRecipes("egg")
        assertThat(SUT.listRecipeIds.size, `is` (Matchers.greaterThan(1)))
    }

}

