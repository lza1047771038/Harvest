package com.harvest.core_base.activity

import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2

abstract class CommonViewPager22Activity : CommonActivity(),
    IViewPager2AdapterHelper {

    protected var root: View? = null
    protected var viewPager2: ViewPager2? = null
    private var a2Adapter: ViewPager2Adapter? = null
    private var currentFragment: Fragment? = null

    private val onPageChangeCallback = object : ViewPager2.OnPageChangeCallback() {
        override fun onPageSelected(position: Int) {
            val newDisplayFragment = getFragment(position)
            val transaction = supportFragmentManager.beginTransaction()
            if (newDisplayFragment.fragmentManager == supportFragmentManager) {
                transaction.setMaxLifecycle(newDisplayFragment, Lifecycle.State.RESUMED)
                    .commitNow()
            }

            val current = currentFragment
            if (current != null && current.fragmentManager == supportFragmentManager) {
                transaction
                    .setMaxLifecycle(current, Lifecycle.State.STARTED).commitNow()
            }
            currentFragment = newDisplayFragment
        }
    }

    override fun obtainLayoutRootView(): View? {
        val viewPager2: ViewPager2?
        if (root == null) {
            viewPager2 = ViewPager2(this)
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

    open fun transparentStatusBarEnabled():Boolean = false

    override suspend fun initViews() {
        val viewPager2 = viewPager2
        if (viewPager2 != null) {
            a2Adapter = ViewPager2Adapter(this, this)
            viewPager2.adapter = a2Adapter
            viewPager2.currentItem = currentPosition()
        }
    }
}

class ViewPager2Adapter(
    private val viewPager2AdapterHelper: IViewPager2AdapterHelper,
    private val activity: FragmentActivity
) :
    FragmentStateAdapter(activity) {

    override fun getItemCount(): Int = viewPager2AdapterHelper.getItemCount()

    override fun createFragment(position: Int): Fragment =
        viewPager2AdapterHelper.getFragment(position)
}


interface IViewPager2AdapterHelper {
    fun getItemCount(): Int
    fun getFragment(position: Int): Fragment
    fun currentPosition(): Int
}