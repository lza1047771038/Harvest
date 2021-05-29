package com.harvest.core_base.impl

import android.app.Application
import android.content.Context
import com.harvest.core_base.interfaces.IContext

class ContextResolver(context: Context) : IContext {
    private val mContext: Context = context

    override fun getContext(): Context = mContext

    override fun getApplicationContext(): Context = mContext.applicationContext

    override fun getApplication(): Application = (mContext.applicationContext as Application)

}