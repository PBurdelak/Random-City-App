package com.pburdelak.randomcityapp.screen.base

import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding

abstract class BaseFragment<B : ViewBinding> : Fragment() {

    private var _binding: B? = null
    protected var binding: B
        get() = _binding!!
        set(value) {
            value.root.isClickable = true
            value.root.isFocusable = true
            _binding = value
        }

    protected val navigator: BaseNavigator?
        get() = activity as? BaseNavigator

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
}
