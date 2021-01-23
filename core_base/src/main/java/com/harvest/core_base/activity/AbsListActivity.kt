package com.open.core_base.activity

import android.os.Bundle
import androidx.recyclerview.widget.RecyclerView

abstract class AbsListActivity : CommonActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    protected open fun initRecyclerView(recyclerView: RecyclerView) {}

    protected abstract fun initAdapter(): RecyclerView.Adapter<*>
}