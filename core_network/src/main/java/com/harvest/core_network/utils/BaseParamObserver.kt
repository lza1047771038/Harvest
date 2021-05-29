package com.harvest.core_network.utils

import androidx.lifecycle.Observer
import com.harvest.core_network.datasource.ParamResource

abstract class BaseParamObserver<Param, Result> : Observer<ParamResource<Param, Result>> {

    override fun onChanged(t: ParamResource<Param, Result>) {
        val isSuccess = t.isSuccess()
        val param: Param? = t.param
        val result: Result? = t.data
        val throwable: Throwable? = t.throwable
        val isError = t.isError()

        if (isError) {
            onFailed(throwable, param)
            return
        }

        if (isSuccess) {

            onSuccess(result, param)

            if (result != null && param != null) {
                onData(result, param)
            }
            return
        }
    }

    open fun onData(result: Result, param: Param) {}

    abstract fun onSuccess(result: Result?, param: Param?)

    abstract fun onFailed(e: Throwable?, param: Param?)
}