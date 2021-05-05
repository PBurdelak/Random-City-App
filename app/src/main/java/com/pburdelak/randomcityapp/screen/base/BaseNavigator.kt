package com.pburdelak.randomcityapp.screen.base

import androidx.navigation.NavController
import androidx.navigation.NavDirections
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import com.pburdelak.randomcityapp.R
import com.pburdelak.randomcityapp.utils.log

interface BaseNavigator {

    companion object {
        private val navOptions: NavOptions
            get() = NavOptions.Builder()
                .setEnterAnim(R.anim.enter_from_right)
                .setPopEnterAnim(R.anim.enter_from_left_partial)
                .setExitAnim(R.anim.exit_to_left_partial)
                .setPopExitAnim(R.anim.exit_to_right)
                .build()
    }

    val navController: NavController?

    fun navigateTo(direction: NavDirections) {
        try {
            navController?.navigate(
                direction.actionId,
                direction.arguments,
                navOptions,
                null
            )
        } catch (e: Exception) {
            e.log()
        }
    }

    fun navigateUp(fragment: BaseFragment<*>) {
        try {
            fragment.findNavController().navigateUp()
        } catch (e: Exception) {
            e.log()
        }
    }
}