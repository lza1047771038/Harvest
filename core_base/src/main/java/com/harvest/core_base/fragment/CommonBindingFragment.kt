package com.open.core_base.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import com.harvest.core_base.coroutine.launch
import com.open.core_base.utils.system.StatusBarUtil

abstract class CommonBindingFragment<B : ViewDataBinding> : Fragment() {
    private var firstInit: Boolean = true

    private var binding: B? = null

    abstract fun initialBinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): B

    protected fun requireBinding(): B = binding!!

    abstract fun isLightStatusBar(): Boolean

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = initialBinding(inflater, container, savedInstanceState)
        binding?.lifecycleOwner = this
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    }

    override fun onStart() {
        super.onStart()
        if (firstInit) {
            launch {
                loadInitialize()
                initViewModel()
                loadData()
            }
            firstInit = false
        }
    }

    private fun preConfig() {
        val activity = activity as AppCompatActivity?
        if (activity != null) {
            StatusBarUtil.setStatusBarDarkTheme(activity, isLightStatusBar())
        }
    }

    protected open suspend fun loadInitialize() {}

    protected open suspend fun loadData() {}

    protected open suspend fun initViewModel(){}
}