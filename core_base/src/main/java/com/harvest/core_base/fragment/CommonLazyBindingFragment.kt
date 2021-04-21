package com.door.core_base.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import com.door.core_base.coroutine.launch
import com.door.core_base.interfaces.IFragmentWrapper

abstract class CommonLazyBindingFragment<B : ViewDataBinding> : Fragment(), IFragmentWrapper {

    private var firstIn: Boolean = true
    private var layoutResLoaded: Boolean = false

    private var rootView: FrameLayout? = null

    private var binding: B? = null

    override fun obtainLayoutId(): Int = 0

    abstract fun initialBinding(
        inflater: LayoutInflater
    ): B

    protected fun requireBinding(): B = binding!!

    override fun obtainLayoutRootView(
        layoutInflater: LayoutInflater,
        parent: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val frameLayout = FrameLayout(requireContext())
        val params = ViewGroup.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT
        )
        frameLayout.layoutParams = params
        rootView = frameLayout
        return rootView
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        var view: View? = null
        val layoutId = obtainLayoutId()
        if (layoutId != 0) {
            view = inflater.inflate(layoutId, container, false)
            layoutResLoaded = true
        }
        if (!layoutResLoaded) {
            view = obtainLayoutRootView(inflater, container, savedInstanceState)
            layoutResLoaded = true
        }
        return view
    }

    override fun onStart() {
        super.onStart()
        if (firstIn) {
            val binding = initialBinding(layoutInflater)
            binding.lifecycleOwner = this
            rootView?.addView(binding.root)
            launch {
                initViews()
                initViewModel()
                loadData()
            }
            this.binding = binding
        }
    }

    override fun onApplyTheme() {}

    override suspend fun initViews() {}

    override suspend fun initViewModel() {}

    override suspend fun loadData() {}
}