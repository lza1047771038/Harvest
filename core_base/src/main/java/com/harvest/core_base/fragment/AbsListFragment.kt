package com.open.core_base.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout

abstract class AbsListFragment : CommonFragment() {

    private var recyclerView: RecyclerView? = null
    protected var adapter: RecyclerView.Adapter<*>? = null

    private val isRefreshing: MutableLiveData<Boolean> by lazy {
        val liveData = MutableLiveData<Boolean>()
        liveData.observe(this@AbsListFragment) {
            val rootView = this@AbsListFragment.view
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

    override fun getRootView(
        layoutInflater: LayoutInflater,
        parent: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val swipeRefreshLayout = SwipeRefreshLayout(requireContext())
        val layoutParams = ViewGroup.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        swipeRefreshLayout.layoutParams = layoutParams
        swipeRefreshLayout.isEnabled = enableRefresh()
        swipeRefreshLayout.setOnRefreshListener(getRefreshListener())

        recyclerView = RecyclerView(requireContext())
        val linearLayoutParams = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )
        recyclerView?.layoutParams = linearLayoutParams
        swipeRefreshLayout.addView(recyclerView!!)

        return swipeRefreshLayout
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initRecyclerView(recyclerView!!)
        adapter = initAdapter()
        recyclerView?.adapter = adapter
    }

    protected abstract fun initRecyclerView(recyclerView: RecyclerView)

    protected abstract fun initAdapter(): RecyclerView.Adapter<*>
}