package com.harvest.core_base.activity

import android.net.Uri
import android.view.View
import android.view.ViewGroup
import androidx.viewpager.widget.PagerAdapter
import androidx.viewpager.widget.ViewPager
import com.facebook.drawee.view.SimpleDraweeView


abstract class BaseViewPagerActivity<M, V : View> : CommonActivity(),
    IViewPagerAdapterHelper<M, V> {

    protected var root: View? = null
    private var adapter: ViewPagerAdapter<M, V>? = null

    override fun obtainLayoutRootView(): View {
        if (root == null) {
            val viewPager = ViewPager(this)
            val params = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
            )
            viewPager.layoutParams = params
            root = viewPager
        }
        return root!!
    }

    override suspend fun initViews() {
        super.initViews()
        val rootView = root
        if (rootView != null && rootView is ViewPager) {
            adapter = ViewPagerAdapter(this)
            rootView.adapter = adapter
            rootView.currentItem = currentPosition()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        root = null
        adapter = null
    }
}

class ViewPagerAdapter<M, V : View>(private val viewPagerHelper: IViewPagerAdapterHelper<M, V>) :
    PagerAdapter() {

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val metaData = viewPagerHelper.getMetaData(position)
        val view = viewPagerHelper.getViewItem()
        if (metaData != null) {
            viewPagerHelper.bind(view, metaData)
        }
        container.addView(view)
        return view
    }

    override fun getCount(): Int = viewPagerHelper.getCount()

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view == `object`
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        if (`object` is SimpleDraweeView) {
            `object`.setImageURI(null as Uri?)
        }
    }
}

interface IViewPagerAdapterHelper<M, V : View> {

    fun getCount(): Int

    fun getViewItem(): V

    fun getMetaData(position: Int): M?

    fun bind(view: V, data: M)

    fun currentPosition(): Int
}