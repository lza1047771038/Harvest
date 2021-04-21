package com.harvest.core_base.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.widget.ViewPager2
import com.harvest.core_base.activity.IViewPager2AdapterHelper
import com.harvest.core_base.activity.ViewPager2Adapter

abstract class CommonViewPager2Fragment : CommonFragment(), IViewPager2AdapterHelper {

    protected var root: View? = null
    protected var viewPager2: ViewPager2? = null
    private var a2Adapter: ViewPager2Adapter? = null
    private var currentFragment: Fragment? = null

    private val onPageChangeCallback = object : ViewPager2.OnPageChangeCallback() {
        override fun onPageSelected(position: Int) {
            val newDisplayFragment = getFragment(position)
            val transaction = childFragmentManager.beginTransaction()
            if (newDisplayFragment.fragmentManager == childFragmentManager) {
                transaction.setMaxLifecycle(newDisplayFragment, Lifecycle.State.RESUMED)
                    .commitNow()
            }

            val current = currentFragment
            if (current != null && current.fragmentManager == childFragmentManager) {
                transaction
                    .setMaxLifecycle(current, Lifecycle.State.STARTED).commitNow()
            }
            currentFragment = newDisplayFragment
        }
    }

    override fun obtainLayoutRootView(
        layoutInflater: LayoutInflater,
        parent: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val viewPager2: ViewPager2?
        if (root == null) {
            viewPager2 = ViewPager2(requireContext())
            val params = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
            )
            viewPager2.layoutParams = params
            viewPager2.registerOnPageChangeCallback(onPageChangeCallback)
            this.viewPager2 = viewPager2
            root = obtainViewPager(viewPager2)
        }
        return root
    }

    open fun obtainViewPager(viewPager2: ViewPager2): View? = viewPager2

    override suspend fun initViews() {
        super.initViews()
        val viewPager2 = viewPager2
        if (viewPager2 != null) {
            a2Adapter = ViewPager2Adapter(this, childFragmentManager, lifecycle)
            viewPager2.adapter = a2Adapter
            viewPager2.currentItem = currentPosition()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        viewPager2?.unregisterOnPageChangeCallback(onPageChangeCallback)
        currentFragment = null
        viewPager2 = null
        a2Adapter = null
        root = null
    }

    override fun onDestroy() {
        super.onDestroy()
        root = null
    }
}