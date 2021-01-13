package com.harvest.core_network.datasource

enum class Status {
    READY,
    SUCCESS,
    ERROR,
    LOADING,
    EMPTY,
    NOMORE,
    INSERTED,
    REMOVED,
    CLEAR
}

open class Resource<Result>(
    open val status: Status,
    open val data: Result? = null,
    open val throwable: Throwable? = null
) {
    fun isLoading(): Boolean {
        return status == Status.LOADING
    }

    fun isError(): Boolean {
        return status == Status.ERROR
    }

    fun isSuccess(): Boolean {
        return status == Status.SUCCESS
    }
}

class ParamResource<Param, Result> private constructor(
    override val status: Status,
    var param: Param? = null,
    override val data: Result? = null,
    override val throwable: Throwable? = null
) :
    Resource<Result>(status, data) {

    companion object {
        @JvmStatic
        fun <P, R> success(param: P?, data: R?): ParamResource<P, R> {
            return ParamResource(Status.SUCCESS, param, data)
        }

        @JvmStatic
        fun <P, R> error(
            param: P?,
            data: R? = null,
            throwable: Throwable? = null
        ): ParamResource<P, R> {
            return ParamResource(Status.ERROR, param, data, throwable)
        }

        @JvmStatic
        fun <P, R> loading(param: P?): ParamResource<P, R> {
            return ParamResource(Status.LOADING, param)
        }
    }
}