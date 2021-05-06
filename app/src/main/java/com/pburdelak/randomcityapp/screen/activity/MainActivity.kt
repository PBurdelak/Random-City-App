package com.pburdelak.randomcityapp.screen.activity

import android.content.res.Resources
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.pburdelak.randomcityapp.R
import com.pburdelak.randomcityapp.databinding.ActivityMainBinding
import com.pburdelak.randomcityapp.databinding.ActivityMainMasterDetailsBinding
import com.pburdelak.randomcityapp.screen.base.BaseNavigator
import com.pburdelak.randomcityapp.screen.details.DetailsFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity :  AppCompatActivity(), BaseNavigator {

    companion object {
        private const val MASTER_NAV_HOST_TAG = "MASTER_NAV_HOST_TAG"
        private const val DETAILS_FRAGMENT_TAG = "DETAILS_FRAGMENT_TAG"
    }

    private val viewModel: CombinationProducerViewModel by viewModels()
    private var toolbar: Toolbar? = null

    private var navHostFragment: NavHostFragment? = null

    override val navController: NavController?
        get() = navHostFragment?.navController

    val isTabletLandscape: Boolean
        get() = resources.getBoolean(R.bool.isTabletLandscape)


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(createView())
        configureToolbar()
        setupMasterNavHostFragment(savedInstanceState)
        setupDetailsFragment()
    }

    private fun createView(): View {
        return if (isTabletLandscape) {
            createMasterDetailsView()
        } else {
            createNormalView()
        }
    }

    private fun createMasterDetailsView(): View {
        return ActivityMainMasterDetailsBinding.inflate(layoutInflater).also {
            toolbar = it.toolbar
        }.root
    }

    private fun createNormalView(): View {
        return ActivityMainBinding.inflate(layoutInflater).also {
            toolbar = it.toolbar
        }.root
    }

    private fun setupMasterNavHostFragment(savedInstanceState: Bundle?) {
        navHostFragment = if (savedInstanceState == null) {
            createMasterFragment()
        } else {
            restoreMasterFragment()
        }
        if (isTabletLandscape && navController?.currentDestination?.id != R.id.destination_list) {
            navController?.navigateUp()
        }
    }

    private fun createMasterFragment() = NavHostFragment.create(R.navigation.navigation_main).also {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container_view, it, MASTER_NAV_HOST_TAG)
            .commitNow()
    }

    private fun restoreMasterFragment() =
        supportFragmentManager.findFragmentByTag(MASTER_NAV_HOST_TAG) as? NavHostFragment

    private fun setupDetailsFragment() {
        if (!isTabletLandscape) return
        restoreDetailsFragment() ?: createDetailsFragment()
    }

    private fun restoreDetailsFragment() =
        supportFragmentManager.findFragmentByTag(DETAILS_FRAGMENT_TAG) as? DetailsFragment

    private fun createDetailsFragment() = DetailsFragment().also {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container_view_details, it, DETAILS_FRAGMENT_TAG)
            .commitNow()
    }

    private fun configureToolbar() {
        val toolbar = toolbar ?: return
        setSupportActionBar(toolbar)
        navController?.let { navController ->
            val appBarConfiguration = AppBarConfiguration(navController.graph)
            toolbar.setupWithNavController(navController, appBarConfiguration)
        }
    }

    override fun getTheme(): Resources.Theme {
        val theme = super.getTheme()
        theme.applyStyle(R.style.Theme_RandomCityApp, true)
        return theme
    }

    override fun onStart() {
        super.onStart()
        viewModel.startGenerator()
    }

    override fun onStop() {
        viewModel.stopGenerator()
        super.onStop()
    }

    override fun onDestroy() {
        toolbar = null
        navHostFragment = null
        super.onDestroy()
    }
}