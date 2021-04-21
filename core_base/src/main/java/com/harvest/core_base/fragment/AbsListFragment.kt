package com.harvest.core_base.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.harvest.core_base.databinding.LayoutBaseRecyclerViewBinding

abstract class AbsListFragment : CommonBindingFragment<LayoutBaseRecyclerViewBinding>() {

    protected var adapter: RecyclerView.Adapter<*>? = null

    private val isRefreshing: MutableLiveData<Boolean> by lazy { MutableLiveData() }

    override fun initialBinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): LayoutBaseRecyclerViewBinding =
        LayoutBaseRecyclerViewBinding.inflate(inflater, container, false)

    override fun isLightStatusBar(): Boolean = false

    protected open fun enableRefresh(): Boolean = true

    protected open fun getRefreshListener(): SwipeRefreshLayout.OnRefreshListener? = null

    protected fun setRefreshing(isRefreshing: Boolean) {
        this.isRefreshing.postValue(isRefreshing)
    }

    override suspend fun initViews() {
        super.initViews()
        isRefreshing.observe(this@AbsListFragment) {
            requireBinding().swipeRefreshLayout.isRefreshing = it
        }
        requireBinding().run {
            swipeRefreshLayout.isEnabled = enableRefresh()
            swipeRefreshLayout.setOnRefreshListener(getRefreshListener())
            initRecyclerView(recyclerView)
            adapter = initAdapter()
            recyclerView.adapter = adapter
        }
    }

    protected abstract fun initRecyclerView(recyclerView: RecyclerView)

    protected abstract fun initAdapter(): RecyclerView.Adapter<*>

    override fun onDestroyView() {
        requireBinding().recyclerView.adapter = null
        super.onDestroyView()
    }
}