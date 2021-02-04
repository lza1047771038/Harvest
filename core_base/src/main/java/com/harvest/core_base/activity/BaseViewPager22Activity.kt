package com.harvest.core_base.activity

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2

abstract class BaseViewPager22Activity<M, V : View> : CommonActivity(),
    IViewPagerAdapterHelper<M, V> {

    protected var root: View? = null
    private var a2Adapter: ViewPager2Adapter<M, V>? = null

    override fun getRootView(): View {
        if (root == null) {
            val viewPager = ViewPager2(this)
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
        val rootView = root
        if (rootView != null && rootView is ViewPager2) {
            a2Adapter = ViewPager2Adapter(this)
            rootView.adapter = a2Adapter
        }
    }
}

class ViewPager2Adapter<M, V : View>(private val viewPager2Helper: IViewPagerAdapterHelper<M, V>) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view = viewPager2Helper.getViewItem()
        return ViewPagerHolder<V>(view)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val meta: M? = viewPager2Helper.getMetaData(position)
        if (meta != null) {
            viewPager2Helper.bind(holder.itemView as V, meta)
        }
    }

    override fun getItemCount(): Int = viewPager2Helper.getCount()

    private class ViewPagerHolder<V>(view: View) : RecyclerView.ViewHolder(view)
}