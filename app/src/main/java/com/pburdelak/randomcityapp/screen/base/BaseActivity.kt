package com.pburdelak.randomcityapp.screen.base

import android.content.res.Resources
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.pburdelak.randomcityapp.R

abstract class BaseActivity : AppCompatActivity(), BaseNavigator {

    companion object {
        protected const val NAV_HOST_TAG = "NAV_HOST_TAG"
    }

    private var navHostFragment: NavHostFragment? = null
    override val navController: NavController?
        get() = navHostFragment?.navController

    protected open var navGraphId: Int = -1
    protected open var navHostFragmentContainerId: Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(createView())
        if (savedInstanceState == null) {
            setupNavHostFragment()
        } else {
            navHostFragment = supportFragmentManager.findFragmentByTag(NAV_HOST_TAG) as? NavHostFragment
        }
    }

    protected abstract fun createView(): View

    private fun setupNavHostFragment() {
        if (navGraphId == -1 || navHostFragmentContainerId == -1) return

        navHostFragment = NavHostFragment.create(navGraphId).also {
            supportFragmentManager.beginTransaction()
                .replace(navHostFragmentContainerId, it, NAV_HOST_TAG)
                .commitNow()
        }
    }

    override fun getTheme(): Resources.Theme {
        val theme = super.getTheme()
        theme.applyStyle(R.style.Theme_RandomCityApp, true)
        return theme
    }
}