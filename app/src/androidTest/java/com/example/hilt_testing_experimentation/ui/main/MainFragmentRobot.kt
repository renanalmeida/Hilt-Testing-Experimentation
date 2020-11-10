package com.example.hilt_testing_experimentation.ui.main

import androidx.test.espresso.Espresso.onData
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.hasDescendant
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import com.example.hilt_testing_experimentation.R
import com.example.hilt_testing_experimentation.domain.detailedpokemon.DetailedPokemon
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.CoreMatchers.allOf
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.CoreMatchers.instanceOf
import org.hamcrest.CoreMatchers.not
import org.hamcrest.Matchers.hasEntry

class MainFragmentRobot {

    fun checkErrorDisplayed(): MainFragmentRobot {
        onView(withText("An error occurred, try again later"))
            .check(matches(isDisplayed()))
        return this
    }

    fun checkLoadingDisplayed(): MainFragmentRobot {
        return checkLoadingVisibility(true)
    }

    fun checkLoadingHidden(): MainFragmentRobot {
        return checkLoadingVisibility(false)
    }

    fun checkPokemonDisplayed(name: String): MainFragmentRobot {
        onView(withId(R.id.recycler_main))
            .check(matches(hasDescendant(withText(name))))
        return this
    }

    private fun checkLoadingVisibility(isVisible: Boolean): MainFragmentRobot {
        onView(withText("Loading..."))
            .check(matches(if (isVisible) isDisplayed() else not(isDisplayed())))
        return this
    }
}