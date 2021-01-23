package com.open.core_base.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.harvest.core_base.coroutine.launch

abstract class CommonFragment : Fragment() {
    private var firstIn = true
    private var layoutResLoaded: Boolean = false

    protected open fun getLayoutId(): Int = 0

    protected open fun getRootView(
        layoutInflater: LayoutInflater,
        parent: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        var view: View? = null
        val layoutId = getLayoutId()
        if (layoutId != 0) {
            view = inflater.inflate(layoutId, container, false)
            layoutResLoaded = true
        }
        if (!layoutResLoaded) {
            view = getRootView(inflater, container, savedInstanceState)
            layoutResLoaded = true
        }
        return view
    }

    override fun onStart() {
        super.onStart()
        if (firstIn) {
            launch {
                loadInitial()
                loadData()
            }
            firstIn = false
        }
    }

    protected open suspend fun loadInitial() {}

    protected open suspend fun loadData() {}
}