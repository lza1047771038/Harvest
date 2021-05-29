package com.lza.core_base.utils

import com.harvest.core_network.utils.BaseParamObserver
import com.harvest.harvestwork.utils.ToastUtil


class BaseObserver<Param, Result> : BaseParamObserver<Param, Result>() {
    override fun onSuccess(result: Result?, param: Param?) {}

    override fun onFailed(e: Throwable?, param: Param?) {
        ToastUtil.showToast(e?.message)
    }
}

class DefaultObserver<Param, Result>(
    private val dataSuccess: ((result: Result, param: Param) -> Unit)? = null,
    private val success: ((result: Result?, param: Param?) -> Unit)? = null,
    private val failed: ((e: Throwable?, param: Param?) -> Unit) = { e, param ->
        ToastUtil.showToast(e?.message)
    }
) : BaseParamObserver<Param, Result>() {

    override fun onData(result: Result, param: Param) {
        dataSuccess?.invoke(result, param)
    }

    override fun onSuccess(result: Result?, param: Param?) {
        success?.invoke(result, param)
    }

    override fun onFailed(e: Throwable?, param: Param?) {
        failed?.invoke(e, param)
    }

}