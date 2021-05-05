package com.pburdelak.randomcityapp.screen.base

import android.os.Bundle
import android.os.PersistableBundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.viewbinding.ViewBinding

abstract class BaseActivity<B : ViewBinding> : AppCompatActivity(), BaseNavigator {

    companion object {
        protected const val NAV_HOST_TAG = "NAV_HOST_TAG"
        private const val STATE_FULLSCREEN_GRAPH = "STATE_FULLSCREEN_GRAPH"
    }

    private var _binding: B? = null
    protected var binding: B
        get() = _binding!!
        set(value) { _binding = value }

    private var navHostFragment: NavHostFragment? = null
    override val navController: NavController?
        get() = navHostFragment?.navController

    protected var navGraphId: Int = -1
    protected var navHostFragmentContainerId: Int = -1

    override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
        super.onCreate(savedInstanceState, persistentState)

        if (savedInstanceState == null) {
            setupNavHostFragment()
        } else {
            navGraphId = savedInstanceState.getInt(STATE_FULLSCREEN_GRAPH, -1)
            navHostFragment = supportFragmentManager.findFragmentByTag(NAV_HOST_TAG) as? NavHostFragment
        }
    }

    private fun setupNavHostFragment() {
        if (navGraphId == -1 || navHostFragmentContainerId == -1) return

        navHostFragment = NavHostFragment.create(navGraphId).also {
            supportFragmentManager.beginTransaction()
                .replace(navHostFragmentContainerId, it, NAV_HOST_TAG)
                .commitNow()
        }
    }

    override fun onDestroy() {
        _binding = null
        super.onDestroy()
    }
}