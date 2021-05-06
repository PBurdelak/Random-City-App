package com.pburdelak.randomcityapp.screen.base

import android.content.res.Resources
import android.os.Bundle
import android.os.PersistableBundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.viewbinding.ViewBinding
import com.pburdelak.randomcityapp.R
import timber.log.Timber

abstract class BaseActivity<B : ViewBinding> : AppCompatActivity(), BaseNavigator {

    companion object {
        protected const val NAV_HOST_TAG = "NAV_HOST_TAG"
        private const val STATE_NAVIGATION_GRAPH = "STATE_NAVIGATION_GRAPH"
    }

    private var _binding: B? = null
    protected val binding: B
        get() = _binding!!

    private var navHostFragment: NavHostFragment? = null
    override val navController: NavController?
        get() = navHostFragment?.navController

    protected open var navGraphId: Int = -1
    protected open var navHostFragmentContainerId: Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = createViewBinding()
        setContentView(binding.root)
        if (savedInstanceState == null) {
            setupNavHostFragment()
        } else {
            navGraphId = savedInstanceState.getInt(STATE_NAVIGATION_GRAPH, -1)
            navHostFragment = supportFragmentManager.findFragmentByTag(NAV_HOST_TAG) as? NavHostFragment
        }
    }

    protected abstract fun createViewBinding(): B

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

    override fun onDestroy() {
        _binding = null
        super.onDestroy()
    }
}