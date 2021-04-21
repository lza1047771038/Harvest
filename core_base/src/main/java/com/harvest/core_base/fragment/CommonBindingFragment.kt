package com.harvest.core_base.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.ViewDataBinding
import com.open.core_base.utils.system.StatusBarUtil

abstract class CommonBindingFragment<B : ViewDataBinding> : CommonFragment() {

    private var binding: B? = null

    abstract fun initialBinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): B

    protected fun requireBinding(): B = binding!!

    abstract fun isLightStatusBar(): Boolean

    override fun obtainLayoutRootView(
        layoutInflater: LayoutInflater,
        parent: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = initialBinding(layoutInflater, parent, savedInstanceState)
        binding?.lifecycleOwner = this
        return binding?.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding?.unbind()
        binding = null
    }
}