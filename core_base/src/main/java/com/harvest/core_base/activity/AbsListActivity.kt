package com.harvest.core_base.activity

import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout

abstract class AbsListActivity : CommonActivity() {

    private var rootView: View? = null
    private var recyclerView: RecyclerView? = null
    protected var adapter: RecyclerView.Adapter<*>? = null

    private val isRefreshing: MutableLiveData<Boolean> by lazy {
        val liveData = MutableLiveData<Boolean>()
        liveData.observe(this@AbsListActivity) {
            val rootView = this@AbsListActivity.obtainLayoutRootView()
            if (rootView is SwipeRefreshLayout) {
                rootView.isRefreshing = it
            }
        }
        liveData
    }

    protected open fun enableRefresh(): Boolean = true

    protected open fun getRefreshListener(): SwipeRefreshLayout.OnRefreshListener? = null

    protected fun setRefreshing(isRefreshing: Boolean) {
        this.isRefreshing.postValue(isRefreshing)
    }

    override fun obtainLayoutRootView(): View? {
        if (rootView == null) {
            val swipeRefreshLayout = SwipeRefreshLayout(this)
            val layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
            swipeRefreshLayout.layoutParams = layoutParams
            swipeRefreshLayout.isEnabled = enableRefresh()
            swipeRefreshLayout.setOnRefreshListener(getRefreshListener())

            recyclerView = RecyclerView(this)
            val linearLayoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
            recyclerView?.layoutParams = linearLayoutParams
            swipeRefreshLayout.addView(recyclerView!!)
            rootView = swipeRefreshLayout
        }
        return rootView
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initRecyclerView(recyclerView!!)
        adapter = initAdapter()
        recyclerView?.adapter = adapter
    }

    protected open fun initRecyclerView(recyclerView: RecyclerView) {}

    protected abstract fun initAdapter(): RecyclerView.Adapter<*>
}