package com.open.core_base.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.ViewDataBinding
import com.harvest.core_base.coroutine.launch
import com.harvest.core_base.fragment.CommonFragment
import com.open.core_base.utils.system.StatusBarUtil

abstract class CommonBindingFragment<B : ViewDataBinding> : CommonFragment() {
    private var firstInit: Boolean = true

    private var binding: B? = null

    abstract fun initialBinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): B

    protected fun requireBinding(): B = binding!!

    abstract fun isLightStatusBar(): Boolean

    override fun getRootView(
        layoutInflater: LayoutInflater,
        parent: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = initialBinding(layoutInflater, parent, savedInstanceState)
        binding?.lifecycleOwner = this
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    }

    override fun onStart() {
        if (firstInit) {
            launch {
                initViewModel()
            }
            firstInit = false
        }
        super.onStart()
    }

    private fun preConfig() {
        val activity = activity as AppCompatActivity?
        if (activity != null) {
            StatusBarUtil.setStatusBarDarkTheme(activity, isLightStatusBar())
        }
    }

    protected open suspend fun initViewModel() {}
}