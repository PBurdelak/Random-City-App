package com.pburdelak.randomcityapp.screen.activity

import android.os.Bundle
import androidx.activity.viewModels
import com.pburdelak.randomcityapp.R
import com.pburdelak.randomcityapp.databinding.ActivityMainBinding
import com.pburdelak.randomcityapp.screen.base.BaseActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : BaseActivity<ActivityMainBinding>() {

    private val viewModel: MainActivityViewModel by viewModels()

    override var navGraphId: Int = R.navigation.navigation_main
    override var navHostFragmentContainerId: Int = R.id.fragment_container_view

    override fun createViewBinding(): ActivityMainBinding =
        ActivityMainBinding.inflate(layoutInflater)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setSupportActionBar(binding.toolbar)
    }

    override fun onStart() {
        super.onStart()
        viewModel.startGenerator()
    }

    override fun onStop() {
        viewModel.stopGenerator()
        super.onStop()
    }
}